package caio.portfolio.livraria.service.book.salable;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
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

import caio.portfolio.livraria.exception.custom.book.salable.InsuficientSalableBookUnitsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.BookSellDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.BookSellListDTO;

@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace=Replace.ANY)
class BookSellerImplIntegrationTest {

	@Autowired private BookSellerImpl bookSellerImpl;
	
	private static final int UNITS = 2;
	private static final int SELL_UNITS = 10;
	private static final Long O_ALQUIMISTA_ID = 1L;
	private static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(35.50);
	
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
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve realizar venda de livro com sucesso e retornar cálculo do total à pagar")
	void sellBook_returnsBigDecimal() {
		BigDecimal totalToPay = bookSellerImpl.sellBook(O_ALQUIMISTA_ID, UNITS);
		assertNotNull(totalToPay);
		assertEquals(
			BOOK_PRICE.multiply(BigDecimal.valueOf(UNITS)).floatValue(), 
			totalToPay.floatValue());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao tenatr realizar venda de livro não existente")
	void sellBook_throwsSalableBookNotFoundException() {
		assertThrows(
			SalableBookNotFoundException.class,
			() -> bookSellerImpl.sellBook(O_ALQUIMISTA_ID, UNITS));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve realizar venda de múltiplos livros com sucesso e retornar cálculo do total à pagar")
	void sellBooks_returnsBigDecimal() {
		BigDecimal totalToPay = bookSellerImpl.sellBooks(BOOK_SELL_LIST_DTO);
		assertNotNull(totalToPay);
		assertEquals(
			BOOK_PRICE.multiply(BigDecimal.valueOf(SELL_UNITS)).floatValue(), 
			totalToPay.floatValue());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list_zero_units.sql")
	@DisplayName("Deve lançar 'InsuficientSalableBookUnitsException' ao tentar realizar venda com unidades insuficientes")
	void sellBooks_throwsInsuficientSalableBookUnitsException() {
		assertThrows(
			InsuficientSalableBookUnitsException.class,
			() -> bookSellerImpl.sellBooks(BOOK_SELL_LIST_DTO));
	}
}