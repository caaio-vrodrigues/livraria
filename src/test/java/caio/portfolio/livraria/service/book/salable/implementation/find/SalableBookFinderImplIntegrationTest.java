package caio.portfolio.livraria.service.book.salable.implementation.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.model.enums.Genre;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class SalableBookFinderImplIntegrationTest {

	@Autowired private SalableBookFinderImpl salableBookFinderImpl;
	
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
	@Transactional
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve retornar livro após buscar por 'id'")
	void findById_returnsBook() {
		SalableBook book = salableBookFinderImpl.findById(O_ALQUIMISTA_ID);
		assertNotNull(book);
		assertEquals(
			PAULO_COELHO_FULL_NAME, 
			book.getAuthor().getFullName());
		assertEquals(
			O_ALQUIMISTA_TITLE, 
			book.getTitle());
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' após buscar por 'id'")
	void findById_throwsSalableBookNotFoundException() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookFinderImpl.findById(O_ALQUIMISTA_ID));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve retornar lista de livros após buscar por 'author'")
	void findByAuthor_returnsBookList() {
		List<SalableBook> listBook = salableBookFinderImpl
			.findByAuthor(PAULO_COELHO);
		assertEquals(
			BOOK_LIST.size(), 
			listBook.size());
		assertEquals(
			BOOK_LIST.getFirst().getId(), 
			listBook.getFirst().getId());
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' após buscar por 'author'")
	void findByAuthor_returnsEmptyBookList() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookFinderImpl.findByAuthor(PAULO_COELHO));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve retornar lista de livros após buscar por 'publisher'")
	void findByPublisher_returnsBookList() {
		List<SalableBook> listBook = salableBookFinderImpl
			.findByPublisher(ROCCO_PUBLISHER);
		assertEquals(
			BOOK_LIST.size(), 
			listBook.size());
		assertEquals(
			BOOK_LIST.getFirst().getId(), 
			listBook.getFirst().getId());
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' após buscar por 'publisher'")
	void findByPublisher_returnsEmptyBookList() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookFinderImpl.findByPublisher(ROCCO_PUBLISHER));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve retornar lista de livros após buscar por 'title'")
	void findByTitle_returnsBookList() {
		List<SalableBook> listBook = salableBookFinderImpl
			.findByTitle(O_ALQUIMISTA_TITLE);
		assertEquals(
			BOOK_LIST.size(), 
			listBook.size());
		assertEquals(
			BOOK_LIST.getFirst().getId(), 
			listBook.getFirst().getId());
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' após buscar por 'title'")
	void findByTitle_returnsEmptyBookList() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookFinderImpl.findByTitle(O_ALQUIMISTA_TITLE));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve retornar lista de livros após buscar por 'isbn'")
	void findByIsbn_returnsBookList() {
		List<SalableBook> listBook = salableBookFinderImpl
			.findByIsbn(O_ALQUIMISTA_ISBN);
		assertEquals(
			BOOK_LIST.size(), 
			listBook.size());
		assertEquals(
			BOOK_LIST.getFirst().getId(), 
			listBook.getFirst().getId());
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' após buscar por 'isbn'")
	void findByIsbn_returnsEmptyBookList() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookFinderImpl.findByIsbn(O_ALQUIMISTA_ISBN));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve retornar lista de livros após buscar por 'genre'")
	void findByGenre_returnsBookList() {
		List<SalableBook> listBook = salableBookFinderImpl
			.findByGenre(FICTION);
		assertEquals(
			BOOK_LIST.getFirst().getId(), 
			listBook.getFirst().getId());
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' após buscar por 'genre'")
	void findByGenre_returnsEmptyBookList() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookFinderImpl.findByGenre(FICTION));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve retornar livro após verificar por 'id'")
	void existsById_returnsBook() {
		assertTrue(salableBookFinderImpl.existsById(O_ALQUIMISTA_ID));
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' após verificar por 'id'")
	void existsById_throwsSalableBookNotFoundException() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookFinderImpl.existsById(O_ALQUIMISTA_ID));
	}
}