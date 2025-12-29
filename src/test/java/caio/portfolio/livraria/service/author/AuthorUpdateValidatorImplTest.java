package caio.portfolio.livraria.service.author;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.country.CountryNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import caio.portfolio.livraria.service.author.implementation.validate.AuthorUpdateValidatorImpl;
import caio.portfolio.livraria.service.country.CountryService;

@ExtendWith(MockitoExtension.class)
class AuthorUpdateValidatorImplTest {
	
	@InjectMocks AuthorUpdateValidatorImpl authorUpdateValidatorImpl;
	@Mock private CountryService countryService;
	@Mock private AuthorRepository repo;
	
	private static final Long EXISTING_AUTHOR_ID = 2L;
	private static final String NEW_ALIAS = "CVR";
	private static final String INVALID_ALIAS = " ";
	private static final String EXISTING_AUTHOR_FULL_NAME = "Alessandro Del Piero";
	private static final String AUTHOR_ALIAS = "Caio VR";
	private static final String AUTHOR_FULLNAME = "Caio Vinicius Rodrigues";
	private static final String AUTHOR_NEW_FULLNAME = "Caio V. Rodrigues";
	private static final String BRAZIL_NAME = "Brazil";
	private static final String ITALY_NAME = "Italy";
	private static final String BRAZIL_CODE = "BR";
	private static final String ITALY_CODE = "IT";
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
	
	private static final Country ITALY = Country.builder()
		.id(ITALY_ID)
		.name(ITALY_NAME)
		.isoAlpha2Code(ITALY_CODE)
		.build(); 
	
	private static final Author EXISTING_AUTHOR_WITH_NEW_ALIAS = Author
		.builder()
        .id(EXISTING_AUTHOR_ID)
        .alias(NEW_ALIAS)
        .fullName(EXISTING_AUTHOR_FULL_NAME)
        .country(ITALY)
        .build();
	
	@Test
	@DisplayName("Deve validar 'alias' diferente do atual e retorna-lo")
	void validateAlias_returnsNewAlias(){
		when(repo.findByAlias(NEW_ALIAS))
			.thenReturn(Optional.empty());
		String updatedAlias = authorUpdateValidatorImpl
			.validateAlias(AUTHOR_ALIAS, NEW_ALIAS);
		assertNotNull(updatedAlias);
		assertEquals(
			NEW_ALIAS, 
			updatedAlias);
		verify(repo).findByAlias(NEW_ALIAS);
	}
	
	@Test
	@DisplayName("Deve validar 'alias' igual ao atual e retorna-lo")
	void validateAlias_returnsExistingAlias(){
		String updatedAlias = authorUpdateValidatorImpl
			.validateAlias(AUTHOR_ALIAS, AUTHOR_ALIAS);
		assertNotNull(updatedAlias);
		assertEquals(AUTHOR_ALIAS, updatedAlias);
		verifyNoInteractions(repo);
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
		verifyNoInteractions(repo); 
	}
	
	@Test
	@DisplayName("Deve receber argumento vazio e retornar 'RuntimeException'")
	void validateAlias_returnsRuntimeException(){
		when(repo.findByAlias(" "))
			.thenThrow(RuntimeException.class);
		assertThrows(
			RuntimeException.class, 
			() -> authorUpdateValidatorImpl
				.validateAlias(AUTHOR_ALIAS, INVALID_ALIAS));
		verify(repo).findByAlias(INVALID_ALIAS);
	}
	
	@Test
    @DisplayName("Deve lançar 'AuthorAlreadyExistsException' se o novo 'alias' já estiver em uso")
    void validateAlias_throwsAuthorAlreadyExistsException() {
		when(repo.findByAlias(NEW_ALIAS))
			.thenReturn(Optional.of(EXISTING_AUTHOR_WITH_NEW_ALIAS));
		AuthorAlreadyExistsException thrown = assertThrows(
            AuthorAlreadyExistsException.class,
            () -> authorUpdateValidatorImpl
            	.validateAlias(AUTHOR_ALIAS, NEW_ALIAS));
		assertTrue(thrown.getMessage()
			.contains(EXISTING_AUTHOR_WITH_NEW_ALIAS.getFullName()));
		verify(repo).findByAlias(NEW_ALIAS);
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
		verifyNoInteractions(countryService);
	}
	
	@Test
	@DisplayName("Deve receber 'countryId' diferente do 'id' contido no país do autor e retornar país diferente do país do autor")
	void validateCountry_returnsNewCountry() {
		when(countryService.getCountryById(ITALY_ID)).thenReturn(ITALY);
		Country updatedCountry = authorUpdateValidatorImpl
			.validateCountry(BRAZIL, ITALY_ID);
		assertNotNull(updatedCountry);
		assertEquals(ITALY_ID, updatedCountry.getId());
		verify(countryService).getCountryById(ITALY_ID);
	}
	
	@Test
	@DisplayName("Deve receber 'null' e retornar mesmo país do autor")
	void validateCountry_nullArgument_returnsExistingCountry() {
		Country updatedCountry = authorUpdateValidatorImpl
			.validateCountry(BRAZIL, null);
		assertNotNull(updatedCountry);
		assertEquals(BRAZIL_ID, updatedCountry.getId());
		verifyNoInteractions(countryService);
	}
	
	@Test
    @DisplayName("Deve lançar 'CountryNotFoundException' se o 'countryId' para atualização não for encontrado")
    void validateCountry_throwsCountryNotFoundException() {
		when(countryService.getCountryById(NON_EXISTENT_COUNTRY_ID))
			.thenThrow(new CountryNotFoundException("País com 'id': "+NON_EXISTENT_COUNTRY_ID+" não encontrado"));
		CountryNotFoundException thrown = assertThrows(
            CountryNotFoundException.class,
            () -> authorUpdateValidatorImpl
            	.validateCountry(BRAZIL, NON_EXISTENT_COUNTRY_ID));
		assertTrue(thrown.getMessage().contains(" "+NON_EXISTENT_COUNTRY_ID));
		verify(countryService).getCountryById(NON_EXISTENT_COUNTRY_ID);
	}
}
