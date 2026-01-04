package caio.portfolio.livraria.service.country.implementation.create;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class CountryExceptionCreatorImplIntegrationTest {

	@Autowired private CountryExceptionCreatorImpl countryExceptionCreatorImpl;
	
	private static final String BRAZIL_CODE = "BR";
	private static final String INVALID_CODE = "UR";
	private static final String CONCURRENT_COUNTRY_EXCEPTION_MESSAGE = "Falha ao tentar criar país com `isoAlpha2Code`: `"+BRAZIL_CODE+"`";
	private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE_BY_BLANK = "O campo `isoAlpha2Code` não pode estar vazio";
	private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE_BY_INVALID_CODE = "O código `"+INVALID_CODE+"` não corresponde a um código de país válido";
	
	@Test
	@DisplayName("Deve lançar 'ConcurrentCountryException' ao tentar salvar país em cenário concorrente")
	void createConcurrentCountryException() {
		assertEquals(
			CONCURRENT_COUNTRY_EXCEPTION_MESSAGE, 
			countryExceptionCreatorImpl
				.createConcurrentCountryException(BRAZIL_CODE)
					.getLocalizedMessage());
	}
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' por argumento vazio")
	void createIllegalArgumentExceptionByBlank() {
		assertEquals(
			ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE_BY_BLANK, 
			countryExceptionCreatorImpl
				.createIllegalArgumentExceptionByBlank()
					.getLocalizedMessage());
	}
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' por argumento inválido")
	void createIllegalArgumentExceptionByInvalid() {
		assertEquals(
			ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE_BY_INVALID_CODE, 
			countryExceptionCreatorImpl
				.createIllegalArgumentExceptionByInvalid(INVALID_CODE)
					.getLocalizedMessage());
	}
}