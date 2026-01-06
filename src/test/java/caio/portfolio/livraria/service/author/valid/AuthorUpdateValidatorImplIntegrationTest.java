package caio.portfolio.livraria.service.author.valid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.country.CountryNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.service.author.implementation.validate.AuthorUpdateValidatorImpl;

@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace=Replace.ANY)
class AuthorUpdateValidatorImplIntegrationTest {
    
    @Autowired private AuthorUpdateValidatorImpl authorUpdateValidatorImpl;
    
	private static final String NEW_ALIAS = "monteiro.lob";
	private static final String AUTHOR_ALIAS = "Caio VR";
	private static final String AUTHOR_FULLNAME = "Caio Vinicius Rodrigues";
	private static final String AUTHOR_NEW_FULLNAME = "Caio V. Rodrigues";
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	private static final Integer BRAZIL_ID = 1;
	private static final Integer ITALY_ID = 2;
	private static final Integer  NON_EXISTENT_COUNTRY_ID = 3;
	private static final LocalDate AUTHOR_BIRTHDAY = LocalDate.of(1992, 03, 20);
	private static final LocalDate NEW_BIRTHDAY = LocalDate.of(2008, 8, 10);
    
    private static final Country BRAZIL = Country.builder()
        .id(BRAZIL_ID)
        .isoAlpha2Code(BRAZIL_CODE)
        .name(BRAZIL_NAME)
        .build();

    @Test
	@DisplayName("Deve validar 'alias' diferente do atual e retorna-lo")
	void validateAlias_returnsNewAlias(){
		String updatedAlias = authorUpdateValidatorImpl
			.validateAlias(AUTHOR_ALIAS, NEW_ALIAS);
		assertNotNull(updatedAlias);
		assertEquals(NEW_ALIAS, updatedAlias);
	}
	
	@Test
	@DisplayName("Deve validar 'alias' igual ao atual e retorna-lo")
	void validateAlias_returnsExistingAlias(){
		String updatedAlias = authorUpdateValidatorImpl
			.validateAlias(AUTHOR_ALIAS, AUTHOR_ALIAS);
		assertNotNull(updatedAlias);
		assertEquals(AUTHOR_ALIAS, updatedAlias);
	}
	
	@Test
	@DisplayName("Deve receber 'null' e retornar o 'alias' existente")
	void validateAlias_nullArgument_returnsExistingAlias(){
		String updatedAlias = authorUpdateValidatorImpl
			.validateAlias(AUTHOR_ALIAS, null);
		assertNotNull(updatedAlias);
		assertEquals(
			AUTHOR_ALIAS, 
			updatedAlias);
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
    @DisplayName("Deve lançar 'AuthorAlreadyExistsException' se o novo 'alias' já estiver em uso")
    void validateAlias_throwsAuthorAlreadyExistsException() {
		assertThrows(
			AuthorAlreadyExistsException.class,
			() -> authorUpdateValidatorImpl
        		.validateAlias(AUTHOR_ALIAS, NEW_ALIAS));
	}
	
	@Test
	@DisplayName("Deve receber 'fullName' igual ao existente e retorna-lo")
	void validateFullName_returnsExistingFullName() {
		String updatedAuthorName = authorUpdateValidatorImpl
			.validateFullName(AUTHOR_FULLNAME, AUTHOR_FULLNAME);
		assertNotNull(updatedAuthorName);
		assertEquals(AUTHOR_FULLNAME, updatedAuthorName);
	}
	
	@Test
	@DisplayName("Deve receber 'fullName' diferente do existente e retorna-lo")
	void validateFullName_returnsNewFullName() {
		String updatedAuthorName = authorUpdateValidatorImpl
			.validateFullName(AUTHOR_FULLNAME, AUTHOR_NEW_FULLNAME);
		assertNotNull(updatedAuthorName);
		assertEquals(AUTHOR_NEW_FULLNAME, updatedAuthorName);
	}
	
	@Test
	@DisplayName("Deve receber 'null' e retornar 'fullName' existente")
	void validateFullName_nullArgument_returnsExistingFullName() {
		String updatedAuthorName = authorUpdateValidatorImpl
			.validateFullName(AUTHOR_FULLNAME, null);
		assertNotNull(updatedAuthorName);
		assertEquals(AUTHOR_FULLNAME, updatedAuthorName);
	}
	
	@Test
	@DisplayName("Deve receber 'birthday' igual ao existente e retorna-lo")
	void validateBirthday_returnsExistingBirthday() {
		LocalDate updatedBirthday = authorUpdateValidatorImpl
			.validateBirthday(AUTHOR_BIRTHDAY, AUTHOR_BIRTHDAY);
		assertNotNull(updatedBirthday);
		assertEquals(AUTHOR_BIRTHDAY, updatedBirthday);
	}
	
	@Test
	@DisplayName("Deve receber 'birthday' diferente do existente e retorna-lo")
	void validateBirthday_returnsNewBirthday() {
		LocalDate updatedBirthday = authorUpdateValidatorImpl
			.validateBirthday(AUTHOR_BIRTHDAY, NEW_BIRTHDAY);
		assertNotNull(updatedBirthday);
		assertEquals(NEW_BIRTHDAY, updatedBirthday);
	}
	
	@Test
	@DisplayName("Deve receber 'null' e retornar 'birthday' existente")
	void validateBirthday_nullArgument_returnsExistingBirthday() {
		LocalDate updatedBirthday = authorUpdateValidatorImpl
			.validateBirthday(AUTHOR_BIRTHDAY, null);
		assertNotNull(updatedBirthday);
		assertEquals(AUTHOR_BIRTHDAY, updatedBirthday);
	}
	
	@Test
	@DisplayName("Deve receber 'countryId' igual ao 'id' contido no país do autor e retornar mesmo país do autor")
	void validateCountry_returnsExistingCountry() {
		Country updatedCountry = authorUpdateValidatorImpl
			.validateCountry(BRAZIL, BRAZIL_ID);
		assertNotNull(updatedCountry);
		assertEquals(BRAZIL_ID, updatedCountry.getId());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Deve receber 'countryId' diferente do 'id' contido no país do autor e retornar país diferente do país do autor")
	void validateCountry_returnsNewCountry() {
		Country updatedCountry = authorUpdateValidatorImpl
			.validateCountry(BRAZIL, ITALY_ID);
		assertNotNull(updatedCountry);
		assertEquals(ITALY_ID, updatedCountry.getId());
	}
	
	@Test
	@DisplayName("Deve receber 'null' e retornar mesmo país do autor")
	void validateCountry_nullArgument_returnsExistingCountry() {
		Country updatedCountry = authorUpdateValidatorImpl
			.validateCountry(BRAZIL, null);
		assertNotNull(updatedCountry);
		assertEquals(BRAZIL_ID, updatedCountry.getId());
	}
	
	@Test
    @DisplayName("Deve lançar 'CountryNotFoundException' se o 'countryId' para atualização não for encontrado")
    void validateCountry_throwsCountryNotFoundException() {
		assertThrows(
			CountryNotFoundException.class,
			() -> authorUpdateValidatorImpl
        		.validateCountry(BRAZIL, NON_EXISTENT_COUNTRY_ID));
	}
}