package caio.portfolio.livraria.service.country;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import caio.portfolio.livraria.config.CountryValidatorConfig;
import caio.portfolio.livraria.service.country.model.CountryValidator;

@SpringBootTest(
	classes = {
		CountryValidatorImpl.class,
        CountryValidatorConfig.class
	}
)
class CountryValidatorImplIntegrationTest {

	@Autowired CountryValidator validator;
	
	@Test
	@DisplayName("Deve retornar 'isoAlpha2Code' normalizado para valor válido")
    void processIsoAlpha2Code_returnsValidIsoAlpha2Code() {
		assertEquals("BR", validator.processIsoAlpha2Code("BR"));
        assertEquals("FR", validator.processIsoAlpha2Code("FR"));
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
