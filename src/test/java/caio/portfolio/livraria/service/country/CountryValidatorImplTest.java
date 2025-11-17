package caio.portfolio.livraria.service.country;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CountryValidatorImplTest {
	
	@InjectMocks 
	private CountryValidatorImpl validator;
	
	private Set<String> testCodes;
	
	@BeforeEach
	void setUp() {
		testCodes = Set.of("BR", "IT", "AR", "FR");
		validator = new CountryValidatorImpl(testCodes);
	}
	
	@Test
	@DisplayName("Deve retornar 'isoAlpha2Code' normalizado caso seja um valor válido")
    void processIsoAlpha2Code_returnsValidIsoAlpha2Code() {
        assertEquals("BR", validator.processIsoAlpha2Code("br"));
        assertEquals("FR", validator.processIsoAlpha2Code(" fr "));
    }
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' ao enviar argumento nulo")
	void processIsoAlpha2Code_throwsExceptionForNull() {
        Assertions.assertThrows(
        	IllegalArgumentException.class, 
        	() -> validator.processIsoAlpha2Code(null));
    }
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' ao enviar argumento vazio")
    void processIsoAlpha2Code_throwsExceptionForBlank() {
		Assertions.assertThrows(
			IllegalArgumentException.class, 
			() -> validator.processIsoAlpha2Code("  "));
    }
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' ao enviar argumento inválido")
    void processIsoAlpha2Code_throwsExceptionForInvalidCode() {
        IllegalArgumentException exception = Assertions.assertThrows(
            IllegalArgumentException.class, 
            () -> validator.processIsoAlpha2Code("UR")
        );
        Assertions.assertTrue(exception.getMessage().contains("UR"));
    }
	
	@Test
	@DisplayName("Deve retornar nome de país ao receber 'isoAlpha2Code' para validação")
	void resolveNameByIsoAlpha2Code_returnsCountryName() {
		assertEquals("Brazil", validator.resolveNameByIsoAlpha2Code("BR"));
	}
	
	@Test
	@DisplayName("Deve propagar exceção ao receber código inválido")
	void resolveNameByIsoAlpha2Code_throwsExceptionForInvalidCode() {
		Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> validator.resolveNameByIsoAlpha2Code("UR"));
	}
	
	@Test
	@DisplayName("Deve retornar nome de país após receber 'isoAlpha2Code' já validado")
	void getNameByValidatedAndNormalizedIsoAlpha2Code_returnsCountryName() {
		assertEquals("Brazil", validator.getNameByValidatedAndNormalizedIsoAlpha2Code("BR"));
	}
}
