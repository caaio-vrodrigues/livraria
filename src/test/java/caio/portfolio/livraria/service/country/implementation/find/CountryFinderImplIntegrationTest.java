package caio.portfolio.livraria.service.country.implementation.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import caio.portfolio.livraria.exception.custom.country.CountryNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class CountryFinderImplIntegrationTest {

	@Autowired private CountryFinderImpl countryFinderImpl;
	
	private static final String BRAZIL_CODE = "BR";
	private static final String BRAZIL_NAME = "Brazil";
	private static final Integer BRAZIL_ID = 1;
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Deve buscar país por código iso e retorná-lo")
	void findByIsoAlpha2Code_returnsCountry() {
		Country brazil = countryFinderImpl.findByIsoAlpha2Code(BRAZIL_CODE);
		assertNotNull(brazil);
		assertEquals(BRAZIL_NAME, brazil.getName());
		assertEquals(BRAZIL_CODE, brazil.getIsoAlpha2Code());
	}
	
	@Test
	@DisplayName("Deve lançar 'CountryNotFoundException' ao buscar país por código iso")
	void findByIsoAlpha2Code_throwsCountryNotFoundException() {
		assertThrows(
			CountryNotFoundException.class,
			() -> countryFinderImpl.findByIsoAlpha2Code(BRAZIL_CODE));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Deve buscar país por 'id' e retorná-lo")
	void findById_returnsCountry() {
		Country brazil = countryFinderImpl.findById(BRAZIL_ID);
		assertNotNull(brazil);
		assertEquals(BRAZIL_NAME, brazil.getName());
		assertEquals(BRAZIL_CODE, brazil.getIsoAlpha2Code());
	}
	
	@Test
	@DisplayName("Deve lançar 'CountryNotFoundException' ao buscar país por 'id'")
	void findById_throwsCountryNotFoundException() {
		assertThrows(
			CountryNotFoundException.class,
			() -> countryFinderImpl.findById(BRAZIL_ID));
	}
}