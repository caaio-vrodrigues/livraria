package caio.portfolio.livraria.service.book.salable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.exception.custom.book.salable.InsuficientSalableBookUnitsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.BookSellDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.BookSellListDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.CreateSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.ResponseSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.UpdateSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.model.enums.Genre;

@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace=Replace.ANY)
class SalableBookServiceIntegrationTest {

	@Autowired private SalableBookService salableBookService;
	
	private static final int O_ALQUIMISTA_UNITS = 50;
	private static final int NEW_UNITS = 30;
	private static final int SELL_UNITS = 2;
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
	private static final String O_ALQUIMISTA_ISBN = "978-85-325-1010-0";
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
	private static final BigDecimal O_ALQUIMISTA_PRICE = BigDecimal.valueOf(35.5);
	private static final BigDecimal NEW_PRICE = BigDecimal.valueOf(32.8);
	
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
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve criar novo livro com sucesso e retornar 'ResponseSalableBookDTO'")
	void createSalableBook_returnsResponseSalableBookDTO() {
		ResponseSalableBookDTO bookRespDTO = salableBookService
			.createSalableBook(O_ALQUIMISTA_CREATE_DTO);
		assertNotNull(bookRespDTO);
		assertEquals(
			O_ALQUIMISTA_CREATE_DTO.getTitle(), 
			bookRespDTO.getTitle());
		assertEquals(
			O_ALQUIMISTA_CREATE_DTO.getAuthorId(), 
			bookRespDTO.getAuthorId());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@DisplayName("Deve lançar 'AuthorNotFoundException' ao tentar criar novo livro com autor inexistente")
	void createSalableBook_throwsAuthorNotFoundException() {
		assertThrows(
			AuthorNotFoundException.class,
			() -> salableBookService.createSalableBook(O_ALQUIMISTA_CREATE_DTO));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve lançar 'PublisherNotFoundException' ao tentar criar novo livro com editora inexistente")
	void createSalableBook_throwsPublisherNotFoundException() {
		assertThrows(
			PublisherNotFoundException.class,
			() -> salableBookService.createSalableBook(O_ALQUIMISTA_CREATE_DTO));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve retornar uma lista de livros disponiveis para venda convertidos em 'ResponseSalableBookDTO'")
	void getAllResponseSalableBookDTOs_returnsResponseSalableBookDTOList() {
		List<ResponseSalableBookDTO> respBookDTOList = salableBookService
			.getAllResponseSalableBookDTOs();
		assertEquals(2, respBookDTOList.size());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve retornar lista de livros à venda vazia")
	void getAllResponseSalableBookDTOs_returnsEmptyList() {
		List<ResponseSalableBookDTO> respBookDTOList = salableBookService
			.getAllResponseSalableBookDTOs();
		assertEquals(0, respBookDTOList.size());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve retornar 'ResponseSalableBookDTO' ao buscar livro à venda por 'id'")
	void getResponseSalableBookDTOById_returnsResponseSalableBookDTO() {
		ResponseSalableBookDTO bookDTO = salableBookService
			.getResponseSalableBookDTOById(PAULO_COELHO_ID);
		assertNotNull(bookDTO);
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao buscar livro inexistente por 'id'")
	void getResponseSalableBookDTOById_throwsSalableBookNotFoundException() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookService.getResponseSalableBookDTOById(PAULO_COELHO_ID));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve retornar lista de 'ResponseSalableBookDTO' ao buscar livros à venda por autor")
	void getResponseSalableBookDTOByAuthorId_returnsResponseSalableBookDTO() {
		List<ResponseSalableBookDTO> respBookDTOList = salableBookService
			.getResponseSalableBookDTOByAuthorId(PAULO_COELHO_ID);
		assertEquals(1, respBookDTOList.size());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao buscar livros à venda por autor")
	void getAllResponseSalableBookDTOs_returnsEmptyListByAuthor() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookService
				.getResponseSalableBookDTOByAuthorId(PAULO_COELHO_ID));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve retornar lista de livros à venda ao buscar por editora")
	void getResponseSalableBookDTOByPublisherId_returnsResponseSalableBookDTOList() {
		List<ResponseSalableBookDTO> respBookDTOList = salableBookService
			.getResponseSalableBookDTOByPublisherId(PAULO_COELHO_ID);
		assertEquals(1, respBookDTOList.size());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao buscar livros à venda por editora")
	void getAllResponseSalableBookDTOs_throwsSalableBookNotFoundException() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookService
				.getResponseSalableBookDTOByPublisherId(PAULO_COELHO_ID));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve retornar lista de livros à venda ao buscar por título")
	void getResponseSalableBookDTOByTitle_returnsResponseSalableBookDTOList() {
		List<ResponseSalableBookDTO> respBookDTOList = salableBookService
			.getResponseSalableBookDTOByTitle(O_ALQUIMISTA_TITLE);
		assertEquals(1, respBookDTOList.size());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao buscar livros à venda por título")
	void getResponseSalableBookDTOByTitle_throwsSalableBookNotFoundException() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookService
				.getResponseSalableBookDTOByTitle(O_ALQUIMISTA_TITLE));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve retornar lista de livros à venda ao buscar por gênero")
	void getResponseSalableBookDTOByGenre_returnsResponseSalableBookDTOList() {
		List<ResponseSalableBookDTO> respBookDTOList = salableBookService
			.getResponseSalableBookDTOByGenre(Genre.FICTION);
		assertEquals(2, respBookDTOList.size());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao buscar livros à venda por gênero")
	void getResponseSalableBookDTOByGenre_throwsSalableBookNotFoundException() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookService
				.getResponseSalableBookDTOByGenre(Genre.FICTION));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve retornar lista de livros à venda ao buscar por ISBN")
	void getResponseSalableBookDTOByIsbn_returnsResponseSalableBookDTOList() {
		List<ResponseSalableBookDTO> respBookDTOList = salableBookService
			.getResponseSalableBookDTOByIsbn(O_ALQUIMISTA_ISBN);
		assertEquals(1, respBookDTOList.size());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao buscar livros à venda por ISBN")
	void getResponseSalableBookDTOByIsbn_throwsSalableBookNotFoundException() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> salableBookService
				.getResponseSalableBookDTOByIsbn(O_ALQUIMISTA_ISBN));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve atualizar livro com novos valores e retornar 'ResponseSalableBookDTO'")
	void updateSalableBookById_returnsResponseSalableBookDTO() {
		ResponseSalableBookDTO responseSalableBookDTO = salableBookService
			.updateSalableBookById(O_ALQUIMISTA_ID, UPDATE_SALABLE_BOOK_DTO);
		assertNotNull(responseSalableBookDTO);
		assertEquals(NEW_TITLE, responseSalableBookDTO.getTitle());
		assertEquals(NEW_ISBN, responseSalableBookDTO.getIsbn());
		assertEquals(NEW_UNITS, responseSalableBookDTO.getUnits());
		assertEquals(NEW_PRICE, responseSalableBookDTO.getPrice());
		assertEquals(CAIO_VINICIUS_RODRIGUES.getId(), responseSalableBookDTO.getAuthorId());
		assertEquals(GLOBAL_BOOKS_ID, responseSalableBookDTO.getPublisherId());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve realizar venda de múltiplos livros e retornar total à pagar")
	void sellBooks_returnsBigdecimal() {
		BigDecimal totalToPay = salableBookService
			.sellBooks(BOOK_SELL_LIST_DTO);
		assertNotNull(totalToPay);
		assertEquals(
			O_ALQUIMISTA_PRICE.multiply(
				BigDecimal.valueOf(SELL_UNITS)).floatValue(), 
			totalToPay.floatValue());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list_zero_units.sql")
	@DisplayName("Deve propagar corretamente 'InsuficientSalableBookUnitsException' ao tentar venda de múltiplos livros com unidades insuficientes")
	void sellBooks_throwsInsuficientSalableBookUnitsException() {
		assertThrows(
			InsuficientSalableBookUnitsException.class,
			() -> salableBookService.sellBooks(BOOK_SELL_LIST_DTO));
	}
}