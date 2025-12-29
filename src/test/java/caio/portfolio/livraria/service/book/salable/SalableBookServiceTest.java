package caio.portfolio.livraria.service.book.salable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.exception.custom.book.salable.InsuficientSalableBookUnitsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.BookSellDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.BookSellListDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.CreateSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.ResponseSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.UpdateSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.repository.SalableBookRepository;
import caio.portfolio.livraria.model.enums.Genre;
import caio.portfolio.livraria.service.author.AuthorService;
import caio.portfolio.livraria.service.book.salable.dto.TitleAndAuthorUpdateDTO;
import caio.portfolio.livraria.service.book.salable.model.create.ResponseSalableBookDTOCreator;
import caio.portfolio.livraria.service.book.salable.model.save.SalableBookSaverAndConcurrencyHandle;
import caio.portfolio.livraria.service.book.salable.model.sell.BookSeller;
import caio.portfolio.livraria.service.book.salable.model.validate.SalableBookUniquenessValidator;
import caio.portfolio.livraria.service.book.salable.model.validate.SalableBookUpdateValidator;
import caio.portfolio.livraria.service.publisher.PublisherService;

@ExtendWith(MockitoExtension.class)
class SalableBookServiceTest {

	@InjectMocks private SalableBookService salableBookService;
	@Mock private AuthorService authorService;
	@Mock private PublisherService publisherService;
	@Mock private BookSeller bookSeller;
	@Mock private SalableBookUniquenessValidator salableBookUniquenessValidator;
	@Mock private SalableBookUpdateValidator salableBookUpdateValidator;
	@Mock private SalableBookRepository repo;
	@Mock private ResponseSalableBookDTOCreator responseSalableBookDTOCreator;
	@Mock private SalableBookSaverAndConcurrencyHandle salableBookSaverAndConcurrencyHandleImpl;
	
	private static final int O_ALQUIMISTA_UNITS = 50;
	private static final int SELL_UNITS = 2;
	private static final int NEW_UNITS = 30;
	private static final Long PAULO_COELHO_ID = 1L;
	private static final Long ROCCO_ID = 1L;
	private static final Long O_ALQUIMISTA_ID = 1L;
	private static final Long CAIO_ID = 2L;
	private static final Long GLOBAL_BOOKS_ID = 2L;
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	private static final String PAULO_COLEHO_ALIAS = "O Mago";
	private static final String PAULO_COELHO_FULL_NAME = "Paulo Coelho";
	private static final String O_ALQUIMISTA_TITLE = "O Alquimista";
	private static final String NEW_TITLE = "New Title";
	private static final String ROCCO_NAME = "Rocco";
	private static final String ROCCO_FULL_ADDRESS = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	private static final String O_ALQUIMISTA_ISBN = "abc123";
	private static final String NEW_ISBN = "newisbn345";
	private static final String CAIO_ALIAS = "CVR";
	private static final String CAIO_FULL_NAME = "Caio Vinicius Rodrigues";
	private static final String GLOBAL_BOOKS_NAME = "Global Books";
	private static final String USA_NAME = "Estados Unidos";
	private static final String USA_ISO_CODE = "US";
	private static final String GLOBAL_BOOKS_FULL_ADDRESS = "123 Main Street, New York, USA";
	private static final Integer BRAZIL_ID = 1;
	private static final Integer USA_ID = 2;
	private static final LocalDate CAIO_BIRTHDAY = LocalDate.of(1992, 03, 20);
	private static final LocalDate PAULO_COELHO_BIRTHDAY = LocalDate.of(1947, 8, 24);
	private static final BigDecimal O_ALQUIMISTA_PRICE = BigDecimal.valueOf(39.5);
	private static final BigDecimal NEW_PRICE = BigDecimal.valueOf(32.8);
	private static final BigDecimal TOTAL_TO_PAY = O_ALQUIMISTA_PRICE.multiply(BigDecimal.valueOf(2));
	
	private static final Country BRAZIL = Country.builder()
		.id(BRAZIL_ID)
		.isoAlpha2Code(BRAZIL_CODE)
		.name(BRAZIL_NAME)
		.build(); 
	
	private static final Country USA = Country.builder()
		.id(USA_ID)
		.name(USA_NAME)
		.isoAlpha2Code(USA_ISO_CODE)
		.build();
	
