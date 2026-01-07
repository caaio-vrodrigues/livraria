package caio.portfolio.livraria.service.book.salable.implementation.find;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.repository.SalableBookRepository;
import caio.portfolio.livraria.model.enums.Genre;

@ExtendWith(MockitoExtension.class)
class SalableBookFinderImplTest {

	@InjectMocks private SalableBookFinderImpl salableBookFinderImpl;
	@Mock private SalableBookRepository repo;
	@Mock private  MessageSource salableBookMessageSource;

	private static final Genre FICTION = Genre.FICTION;
	private static final int O_ALQUIMISTA_UNITS = 100;
	private static final Long PAULO_COELHO_ID = 1L;
	private static final Long ROCCO_ID = 1L;
	private static final Long O_ALQUIMISTA_ID = 1L;
	private static final String O_ALQUIMISTA_ISBN = "978-85-325-1010-0";
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	private static final String PAULO_COELHO_FULL_NAME = "Paulo Coelho";
	private static final String O_ALQUIMISTA_TITLE = "O Alquimista";
	private static final String PAULO_COLEHO_ALIAS = "O Mago";
	private static final String ROCCO_NAME = "Rocco";
	private static final String ROCCO_FULL_ADDRESS = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	private static final String BOOK_NOT_FOUND_ID_MSG = "Não foi possível encontrar livro com `id`: `"+O_ALQUIMISTA_ID+"`";
	private static final Integer BRAZIL_ID = 1;
	private static final LocalDate PAULO_COELHO_BIRTHDAY = LocalDate.of(1947, 8, 24);
	private static final BigDecimal O_ALQUIMISTA_PRICE = BigDecimal.valueOf(35.50);
	
	private static final Country BRAZIL = Country.builder()
		.id(BRAZIL_ID)
		.isoAlpha2Code(BRAZIL_CODE)
		.name(BRAZIL_NAME)
		.build(); 
	
	private static final Author PAULO_COELHO = Author.builder()
		.id(PAULO_COELHO_ID)
		.alias(PAULO_COLEHO_ALIAS)
		.fullName(PAULO_COELHO_FULL_NAME)
		.birthday(PAULO_COELHO_BIRTHDAY)
		.country(BRAZIL)
		.build();
	
	private static final Publisher ROCCO_PUBLISHER = Publisher.builder()
		.id(ROCCO_ID)
		.name(ROCCO_NAME)
		.country(BRAZIL)
		.fullAddress(ROCCO_FULL_ADDRESS)
		.build();
	
	private static final SalableBook O_ALQUIMISTA = SalableBook.builder()
		.id(O_ALQUIMISTA_ID)
		.author(PAULO_COELHO)	
		.title(O_ALQUIMISTA_TITLE)
		.genre(Genre.FICTION)
		.isbn(O_ALQUIMISTA_ISBN)
		.publisher(ROCCO_PUBLISHER)
		.price(O_ALQUIMISTA_PRICE)
		.units(O_ALQUIMISTA_UNITS)
		.build();
	
	private static final List<SalableBook> BOOK_LIST = List.of(O_ALQUIMISTA);
	
	@Test
	@DisplayName("Deve retornar livro após buscar por 'id'")
	void findById_returnsBook() {
		when(repo.findById(anyLong()))
			.thenReturn(Optional.of(O_ALQUIMISTA));
		SalableBook book = salableBookFinderImpl.findById(O_ALQUIMISTA_ID);
		assertNotNull(book);
		assertEquals(
			PAULO_COELHO_FULL_NAME, 
			book.getAuthor().getFullName());
		assertEquals(
			O_ALQUIMISTA_TITLE, 
			book.getTitle());
		verify(repo, times(1))
			.findById(anyLong());
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' após buscar por 'id'")
	void findById_throwsSalableBookNotFoundException() {
		when(repo.findById(anyLong()))
			.thenReturn(Optional.empty());
		when(salableBookMessageSource.getMessage(
				anyString(), 
				any(Object[].class), 
				any(Locale.class)))
			.thenReturn(BOOK_NOT_FOUND_ID_MSG);
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookFinderImpl.findById(O_ALQUIMISTA_ID));
		verify(repo, times(1))
			.findById(anyLong());
		verify(salableBookMessageSource, times(1))
			.getMessage(
				anyString(), 
				any(Object[].class), 
				any(Locale.class));
	}
	
