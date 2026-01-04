package caio.portfolio.livraria.service.country.implementation.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;

import java.util.Locale;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@ExtendWith(MockitoExtension.class)
class CountryExceptionCreatorImplTest {

	@InjectMocks CountryExceptionCreatorImpl countryExceptionCreatorImpl;
	@Mock MessageSource countryMessageSource;
	
	private static final String BRAZIL_CODE = "BR";
	private static final String INVALID_CODE = "UR";
	private static final String CONCURRENT_COUNTRY_EXCEPTION_MESSAGE = "Falha ao tentar criar país com `isoAlpha2Code`: `"+BRAZIL_CODE+"`";
	private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE_BY_BLANK = "O campo `isoAlpha2Code` não pode estar vazio";
	private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE_BY_INVALID_CODE = "O código `"+INVALID_CODE+"` não corresponde a um código de país válido";
	
	@Test
	@DisplayName("Deve lançar 'ConcurrentCountryException' ao tentar salvar país em cenário concorrente")
	void createConcurrentCountryException() {
		when(countryMessageSource
			.getMessage(
				anyString(), 
				any(Object[].class), 
				any(Locale.class)))
			.thenReturn(CONCURRENT_COUNTRY_EXCEPTION_MESSAGE);
		assertEquals(
			CONCURRENT_COUNTRY_EXCEPTION_MESSAGE, 
			countryExceptionCreatorImpl
				.createConcurrentCountryException(BRAZIL_CODE).getLocalizedMessage());
	}
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' por argumento vazio")
	void createIllegalArgumentExceptionByBlank() {
		when(countryMessageSource
			.getMessage(
				anyString(), 
				any(Object[].class), 
				any(Locale.class)))
			.thenReturn(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE_BY_BLANK);
		assertEquals(
			ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE_BY_BLANK, 
			countryExceptionCreatorImpl
				.createIllegalArgumentExceptionByBlank().getLocalizedMessage());
	}
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' por argumento inválido")
	void createIllegalArgumentExceptionByInvalid() {
		when(countryMessageSource
			.getMessage(
				anyString(), 
				any(Object[].class), 
				any(Locale.class)))
			.thenReturn(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE_BY_INVALID_CODE);
		assertEquals(
			ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE_BY_INVALID_CODE, 
			countryExceptionCreatorImpl
				.createIllegalArgumentExceptionByInvalid(INVALID_CODE).getLocalizedMessage());
	}
}