	private static final Author PAULO_COELHO = Author.builder()
		.id(PAULO_COELHO_ID)
		.alias(PAULO_COLEHO_ALIAS)
		.fullName(PAULO_COELHO_FULL_NAME)
		.birthday(PAULO_COELHO_BIRTHDAY)
		.country(BRAZIL)
		.build();
	
	private static final Author CAIO_VINICIUS_RODRIGUES = Author.builder()
		.id(CAIO_ID)
		.alias(CAIO_ALIAS)
		.fullName(CAIO_FULL_NAME)
		.birthday(CAIO_BIRTHDAY)
		.country(BRAZIL)
		.build();
	
	private static final Publisher ROCCO_PUBLISHER = Publisher.builder()
		.id(ROCCO_ID)
		.name(ROCCO_NAME)
		.country(BRAZIL)
		.fullAddress(ROCCO_FULL_ADDRESS)
		.build();
	
	private static final Publisher GLOBAL_BOOKS_PUBLISHER = Publisher.builder()
		.id(GLOBAL_BOOKS_ID)
		.name(GLOBAL_BOOKS_NAME)
		.fullAddress(GLOBAL_BOOKS_FULL_ADDRESS)
		.country(USA)
		.build();
	
	private static final SalableBook O_ALQUIMISTA = SalableBook.builder()
		.id(O_ALQUIMISTA_ID)
		.author(PAULO_COELHO)	
		.title(O_ALQUIMISTA_TITLE)
		.genre(Genre.FANTASY)
		.isbn(O_ALQUIMISTA_ISBN)
		.publisher(ROCCO_PUBLISHER)
		.price(O_ALQUIMISTA_PRICE)
		.units(O_ALQUIMISTA_UNITS)
		.build();
	
	private static final SalableBook UPDATED_BOOK = SalableBook.builder()
		.author(CAIO_VINICIUS_RODRIGUES)	
		.title(NEW_TITLE)
		.genre(Genre.ROMANCE)
		.isbn(NEW_ISBN)
		.publisher(GLOBAL_BOOKS_PUBLISHER)
		.price(NEW_PRICE)
		.units(NEW_UNITS)
		.build();
	
	private static final CreateSalableBookDTO O_ALQUIMISTA_CREATE_DTO = CreateSalableBookDTO
		.builder()
		.authorId(PAULO_COELHO.getId())	
		.title(O_ALQUIMISTA_TITLE)
		.genre(Genre.FANTASY)
		.isbn(O_ALQUIMISTA_ISBN)
		.publisherId(ROCCO_PUBLISHER.getId())
		.price(O_ALQUIMISTA_PRICE)
		.units(O_ALQUIMISTA_UNITS)
		.build();
	
	private static final ResponseSalableBookDTO O_ALQUIMISTA_RESPONSE_DTO = ResponseSalableBookDTO
		.builder()
		.authorId(PAULO_COELHO.getId())	
		.title(O_ALQUIMISTA_TITLE)
		.genre(Genre.FANTASY)
		.isbn(O_ALQUIMISTA_ISBN)
		.publisherId(ROCCO_PUBLISHER.getId())
		.price(O_ALQUIMISTA_PRICE)
		.units(O_ALQUIMISTA_UNITS)	
		.build();
	
	private static final ResponseSalableBookDTO UPDATED_RESPONSE_BOOK_DTO = ResponseSalableBookDTO
		.builder()
		.authorId(CAIO_VINICIUS_RODRIGUES.getId())		
		.title(NEW_TITLE)
		.genre(Genre.ROMANCE)
		.isbn(NEW_ISBN)
		.publisherId(GLOBAL_BOOKS_PUBLISHER.getId())
		.price(NEW_PRICE)
		.units(NEW_UNITS)
		.build();
	
	private static final TitleAndAuthorUpdateDTO UPDATED_TITLE_AND_AUTHOR = TitleAndAuthorUpdateDTO
		.builder()
		.title(NEW_TITLE)
		.author(CAIO_VINICIUS_RODRIGUES)
		.build();
	
	private static final UpdateSalableBookDTO UPDATE_SALABLE_BOOK_DTO = UpdateSalableBookDTO
		.builder()
		.authorId(CAIO_VINICIUS_RODRIGUES.getId())	
		.title(NEW_TITLE)
		.genre(Genre.ROMANCE)
		.isbn(NEW_ISBN)
		.publisherId(GLOBAL_BOOKS_PUBLISHER.getId())
		.price(NEW_PRICE)
		.units(NEW_UNITS)
		.build();
	