	@Test
	@DisplayName("Deve retornar lista de livros após buscar por 'author'")
	void findByAuthor_returnsBookList() {
		when(repo.findByAuthor(any(Author.class)))
			.thenReturn(BOOK_LIST);
		List<SalableBook> listBook = salableBookFinderImpl
			.findByAuthor(PAULO_COELHO);
		assertEquals(
			BOOK_LIST.size(), 
			listBook.size());
		assertEquals(
			BOOK_LIST.getFirst().getId(), 
			listBook.getFirst().getId());
		verify(repo, times(1))
			.findByAuthor(any(Author.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' após buscar por 'author'")
	void findByAuthor_returnsEmptyBookList() {
		when(repo.findByAuthor(any(Author.class)))
			.thenReturn(List.of());
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookFinderImpl.findByAuthor(PAULO_COELHO));
		verify(repo, times(1))
			.findByAuthor(any(Author.class));
	}
	
	@Test
	@DisplayName("Deve retornar lista de livros após buscar por 'publisher'")
	void findByPublisher_returnsBookList() {
		when(repo.findByPublisher(any(Publisher.class)))
			.thenReturn(BOOK_LIST);
		List<SalableBook> listBook = salableBookFinderImpl
			.findByPublisher(ROCCO_PUBLISHER);
		assertEquals(
			BOOK_LIST.size(), 
			listBook.size());
		assertEquals(
			BOOK_LIST.getFirst().getId(), 
			listBook.getFirst().getId());
		verify(repo, times(1))
			.findByPublisher(any(Publisher.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' após buscar por 'publisher'")
	void findByPublisher_returnsEmptyBookList() {
		when(repo.findByPublisher(any(Publisher.class)))
			.thenReturn(List.of());
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookFinderImpl.findByPublisher(ROCCO_PUBLISHER));
		verify(repo, times(1))
			.findByPublisher(any(Publisher.class));
	}
	
	@Test
	@DisplayName("Deve retornar lista de livros após buscar por 'title'")
	void findByTitle_returnsBookList() {
		when(repo.findByTitle(anyString()))
			.thenReturn(BOOK_LIST);
		List<SalableBook> listBook = salableBookFinderImpl
			.findByTitle(O_ALQUIMISTA_TITLE);
		assertEquals(
			BOOK_LIST.size(), 
			listBook.size());
		assertEquals(
			BOOK_LIST.getFirst().getId(), 
			listBook.getFirst().getId());
		verify(repo, times(1))
			.findByTitle(anyString());
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' após buscar por 'title'")
	void findByTitle_returnsEmptyBookList() {
		when(repo.findByTitle(anyString()))
			.thenReturn(List.of());
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookFinderImpl.findByTitle(O_ALQUIMISTA_TITLE));
		verify(repo, times(1))
			.findByTitle(anyString());
	}
	
	@Test
	@DisplayName("Deve retornar lista de livros após buscar por 'isbn'")
	void findByIsbn_returnsBookList() {
		when(repo.findByIsbn(anyString()))
			.thenReturn(BOOK_LIST);
		List<SalableBook> listBook = salableBookFinderImpl
			.findByIsbn(O_ALQUIMISTA_ISBN);
		assertEquals(
			BOOK_LIST.size(), 
			listBook.size());
		assertEquals(
			BOOK_LIST.getFirst().getId(), 
			listBook.getFirst().getId());
		verify(repo, times(1))
			.findByIsbn(anyString());
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' após buscar por 'isbn'")
	void findByIsbn_returnsEmptyBookList() {
		when(repo.findByIsbn(anyString()))
			.thenReturn(List.of());
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookFinderImpl.findByIsbn(O_ALQUIMISTA_ISBN));
		verify(repo, times(1))
			.findByIsbn(anyString());
	}
	
	@Test
	@DisplayName("Deve retornar lista de livros após buscar por 'genre'")
	void findByGenre_returnsBookList() {
		when(repo.findByGenre(any(Genre.class)))
			.thenReturn(BOOK_LIST);
		List<SalableBook> listBook = salableBookFinderImpl
			.findByGenre(FICTION);
		assertEquals(
			BOOK_LIST.size(), 
			listBook.size());
		assertEquals(
			BOOK_LIST.getFirst().getId(), 
			listBook.getFirst().getId());
		verify(repo, times(1))
			.findByGenre(any(Genre.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' após buscar por 'genre'")
	void findByGenre_returnsEmptyBookList() {
		when(repo.findByGenre(any(Genre.class)))
			.thenReturn(List.of());
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookFinderImpl.findByGenre(FICTION));
		verify(repo, times(1))
			.findByGenre(any(Genre.class));
	}
	
	@Test
	@DisplayName("Deve retornar livro após verificar por 'id'")
	void existsById_returnsBook() {
		when(repo.existsById(anyLong()))
			.thenReturn(true);
		assertTrue(salableBookFinderImpl.existsById(O_ALQUIMISTA_ID));
		verify(repo, times(1))
			.existsById(anyLong());
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' após verificar por 'id'")
	void existsById_throwsSalableBookNotFoundException() {
		when(repo.existsById(anyLong()))
			.thenReturn(false);
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookFinderImpl.existsById(O_ALQUIMISTA_ID));
		verify(repo, times(1))
			.existsById(anyLong());
	}
}