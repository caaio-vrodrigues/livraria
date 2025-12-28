package caio.portfolio.livraria.service.book.salable;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.BookSellListDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.CreateSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.ResponseSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.UpdateSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.repository.SalableBookRepository;
import caio.portfolio.livraria.model.enums.Genre;
import caio.portfolio.livraria.service.author.AuthorService;
import caio.portfolio.livraria.service.book.salable.dto.TitleAndAuthorUpdateDTO;
import caio.portfolio.livraria.service.book.salable.model.BookSeller;
import caio.portfolio.livraria.service.book.salable.model.ResponseSalableBookDTOCreator;
import caio.portfolio.livraria.service.book.salable.model.SalableBookFinder;
import caio.portfolio.livraria.service.book.salable.model.SalableBookSaverAndConcurrencyHandle;
import caio.portfolio.livraria.service.book.salable.model.SalableBookUniquenessValidator;
import caio.portfolio.livraria.service.book.salable.model.SalableBookUpdateValidator;
import caio.portfolio.livraria.service.publisher.PublisherService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalableBookService {

	private final SalableBookRepository repo;
	private final ResponseSalableBookDTOCreator responseSalableBookDTOCreator;
	private final SalableBookUpdateValidator salableBookUpdateValidator;
	private final SalableBookSaverAndConcurrencyHandle salableBookSaverAndConcurrencyHandleImpl;
	private final SalableBookUniquenessValidator salableBookUniquenessValidator;
	private final BookSeller bookSeller;
	private final SalableBookFinder salableBookFinder;
	private final AuthorService authorService;
	private final PublisherService publisherService;

	@Transactional
	public ResponseSalableBookDTO createSalableBook(CreateSalableBookDTO dto) {
		Author author = authorService.getAuthorById(dto.getAuthorId());
		salableBookUniquenessValidator
			.validateUniquenessOnCreate(author, dto.getTitle());
		Publisher publisher = publisherService
			.getPublisherById(dto.getPublisherId());
		SalableBook newBook = SalableBook.builder()
			.title(dto.getTitle())
			.genre(dto.getGenre())
			.author(author)
			.publisher(publisher)
			.isbn(dto.getIsbn())
			.price(dto.getPrice())
			.units(dto.getUnits())
			.build();
		return responseSalableBookDTOCreator
			.toResponseSalableBookDTO(salableBookSaverAndConcurrencyHandleImpl
				.saveAndHandleConcurrency(newBook));
	}

	@Transactional(readOnly=true)
	public List<ResponseSalableBookDTO> getAllResponseSalableBookDTOs() {
		return repo.findAll()
			.stream()
			.map(responseSalableBookDTOCreator::toResponseSalableBookDTO)
			.toList();
	}

	@Transactional(readOnly=true)
	public ResponseSalableBookDTO getResponseSalableBookDTOById(Long id) {
		return responseSalableBookDTOCreator
			.toResponseSalableBookDTO(salableBookFinder.findById(id));
	}

	@Transactional(readOnly=true)
	public List<ResponseSalableBookDTO> getResponseSalableBookDTOByAuthorId(
		Long authorId
	){
		Author author = authorService.getAuthorById(authorId);
		return salableBookFinder.findByAuthor(author).stream()
			.map(responseSalableBookDTOCreator::toResponseSalableBookDTO)
			.toList();
	}
	
	@Transactional(readOnly=true)
	public List<ResponseSalableBookDTO> getResponseSalableBookDTOByPublisherId(
		Long publisherId
	){
		Publisher publisher = publisherService.getPublisherById(publisherId);
		return salableBookFinder.findByPublisher(publisher).stream()
			.map(responseSalableBookDTOCreator::toResponseSalableBookDTO)
			.toList();
	}

	@Transactional(readOnly=true)
	public List<ResponseSalableBookDTO> getResponseSalableBookDTOByTitle(
		String title
	){
		return salableBookFinder.findByTitle(title).stream()
			.map(responseSalableBookDTOCreator::toResponseSalableBookDTO)
			.toList();
	}

	@Transactional(readOnly=true)
	public List<ResponseSalableBookDTO> getResponseSalableBookDTOByGenre(
		Genre genre
	){
		return salableBookFinder.findByGenre(genre).stream()
			.map(responseSalableBookDTOCreator::toResponseSalableBookDTO)
			.toList();
	}

	@Transactional(readOnly=true)
	public List<ResponseSalableBookDTO> getResponseSalableBookDTOByIsbn(
		String isbn
	){
		return salableBookFinder.findByIsbn(isbn).stream()
			.map(responseSalableBookDTOCreator::toResponseSalableBookDTO)
			.toList();
	}

	@Transactional
	public ResponseSalableBookDTO updateSalableBookById(
		Long id, UpdateSalableBookDTO dto
	){
		SalableBook bookToUpdate = salableBookFinder.findById(id);
		TitleAndAuthorUpdateDTO titleAndAuthorUpdateDTO = salableBookUpdateValidator
			.validateTitleAndAuthor(
				bookToUpdate.getTitle(), 
				dto.getTitle(), 
				bookToUpdate.getAuthor(), 
				dto.getAuthorId());
		salableBookUniquenessValidator.validateUniquenessOnUpdate(
			titleAndAuthorUpdateDTO, 
			bookToUpdate.getTitle(), 
			bookToUpdate.getAuthor().getId());
		SalableBook updatedSalableBook = SalableBook.builder()
			.id(bookToUpdate.getId())
			.title(titleAndAuthorUpdateDTO.getTitle())
			.author(titleAndAuthorUpdateDTO.getAuthor())
			.genre(salableBookUpdateValidator
				.validateGenre(bookToUpdate.getGenre(), dto.getGenre()))
			.publisher(salableBookUpdateValidator
				.validatePublisher(bookToUpdate.getPublisher(), dto.getPublisherId()))
			.isbn(salableBookUpdateValidator
				.validateIsbn(bookToUpdate.getIsbn(), dto.getIsbn()))
			.price(salableBookUpdateValidator
				.validatePrice(bookToUpdate.getPrice(), dto.getPrice()))
			.units(salableBookUpdateValidator
				.validateUnits(bookToUpdate.getUnits(), dto.getUnits()))
			.build();
		return responseSalableBookDTOCreator
			.toResponseSalableBookDTO(salableBookSaverAndConcurrencyHandleImpl
				.saveAndHandleConcurrency(updatedSalableBook));
	}

	@Transactional
	public BigDecimal sellBooks(BookSellListDTO bookListDTO) {
		return bookSeller.sellBooks(bookListDTO);
	}

	@Transactional
	public Boolean deleteSalableBookById(Long id) {
		salableBookFinder.existsById(id);
		repo.deleteById(id);
		return true;
	}
}