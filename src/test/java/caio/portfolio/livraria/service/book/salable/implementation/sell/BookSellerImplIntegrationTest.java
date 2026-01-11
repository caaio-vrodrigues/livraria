package caio.portfolio.livraria.service.book.salable.implementation.sell;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import caio.portfolio.livraria.exception.custom.book.salable.InsuficientSalableBookUnitsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.BookSellDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.BookSellListDTO;

@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace=Replace.ANY)
class BookSellerImplIntegrationTest {

	@Autowired private BookSellerImpl bookSellerImpl;
	
	private static final int O_ALQUIMISTA_SELL_UNITS = 10;
	private static final int DOM_CASMURRO_SELL_UNITS = 10;
	private static final Long O_ALQUIMISTA_ID = 1L;
	private static final Long DOM_CASMURRO_ID = 2L;
	private static final BigDecimal O_ALQUIMISTA_PRICE = BigDecimal.valueOf(35.50);
	private static final BigDecimal DOM_CASMURRO_PRICE = BigDecimal.valueOf(28.99);
	
	private static final BookSellDTO O_ALQUIMISTA_SELL_DTO = BookSellDTO.builder()
		.bookId(O_ALQUIMISTA_ID)	
		.units(O_ALQUIMISTA_SELL_UNITS)
		.build();
	
	private static final BookSellDTO DOM_CASMURRO_SELL_DTO = BookSellDTO.builder()
		.bookId(DOM_CASMURRO_ID)	
		.units(DOM_CASMURRO_SELL_UNITS)
		.build();

	private static final List<BookSellDTO> BOOK_SELL_DTO_LIST_SINGLE_BOOK = List.of(
		O_ALQUIMISTA_SELL_DTO);
	
	private static final List<BookSellDTO> BOOK_SELL_DTO_LIST_MANY_BOOKS = List.of(
		O_ALQUIMISTA_SELL_DTO, DOM_CASMURRO_SELL_DTO);
	
	private BookSellListDTO bookSellListDTOSingleBook;
	private BookSellListDTO bookSellListDTOManyBooks;
	
	@BeforeEach
	void setup() {
		bookSellListDTOSingleBook = BookSellListDTO
			.builder()
			.sellList(BOOK_SELL_DTO_LIST_SINGLE_BOOK)
			.build();
		
		bookSellListDTOManyBooks = BookSellListDTO
			.builder()
			.sellList(BOOK_SELL_DTO_LIST_MANY_BOOKS)
			.build();
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve realizar venda de livro com sucesso e retornar total à pagar")
	void sellBooks_returnsBigDecimal() {
		BigDecimal sell = bookSellerImpl.sellBooks(bookSellListDTOSingleBook);
		assertNotNull(sell);
		assertEquals(
			O_ALQUIMISTA_PRICE
				.multiply(BigDecimal.valueOf(O_ALQUIMISTA_SELL_UNITS))
				.floatValue(), 
			sell.floatValue());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve realizar venda de múltiplos livros com sucesso e retornar total à pagar")
	void sellBooks_manyBooks_returnsBigDecimal() {
		BigDecimal sell = bookSellerImpl.sellBooks(bookSellListDTOManyBooks);
		assertNotNull(sell);
		assertEquals(
			O_ALQUIMISTA_PRICE
				.multiply(BigDecimal.valueOf(O_ALQUIMISTA_SELL_UNITS))
				.add(DOM_CASMURRO_PRICE
					.multiply(BigDecimal.valueOf(DOM_CASMURRO_SELL_UNITS)))
				.floatValue(),
			sell.floatValue());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list_zero_units.sql")
	@DisplayName("Deve lançar 'InsuficientSalableBookUnitsException' ao tentar realizar venda de unidades além do disponível em estoque")
	void sellBooks_throwsInsuficientSalableBookUnitsException() {
		assertThrows(
			InsuficientSalableBookUnitsException.class,
			() -> bookSellerImpl.sellBooks(bookSellListDTOSingleBook));
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao tentar vender livro inexistente")
	void sellBooks_throwsSalableBookNotFoundException() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> bookSellerImpl.sellBooks(bookSellListDTOSingleBook));
	}
}