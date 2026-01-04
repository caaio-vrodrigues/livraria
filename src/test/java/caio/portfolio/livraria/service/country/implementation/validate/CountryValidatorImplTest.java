package caio.portfolio.livraria.service.country.implementation.validate;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import caio.portfolio.livraria.service.country.model.create.CountryExceptionCreator;

@ExtendWith(MockitoExtension.class)
class CountryValidatorImplTest {
	
	@InjectMocks private CountryValidatorImpl validator;
	@Mock private CountryExceptionCreator countryExceptionCreator;
	
	private static final String RAW_BRAZIL_CODE = "br ";
	private static final String BRAZIL_NAME = "Brazil";
	private static final String VALID_BRAZIL_CODE = "BR";
	private static final String VALID_ITALY_CODE = "IT";
	private static final String VALID_ARGENTINA_CODE = "AR";
	private static final String VALID_FRANCE_CODE = "FR";
	private static final String RAW_FRANCE_CODE = "fr ";
	private static final String INVALID_COUNTRY_CODE = "UR";
	private static final String BLANK_CODE = " ";
	
	
	private static final Set<String> CODES_LIST = new HashSet<>();
	
	private static final IllegalArgumentException EXCEPTION = new IllegalArgumentException();
	
	@BeforeEach
	void setUp() {
		CODES_LIST.add(VALID_BRAZIL_CODE);
		CODES_LIST.add(VALID_ITALY_CODE);
		CODES_LIST.add(VALID_ARGENTINA_CODE);
		CODES_LIST.add(VALID_FRANCE_CODE);
		validator = new CountryValidatorImpl(CODES_LIST, countryExceptionCreator);
	}
	
	@Test
	@DisplayName("Deve retornar 'isoAlpha2Code' normalizado caso seja um valor válido")
    void processIsoAlpha2Code_returnsValidIsoAlpha2Code() {
        assertEquals(
        	VALID_BRAZIL_CODE, 
        	validator.processIsoAlpha2Code(RAW_BRAZIL_CODE));
        assertEquals(
        	VALID_FRANCE_CODE, 
        	validator.processIsoAlpha2Code(RAW_FRANCE_CODE));
    }
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' ao enviar argumento nulo")
	void processIsoAlpha2Code_throwsExceptionForNull() {
		when(countryExceptionCreator
				.createIllegalArgumentExceptionByBlank())
			.thenReturn(EXCEPTION);
        assertThrows(
        	IllegalArgumentException.class, 
        	() -> validator.processIsoAlpha2Code(null));
        verify(countryExceptionCreator, times(1))
        	.createIllegalArgumentExceptionByBlank();
    }
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' ao enviar argumento vazio")
    void processIsoAlpha2Code_throwsExceptionForBlank() {
		when(countryExceptionCreator
				.createIllegalArgumentExceptionByBlank())
			.thenReturn(EXCEPTION);
		assertThrows(
			IllegalArgumentException.class, 
			() -> validator.processIsoAlpha2Code(BLANK_CODE));
		verify(countryExceptionCreator, times(1))
    		.createIllegalArgumentExceptionByBlank();
    }
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' ao enviar argumento inválido")
    void processIsoAlpha2Code_throwsExceptionForInvalidCode() {
		when(countryExceptionCreator
				.createIllegalArgumentExceptionByInvalid(anyString()))
			.thenReturn(EXCEPTION);
        assertThrows(
			IllegalArgumentException.class, 
			() -> validator.processIsoAlpha2Code(INVALID_COUNTRY_CODE));
        verify(countryExceptionCreator, times(1))
    		.createIllegalArgumentExceptionByInvalid(anyString());
    }
	
	@Test
	@DisplayName("Deve retornar nome de país ao receber 'isoAlpha2Code' para validação")
	void resolveNameByIsoAlpha2Code_returnsCountryName() {
		assertEquals(
			BRAZIL_NAME, 
			validator.resolveNameByIsoAlpha2Code(VALID_BRAZIL_CODE));
	}
	
	@Test
	@DisplayName("Deve propagar IllegalArgumentException ao receber código inválido")
	void resolveNameByIsoAlpha2Code_throwsExceptionForInvalidCode() {
		when(countryExceptionCreator
				.createIllegalArgumentExceptionByInvalid(anyString()))
			.thenReturn(EXCEPTION);
		assertThrows(
			IllegalArgumentException.class,
			() -> validator.resolveNameByIsoAlpha2Code(INVALID_COUNTRY_CODE));
		verify(countryExceptionCreator, times(1))
			.createIllegalArgumentExceptionByInvalid(anyString());
	}
	
	@Test
	@DisplayName("Deve retornar nome de país após receber 'isoAlpha2Code' já validado")
	void getNameByValidatedAndNormalizedIsoAlpha2Code_returnsCountryName() {
		assertEquals(
			BRAZIL_NAME, 
			validator.getNameByValidatedAndNormalizedIsoAlpha2Code(VALID_BRAZIL_CODE));
	}
}