	private static final BookSellDTO BOOK_SELL_DTO = BookSellDTO.builder()
		.bookId(O_ALQUIMISTA_ID)	
		.units(SELL_UNITS)
		.build();
	
	private static final List<BookSellDTO> BOOK_SELL_DTO_LIST = List.of(BOOK_SELL_DTO);
	
	private static final BookSellListDTO BOOK_SELL_LIST_DTO = BookSellListDTO
		.builder()
		.sellList(BOOK_SELL_DTO_LIST)
		.build();
	
	private static final List<SalableBook> BOOK_LIST = List.of(O_ALQUIMISTA);
	
	@Test
	@DisplayName("Deve criar novo livro com sucesso e retornar 'ResponseSalableBookDTO'")
	void createSalableBook_returnsResponseSalableBookDTO() {
		when(authorService.getAuthorById(anyLong()))
			.thenReturn(PAULO_COELHO);
		when(publisherService.getPublisherById(anyLong()))
			.thenReturn(ROCCO_PUBLISHER);
		when(salableBookSaverAndConcurrencyHandleImpl
				.saveAndHandleConcurrency(any(SalableBook.class)))
			.thenReturn(O_ALQUIMISTA);
		when(responseSalableBookDTOCreator
				.toResponseSalableBookDTO(any(SalableBook.class)))
			.thenReturn(O_ALQUIMISTA_RESPONSE_DTO);
		ResponseSalableBookDTO bookRespDTO = salableBookService
			.createSalableBook(O_ALQUIMISTA_CREATE_DTO);
		assertNotNull(bookRespDTO);
		assertEquals(
			O_ALQUIMISTA_CREATE_DTO.getTitle(), 
			bookRespDTO.getTitle());
		assertEquals(
			O_ALQUIMISTA_CREATE_DTO.getAuthorId(), 
			bookRespDTO.getAuthorId());
		verify(authorService, times(1))
			.getAuthorById(anyLong());
		verify(publisherService, times(1))
			.getPublisherById(anyLong());
		verify(salableBookSaverAndConcurrencyHandleImpl, times(1))
			.saveAndHandleConcurrency(any(SalableBook.class));
		verify(responseSalableBookDTOCreator, times(1))
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'AuthorNotFoundException' ao tentar criar novo livro com autor inexistente")
	void createSalableBook_throwsAuthorNotFoundException() {
		when(authorService.getAuthorById(anyLong()))
			.thenThrow(AuthorNotFoundException.class);
		assertThrows(
			AuthorNotFoundException.class,
			() -> salableBookService.createSalableBook(O_ALQUIMISTA_CREATE_DTO));
		verify(authorService, times(1))
			.getAuthorById(anyLong());
	}
	
	@Test
	@DisplayName("Deve lançar 'PublisherNotFoundException' ao tentar criar novo livro com editora inexistente")
	void createSalableBook_throwsPublisherNotFoundException() {
		when(authorService.getAuthorById(anyLong()))
			.thenReturn(PAULO_COELHO);
		when(publisherService.getPublisherById(anyLong()))
			.thenThrow(PublisherNotFoundException.class);
		assertThrows(
			PublisherNotFoundException.class,
			() -> salableBookService.createSalableBook(O_ALQUIMISTA_CREATE_DTO));
		verify(authorService, times(1))
			.getAuthorById(anyLong());
		verify(publisherService, times(1))
			.getPublisherById(anyLong());
	}
	
	@Test
	@DisplayName("Deve retornar uma lista de livros disponiveis para venda convertidos em 'ResponseSalableBookDTO'")
	void getAllResponseSalableBookDTOs_returnsResponseSalableBookDTOList() {
		when(repo.findAll()).thenReturn(BOOK_LIST);
		when(responseSalableBookDTOCreator.toResponseSalableBookDTO(any(SalableBook.class)))
			.thenReturn(O_ALQUIMISTA_RESPONSE_DTO);
		List<ResponseSalableBookDTO> respBookDTOList = salableBookService
			.getAllResponseSalableBookDTOs();
		assertEquals(1, respBookDTOList.size());
		verify(repo, times(1)).findAll();
		verify(responseSalableBookDTOCreator, times(1))
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve retornar lista de livros à venda vazia")
	void getAllResponseSalableBookDTOs_returnsEmptyList() {
		when(repo.findAll()).thenReturn(List.of());
		List<ResponseSalableBookDTO> respBookDTOList = salableBookService
			.getAllResponseSalableBookDTOs();
		assertEquals(0, respBookDTOList.size());
		verify(repo, times(1)).findAll();
		verify(responseSalableBookDTOCreator, never())
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve retornar 'ResponseSalableBookDTO' ao buscar livro à venda por 'id'")
	void getResponseSalableBookDTOById_returnsResponseSalableBookDTO() {
		when(repo.findById(anyLong()))
			.thenReturn(Optional.of(O_ALQUIMISTA));
		when(responseSalableBookDTOCreator
				.toResponseSalableBookDTO(any(SalableBook.class)))
			.thenReturn(O_ALQUIMISTA_RESPONSE_DTO);
		ResponseSalableBookDTO bookDTO = salableBookService
			.getResponseSalableBookDTOById(PAULO_COELHO_ID);
		assertNotNull(bookDTO);
		verify(repo, times(1)).findById(anyLong());
		verify(responseSalableBookDTOCreator, times(1))
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao buscar livro inexistente por 'id'")
	void getResponseSalableBookDTOById_throwsSalableBookNotFoundException() {
		when(repo.findById(anyLong()))
			.thenThrow(SalableBookNotFoundException.class);
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookService.getResponseSalableBookDTOById(PAULO_COELHO_ID));
		verify(repo, times(1)).findById(anyLong());
		verify(responseSalableBookDTOCreator, never())
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve retornar lista de 'ResponseSalableBookDTO' ao buscar livros à venda por autor")
	void getResponseSalableBookDTOByAuthorId_returnsResponseSalableBookDTO() {
		when(authorService.getAuthorById(anyLong()))
			.thenReturn(PAULO_COELHO);
		when(repo.findByAuthor(any(Author.class)))
			.thenReturn(BOOK_LIST);
		when(responseSalableBookDTOCreator
				.toResponseSalableBookDTO(any(SalableBook.class)))
			.thenReturn(O_ALQUIMISTA_RESPONSE_DTO);
		List<ResponseSalableBookDTO> respBookDTOList = salableBookService
			.getResponseSalableBookDTOByAuthorId(PAULO_COELHO_ID);
		assertEquals(1, respBookDTOList.size());
		verify(authorService, times(1)).getAuthorById(anyLong());
		verify(repo, times(1)).findByAuthor(any(Author.class));
		verify(responseSalableBookDTOCreator, times(1))
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao buscar livros à venda por autor")
	void getAllResponseSalableBookDTOs_returnsEmptyListByAuthor() {
		when(authorService.getAuthorById(anyLong()))
			.thenReturn(PAULO_COELHO);
		when(repo.findByAuthor(any(Author.class)))
			.thenThrow(SalableBookNotFoundException.class);
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookService
				.getResponseSalableBookDTOByAuthorId(PAULO_COELHO_ID));
		verify(authorService).getAuthorById(anyLong());
		verify(repo, times(1)).findByAuthor(any(Author.class));
		verify(responseSalableBookDTOCreator, never())
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve retornar lista de livros à venda ao buscar por editora")
	void getResponseSalableBookDTOByPublisherId_returnsResponseSalableBookDTOList() {
		when(publisherService.getPublisherById(anyLong()))
			.thenReturn(ROCCO_PUBLISHER);
		when(repo.findByPublisher(any(Publisher.class)))
			.thenReturn(BOOK_LIST);
		when(responseSalableBookDTOCreator
				.toResponseSalableBookDTO(any(SalableBook.class)))
			.thenReturn(O_ALQUIMISTA_RESPONSE_DTO);
		List<ResponseSalableBookDTO> respBookDTOList = salableBookService
			.getResponseSalableBookDTOByPublisherId(PAULO_COELHO_ID);
		assertEquals(1, respBookDTOList.size());
		verify(publisherService, times(1)).getPublisherById(anyLong());
		verify(repo, times(1)).findByPublisher(any(Publisher.class));
		verify(responseSalableBookDTOCreator, times(1))
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao buscar livros à venda por editora")
	void getAllResponseSalableBookDTOs_throwsSalableBookNotFoundException() {
		when(publisherService.getPublisherById(anyLong()))
			.thenReturn(ROCCO_PUBLISHER);
		when(repo.findByPublisher(any(Publisher.class)))
			.thenThrow(SalableBookNotFoundException.class);
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookService
				.getResponseSalableBookDTOByPublisherId(PAULO_COELHO_ID));
		verify(publisherService).getPublisherById(anyLong());
		verify(repo, times(1)).findByPublisher(ROCCO_PUBLISHER);
		verify(responseSalableBookDTOCreator, never())
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve retornar lista de livros à venda ao buscar por título")
	void getResponseSalableBookDTOByTitle_returnsResponseSalableBookDTOList() {
		when(repo.findByTitle(anyString())).thenReturn(BOOK_LIST);
		when(responseSalableBookDTOCreator
				.toResponseSalableBookDTO(any(SalableBook.class)))
			.thenReturn(O_ALQUIMISTA_RESPONSE_DTO);
		List<ResponseSalableBookDTO> respBookDTOList = salableBookService
			.getResponseSalableBookDTOByTitle(O_ALQUIMISTA_TITLE);
		assertEquals(1, respBookDTOList.size());
		verify(repo, times(1)).findByTitle(anyString());
		verify(responseSalableBookDTOCreator, times(1))
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao buscar livros à venda por título")
	void getResponseSalableBookDTOByTitle_throwsSalableBookNotFoundException() {
		when(repo.findByTitle(anyString())).thenReturn(List.of());
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookService
				.getResponseSalableBookDTOByTitle(O_ALQUIMISTA_TITLE));
		verify(repo, times(1)).findByTitle(anyString());
		verify(responseSalableBookDTOCreator, never())
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve retornar lista de livros à venda ao buscar por gênero")
	void getResponseSalableBookDTOByGenre_returnsResponseSalableBookDTOList() {
		when(repo.findByGenre(any(Genre.class))).thenReturn(BOOK_LIST);
		when(responseSalableBookDTOCreator
				.toResponseSalableBookDTO(any(SalableBook.class)))
			.thenReturn(O_ALQUIMISTA_RESPONSE_DTO);
		List<ResponseSalableBookDTO> respBookDTOList = salableBookService
			.getResponseSalableBookDTOByGenre(Genre.FICTION);
		assertEquals(1, respBookDTOList.size());
		verify(repo, times(1)).findByGenre(any(Genre.class));
		verify(responseSalableBookDTOCreator, times(1))
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao buscar livros à venda por gênero")
	void getResponseSalableBookDTOByGenre_throwsSalableBookNotFoundException() {
		when(repo.findByGenre(any(Genre.class)))
			.thenThrow(SalableBookNotFoundException.class);
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookService
				.getResponseSalableBookDTOByGenre(Genre.FICTION));
		verify(repo, times(1)).findByGenre(any(Genre.class));
		verify(responseSalableBookDTOCreator, never())
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve retornar lista de livros à venda ao buscar por ISBN")
	void getResponseSalableBookDTOByIsbn_returnsResponseSalableBookDTOList() {
		when(repo.findByIsbn(anyString())).thenReturn(BOOK_LIST);
		when(responseSalableBookDTOCreator
				.toResponseSalableBookDTO(any(SalableBook.class)))
			.thenReturn(O_ALQUIMISTA_RESPONSE_DTO);
		List<ResponseSalableBookDTO> respBookDTOList = salableBookService
			.getResponseSalableBookDTOByIsbn(O_ALQUIMISTA_ISBN);
		assertEquals(1, respBookDTOList.size());
		verify(repo, times(1)).findByIsbn(anyString());
		verify(responseSalableBookDTOCreator, times(1))
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao buscar livros à venda por ISBN")
	void getResponseSalableBookDTOByIsbn_throwsSalableBookNotFoundException() {
		when(repo.findByIsbn(anyString()))
			.thenThrow(SalableBookNotFoundException.class);
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookService
				.getResponseSalableBookDTOByIsbn(O_ALQUIMISTA_ISBN));
		verify(repo, times(1)).findByIsbn(anyString());
		verify(responseSalableBookDTOCreator, never())
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve atualizar livro com novos valores e retornar 'ResponseSalableBookDTO'")
	void updateSalableBookById_returnsResponseSalableBookDTO() {
		when(repo.findById(anyLong()))
			.thenReturn(Optional.of(O_ALQUIMISTA));
		when(salableBookUpdateValidator.validateTitleAndAuthor(
				anyString() ,anyString(), any(Author.class), anyLong()))
			.thenReturn(UPDATED_TITLE_AND_AUTHOR);
		when(salableBookUpdateValidator.validateGenre(
				any(Genre.class), any(Genre.class)))
			.thenReturn(Genre.ROMANCE);
		when(salableBookUpdateValidator.validatePublisher(
				any(Publisher.class), anyLong()))
			.thenReturn(GLOBAL_BOOKS_PUBLISHER);
		when(salableBookUpdateValidator
				.validateIsbn(anyString(), anyString()))
			.thenReturn(NEW_ISBN);
		when(salableBookUpdateValidator
				.validatePrice(any(BigDecimal.class), any(BigDecimal.class)))
			.thenReturn(NEW_PRICE);
		when(salableBookUpdateValidator
				.validateUnits(anyInt(), anyInt()))
			.thenReturn(NEW_UNITS);
		when(salableBookSaverAndConcurrencyHandleImpl
				.saveAndHandleConcurrency(any(SalableBook.class)))
			.thenReturn(UPDATED_BOOK);
		when(responseSalableBookDTOCreator
				.toResponseSalableBookDTO(any(SalableBook.class)))
			.thenReturn(UPDATED_RESPONSE_BOOK_DTO);
		ResponseSalableBookDTO responseSalableBookDTO = salableBookService
			.updateSalableBookById(O_ALQUIMISTA_ID, UPDATE_SALABLE_BOOK_DTO);
		assertNotNull(responseSalableBookDTO);
		assertEquals(NEW_TITLE, responseSalableBookDTO.getTitle());
		assertEquals(NEW_ISBN, responseSalableBookDTO.getIsbn());
		assertEquals(NEW_UNITS, responseSalableBookDTO.getUnits());
		assertEquals(NEW_PRICE, responseSalableBookDTO.getPrice());
		assertEquals(CAIO_VINICIUS_RODRIGUES.getId(), responseSalableBookDTO.getAuthorId());
		assertEquals(GLOBAL_BOOKS_ID, responseSalableBookDTO.getPublisherId());
		verify(repo, times(1)).findById(anyLong());
		verify(salableBookUpdateValidator, times(1))
			.validateTitleAndAuthor(
				anyString() ,anyString(), any(Author.class), anyLong());
		verify(salableBookUpdateValidator, times(1))
			.validateGenre(any(Genre.class), any(Genre.class));
		verify(salableBookUpdateValidator, times(1))
			.validatePublisher(any(Publisher.class), anyLong());
		verify(salableBookUpdateValidator, times(1))
			.validateIsbn(anyString(), anyString());
		verify(salableBookUpdateValidator, times(1))
			.validatePrice(any(BigDecimal.class), any(BigDecimal.class));
		verify(salableBookUpdateValidator, times(1))
			.validateUnits(anyInt(), anyInt());
		verify(salableBookSaverAndConcurrencyHandleImpl, times(1))
			.saveAndHandleConcurrency(any(SalableBook.class));
		verify(responseSalableBookDTOCreator, times(1))
			.toResponseSalableBookDTO(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve realizar venda de múltiplos livros e retornar total à pagar")
	void sellBooks_returnsBigdecimal() {
		when(bookSeller.sellBooks(any(BookSellListDTO.class)))
			.thenReturn(TOTAL_TO_PAY);
		BigDecimal totalToPay = salableBookService.sellBooks(BOOK_SELL_LIST_DTO);
		assertNotNull(totalToPay);
		assertEquals(TOTAL_TO_PAY, totalToPay);
	}
	
	@Test
	@DisplayName("Deve propagar corretamente 'InsuficientSalableBookUnitsException' ao tentar venda de múltiplos livros com unidades insuficientes")
	void sellBooks_throwsInsuficientSalableBookUnitsException() {
		when(bookSeller.sellBooks(any(BookSellListDTO.class)))
			.thenThrow(InsuficientSalableBookUnitsException.class);
		assertThrows(
			InsuficientSalableBookUnitsException.class,
			() -> salableBookService.sellBooks(BOOK_SELL_LIST_DTO));
	}
}