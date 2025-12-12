package caio.portfolio.livraria.service.book.salable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import caio.portfolio.livraria.exception.custom.book.salable.SalableBookAlreadyExistsException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.service.book.salable.dto.TitleAndAuthorUpdateDTO;

@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace=Replace.ANY)
class SalableBookUniquenessValidatorImplIntegrationTest {
	
	@Autowired private SalableBookUniquenessValidatorImpl salableBookUniquenessValidatorImpl;
	
	private static final Long MACHADO_ID = 2L;
	private static final Long PAULO_COELHO_ID = 1L;
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	private static final String PAULO_COLEHO_ALIAS = "O Mago";
	private static final String PAULO_COELHO_FULL_NAME = "Paulo Coelho";
	private static final String DOM_CASMURRO_TITLE = "Dom Casmurro";
	private static final String NEW_TITLE = "New Title";
	private static final String O_ALQUIMISTA_TITLE = "O Alquimista";
	private static final String MACHADO_ALIAS = "machado.ass";
	private static final String MACHADO_FULL_NAME = "Machado de Assis";
	private static final Integer BRAZIL_ID = 1;
	private static final LocalDate MACHADO_BIRTHDAY = LocalDate.of(1839, 6, 21);
	private static final LocalDate PAULO_COELHO_BIRTHDAY = LocalDate.of(1947, 8, 24);
	
	private static final Country BRAZIL = Country.builder()
		.id(BRAZIL_ID)
		.isoAlpha2Code(BRAZIL_CODE)
		.name(BRAZIL_NAME)
		.build(); 
	
	private static final Author MACHADO_DE_ASSIS = Author.builder()
		.id(MACHADO_ID)
		.alias(MACHADO_ALIAS)
		.fullName(MACHADO_FULL_NAME)
		.birthday(MACHADO_BIRTHDAY)
		.country(BRAZIL)
		.build();
	
	private static final Author PAULO_COELHO = Author.builder()
		.id(PAULO_COELHO_ID)
		.alias(PAULO_COLEHO_ALIAS)
		.fullName(PAULO_COELHO_FULL_NAME)
		.birthday(PAULO_COELHO_BIRTHDAY)
		.country(BRAZIL)
		.build();
	
	private static final TitleAndAuthorUpdateDTO O_ALQUIMISTA_UPDATE_DTO = TitleAndAuthorUpdateDTO
		.builder()
		.author(PAULO_COELHO)
		.title(O_ALQUIMISTA_TITLE)
		.build();
	
	private static final TitleAndAuthorUpdateDTO UPDATE_DTO = TitleAndAuthorUpdateDTO
		.builder()
		.author(MACHADO_DE_ASSIS)
		.title(NEW_TITLE)
		.build();
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve validar unicidade de livro com sucesso ao receber novos valores para atualização")
	void validateUniquenessOnUpdate_differentArgumentValues_successfulValidation() {
		assertDoesNotThrow(
			() -> salableBookUniquenessValidatorImpl
				.validateUniquenessOnUpdate(
					UPDATE_DTO, O_ALQUIMISTA_TITLE, PAULO_COELHO_ID),
			"Não deve lançar RuntimeException");
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve validar unicidade de livro com sucesso ao receber mesmos valores para atualização")
	void validateUniquenessOnUpdate_sameArgumentValues_successfulValidation() {
		assertDoesNotThrow(
			() -> salableBookUniquenessValidatorImpl
				.validateUniquenessOnUpdate(
					O_ALQUIMISTA_UPDATE_DTO, O_ALQUIMISTA_TITLE, PAULO_COELHO_ID),
			"Não deve lançar RuntimeException");
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve lançar 'SalableBookAlreadyExistsException' ao tentar validação de unicidade para atualização com livro já existente")
	void validateUniquenessOnUpdate_throwsSalableBookAlreadyExistsException() {
		assertThrows(
			SalableBookAlreadyExistsException.class,
			() -> salableBookUniquenessValidatorImpl
				.validateUniquenessOnUpdate(
					O_ALQUIMISTA_UPDATE_DTO, DOM_CASMURRO_TITLE, MACHADO_ID));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve validar unicidade de livro com sucesso para criação")
	void validateUniquenessOnCreate_successfulValidation() {
		assertDoesNotThrow(
			() -> salableBookUniquenessValidatorImpl
				.validateUniquenessOnCreate(PAULO_COELHO, O_ALQUIMISTA_TITLE),
			"Não deve lançar RuntimeException");
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@Sql("/sql/book/salable/insert_salable_book_list.sql")
	@DisplayName("Deve lançar 'SalableBookAlreadyExistsException' ao tentar validar unicidade de livro já existente")
	void validateUniquenessOnCreate_throwsSalableBookAlreadyExistsException() {
		assertThrows(
			SalableBookAlreadyExistsException.class,
			() -> salableBookUniquenessValidatorImpl
				.validateUniquenessOnCreate(PAULO_COELHO, O_ALQUIMISTA_TITLE));
	}
}