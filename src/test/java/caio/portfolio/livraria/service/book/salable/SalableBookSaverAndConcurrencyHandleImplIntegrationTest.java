package caio.portfolio.livraria.service.book.salable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.model.enums.Genre;

@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace=Replace.ANY)
class SalableBookSaverAndConcurrencyHandleImplIntegrationTest {

	@Autowired SalableBookSaverAndConcurrencyHandleImpl salableBookSaverAndConcurrencyHandleImpl;
	
	private static final int UNITS = 50;
	private static final Long PAULO_COELHO_ID = 1L;
	private static final Long ROCCO_ID = 1L;
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	private static final String PAULO_COLEHO_ALIAS = "O Mago";
	private static final String PAULO_COELHO_FULL_NAME = "Paulo Coelho";
	private static final String ROCCO_NAME = "Rocco";
	private static final String ROCCO_FULL_ADDRESS = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	private static final String O_ALQUIMISTA_ISBN = "abc123";
	private static final String O_ALQUIMISTA_TITLE = "Current Title";
	private static final Integer BRAZIL_ID = 1;
	private static final LocalDate PAULO_COELHO_BIRTHDAY = LocalDate.of(1947, 8, 24);
	private static final BigDecimal O_ALQUIMISTA_PRICE = BigDecimal.valueOf(39.5);
	
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
		.author(PAULO_COELHO)	
		.title(O_ALQUIMISTA_TITLE)
		.genre(Genre.FANTASY)
		.isbn(O_ALQUIMISTA_ISBN)
		.publisher(ROCCO_PUBLISHER)
		.price(O_ALQUIMISTA_PRICE)
		.units(UNITS)
		.build();
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve salvar novo 'SalableBook' com sucesso tratando possível concorrência")
	void saveAndHandleConcurrency_returnsSalableBook() {
		SalableBook book = salableBookSaverAndConcurrencyHandleImpl
			.saveAndHandleConcurrency(O_ALQUIMISTA);
		assertNotNull(book);
		assertEquals(O_ALQUIMISTA.getId(), book.getId());
		assertEquals(O_ALQUIMISTA.getTitle(), book.getTitle());
		assertEquals(O_ALQUIMISTA.getAuthor(), book.getAuthor());
	}
}
