package caio.portfolio.livraria.service.country.implementation.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class CountryExceptionCreatorImplIntegrationTest {

	@Autowired private CountryExceptionCreatorImpl countryExceptionCreatorImpl;
	@Autowired private MessageSource countryMessageSource;
	
	private static final String BRAZIL_CODE = "BR";
	private static final String INVALID_CODE = "UR";
	
	@Test
	@DisplayName("Deve lançar 'ConcurrentCountryException' ao tentar salvar país em cenário concorrente")
	void createConcurrentCountryException() {
		String expectedMsg = countryMessageSource.getMessage(
			"concurret.country.iso", 
			new Object[] {BRAZIL_CODE},
			LocaleContextHolder.getLocale());
		String msg = countryExceptionCreatorImpl
			.createConcurrentCountryException(BRAZIL_CODE)
			.getLocalizedMessage();
		assertNotNull(msg);
		assertEquals(expectedMsg, msg);
	}
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' por argumento vazio")
	void createIllegalArgumentExceptionByBlank() {
		String expectedMsg = countryMessageSource.getMessage(
			"illegal.argument.blank.iso",
			new Object[] {},
			LocaleContextHolder.getLocale());
		String msg = countryExceptionCreatorImpl
			.createIllegalArgumentExceptionByBlank()
			.getLocalizedMessage();
		assertNotNull(msg);
		assertEquals(expectedMsg, msg);
	}
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' por argumento inválido")
	void createIllegalArgumentExceptionByInvalid() {
		String expectedMsg = countryMessageSource.getMessage(
			"illegal.argument.invalid.iso",
			new Object[] {INVALID_CODE},
			LocaleContextHolder.getLocale());
		String msg = countryExceptionCreatorImpl
			.createIllegalArgumentExceptionByInvalid(INVALID_CODE)
			.getLocalizedMessage();
		assertNotNull(msg);
		assertEquals(expectedMsg, msg);
	}
}