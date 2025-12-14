package caio.portfolio.livraria.service.book.salable;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace=Replace.ANY)
class BookSellerImplIntegrationTest {

	@Autowired private BookSellerImpl bookSellerImpl;
	
	private static final Long O_ALQUIMISTA_ID = 1L;
	private static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(35.50);
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve realizar venda de 'SalableBook' com sucesso e retornar cálculo do total a pagar")
	void sellBook_returnsBigDecimal() {
		BigDecimal totalToPay = bookSellerImpl.sellBook(O_ALQUIMISTA_ID, 2);
		assertNotNull(totalToPay);
		assertEquals(
			BOOK_PRICE.multiply(BigDecimal.valueOf(2)).floatValue(), 
			totalToPay.floatValue());
	}
}