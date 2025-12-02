package caio.portfolio.livraria.service.book.salable;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.exception.custom.book.salable.ConcurrentSalableBookException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.CreateSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.ResponseSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.repository.SalableBookRepository;
import caio.portfolio.livraria.service.author.AuthorService;
import caio.portfolio.livraria.service.book.salable.model.ResponseSalableBookDTOCreator;
import caio.portfolio.livraria.service.publisher.PublisherService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalableBookService {

	private final SalableBookRepository repo;
	private final ResponseSalableBookDTOCreator responseSalableBookDTOCreator;
	private final AuthorService authorService;
	private final PublisherService publisherService;
	
	private SalableBook saveAndHandleConcurrency(SalableBook book) {
		try {
			return repo.saveAndFlush(book);
		}
		catch(DataIntegrityViolationException e) {
			Optional<SalableBook> salableBookOptional = repo
				.findByTitleAndAuthorAndPublisher(
					book.getTitle(), 
					book.getAuthor(), 
					book.getPublisher());
			if(salableBookOptional.isPresent()) throw new SalableBookAlreadyExistsException("Não foi possível realizar a operação. Livro: '"+salableBookOptional.get().getTitle()+"' já existe");
			throw new ConcurrentSalableBookException("Não foi possível criar livro: '"+book.getTitle()+"' por falha de concorrência. Verifique se o livro já existe ou tente novamente se necessário");
		}
	}

	@Transactional
	public ResponseSalableBookDTO createSalableBook(CreateSalableBookDTO dto) {
		Author author = authorService.getAuthorById(dto.getAuthorId());
		Publisher publisher = publisherService.getPublisherById(dto.getPublisherId());
		Optional<SalableBook> salableBookOptional = repo.findByTitleAndAuthorAndPublisher(
			dto.getTitle(), 
			author, 
			publisher);
		if(salableBookOptional.isPresent()) throw new SalableBookAlreadyExistsException("Não foi possível realizar a operação. Livro: '"+salableBookOptional.get().getTitle()+"' já existe");
		SalableBook newBook = SalableBook.builder()
			.title(dto.getTitle())
			.genre(dto.getGenre())
			.author(author)
			.publisher(publisher)
			.isbn(dto.getIsbn())
			.price(dto.getPrice())
			.units(dto.getUnits())
			.build();
		newBook = saveAndHandleConcurrency(newBook);
		return responseSalableBookDTOCreator.toResponseSalableBookDTO(newBook);
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
			.orElseThrow(() -> new SalableBookNotFoundException("Não foi possível encontra livro com 'id': '"+id+"'")));
	}
}