package caio.portfolio.livraria.service.book.salable;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
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
	private final AuthorService authorService;
	private final PublisherService publisherService;

	@Transactional
	public ResponseSalableBookDTO createSalableBook(CreateSalableBookDTO dto) {
		Author author = authorService
			.getAuthorById(dto.getAuthorId());
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
		return responseSalableBookDTOCreator.toResponseSalableBookDTO(repo.findById(id)
			.orElseThrow(() -> new SalableBookNotFoundException("Não foi possível encontrar livro com 'id': '"+id+"'")));
	}

	@Transactional(readOnly=true)
	public List<ResponseSalableBookDTO> getResponseSalableBookDTOByAuthorId(Long authorId) {
		Author author = authorService.getAuthorById(authorId);
		List<SalableBook> bookList = repo.findByAuthor(author);
		if(bookList.isEmpty()) 
			throw new SalableBookNotFoundException("Não foi possível encontrar livro para o autor: '"+author.getFullName()+"'");
		return bookList.stream()
			.map(responseSalableBookDTOCreator::toResponseSalableBookDTO)
			.toList();
	}

	@Transactional(readOnly=true)
	public List<ResponseSalableBookDTO> getResponseSalableBookDTOByPublisherId(Long publisherId) {
		Publisher publisher = publisherService.getPublisherById(publisherId);
		List<SalableBook> bookList = repo.findByPublisher(publisher);
		if(bookList.isEmpty()) 
			throw new SalableBookNotFoundException("Não foi possível encontrar livro para a editora: '"+publisher.getName()+"'");
		return bookList.stream()
			.map(responseSalableBookDTOCreator::toResponseSalableBookDTO)
			.toList();
	}

	@Transactional(readOnly=true)
	public List<ResponseSalableBookDTO> getResponseSalableBookDTOByTitle(String title) {
		List<SalableBook> bookList = repo.findByTitle(title);
		if(bookList.isEmpty())
			throw new SalableBookNotFoundException("Não foi possível encontrar livro para o 'title': '"+title+"'");
		return bookList.stream()
			.map(responseSalableBookDTOCreator::toResponseSalableBookDTO)
			.toList();
	}

	@Transactional(readOnly=true)
	public List<ResponseSalableBookDTO> getResponseSalableBookDTOByGenre(Genre genre) {
		List<SalableBook> bookList = repo.findByGenre(genre);
		if(bookList.isEmpty())
			throw new SalableBookNotFoundException("Não foi possível encontrar livro para o 'genre': '"+genre.name()+"'");
		return bookList.stream()
			.map(responseSalableBookDTOCreator::toResponseSalableBookDTO)
			.toList();
	}

	@Transactional(readOnly=true)
	public List<ResponseSalableBookDTO> getResponseSalableBookDTOByIsbn(String isbn) {
		List<SalableBook> bookList = repo.findByIsbn(isbn);
		if(bookList.isEmpty())
			throw new SalableBookNotFoundException("Não foi possível encontrar livro para o 'isbn': '"+isbn+"'");
		return bookList.stream()
			.map(responseSalableBookDTOCreator::toResponseSalableBookDTO)
			.toList();
	}

	@Transactional
	public ResponseSalableBookDTO updateSalableBookByTitleAndAuthor(
		Long authorId, String title, UpdateSalableBookDTO dto
	){
		Author currentAuthor = authorService.getAuthorById(authorId);
		SalableBook bookToUpdate = repo
			.findByTitleAndAuthor(title, currentAuthor)
				.orElseThrow(() -> new SalableBookNotFoundException("Não foi possível encontrar livro para o 'title': '"+title+"'; 'authorId': '"+authorId+"'"));
		TitleAndAuthorUpdateDTO titleAndAuthorUpdateDTO = salableBookUpdateValidator
			.validateTitleAndAuthor(
				bookToUpdate.getTitle(), 
				dto.getTitle(), 
				bookToUpdate.getAuthor(), 
				dto.getAuthorId());
		salableBookUniquenessValidator.validateUniquenessOnUpdate(
			titleAndAuthorUpdateDTO, bookToUpdate.getTitle(), bookToUpdate.getAuthor().getId());
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
	public BigDecimal sellBook(Long bookId, int units){
		return bookSeller.sellBook(bookId, units);
	}
}