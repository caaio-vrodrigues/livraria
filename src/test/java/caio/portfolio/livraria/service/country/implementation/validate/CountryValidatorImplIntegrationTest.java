package caio.portfolio.livraria.service.country.implementation.validate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import caio.portfolio.livraria.service.country.model.validate.CountryValidator;

@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace=Replace.ANY)
class CountryValidatorImplIntegrationTest {

	@Autowired CountryValidator validator;
	
	private static final String BRAZIL_NAME = "Brazil";
	private static final String VALID_BRAZIL_CODE = "BR";
	private static final String RAW_BRAZIL_CODE = "br ";
	private static final String VALID_IRELAND_CODE = "IR";
	private static final String RAW_IRELAND_CODE = "ir ";
	private static final String INVALID_COUNTRY_CODE = "UR";
	private static final String INVALID_CODE = " ";
	
	@Test
	@DisplayName("Deve retornar 'isoAlpha2Code' normalizado para valor válido")
    void processIsoAlpha2Code_returnsValidIsoAlpha2Code() {
		assertEquals(
			VALID_BRAZIL_CODE, 
			validator.processIsoAlpha2Code(RAW_BRAZIL_CODE));
        assertEquals(
        	VALID_IRELAND_CODE, 
        	validator.processIsoAlpha2Code(RAW_IRELAND_CODE));
	}
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' ao enviar argumento nulo")
	void processIsoAlpha2Code_throwsExceptionForNull() {
        assertThrows(
        	IllegalArgumentException.class, 
        	() -> validator.processIsoAlpha2Code(null));
    }
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' ao enviar argumento vazio")
    void processIsoAlpha2Code_throwsExceptionForBlank() {
		assertThrows(
			IllegalArgumentException.class, 
			() -> validator.processIsoAlpha2Code(INVALID_CODE));
    }
	
	@Test
	@DisplayName("Deve lançar 'IllegalArgumentException' ao enviar argumento inválido")
    void processIsoAlpha2Code_throwsExceptionForInvalidCode() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> validator.processIsoAlpha2Code(INVALID_COUNTRY_CODE)
        );
        assertTrue(exception.getMessage().contains(INVALID_COUNTRY_CODE));
    }
	
	@Test
	@DisplayName("Deve retornar nome de país ao receber 'isoAlpha2Code' para validação")
	void resolveNameByIsoAlpha2Code_returnsCountryName() {
		assertEquals(
			BRAZIL_NAME, 
			validator.resolveNameByIsoAlpha2Code(VALID_BRAZIL_CODE));
	}
	
	@Test
	@DisplayName("Deve propagar exceção ao receber código inválido")
	void resolveNameByIsoAlpha2Code_throwsExceptionForInvalidCode() {
		assertThrows(
			IllegalArgumentException.class,
			() -> validator.resolveNameByIsoAlpha2Code(INVALID_COUNTRY_CODE));
	}
	
	@Test
	@DisplayName("Deve retornar nome de país após receber 'isoAlpha2Code' já validado")
	void getNameByValidatedAndNormalizedIsoAlpha2Code_returnsCountryName() {
		assertEquals(
			BRAZIL_NAME, 
			validator.getNameByValidatedAndNormalizedIsoAlpha2Code(
				VALID_BRAZIL_CODE));
	}
}
