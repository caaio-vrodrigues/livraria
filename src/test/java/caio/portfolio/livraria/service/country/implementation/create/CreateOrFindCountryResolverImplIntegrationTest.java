package caio.portfolio.livraria.service.country.implementation.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import caio.portfolio.livraria.service.country.dto.implementation.CountryResultImplDTO;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class CreateOrFindCountryResolverImplIntegrationTest {

	@Autowired private CreateOrFindCountryResolverImpl createOrFindCountryResolverImpl;
	
	private static final String BRAZIL_CODE = "BR";
	private static final String NON_EXISTENT_COUNTRY_CODE = "IT";
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName(value="Deve retornar DTO utilizando país já existente como referência")
	void returnResultWithExistentCountry() {
		CountryResultImplDTO brazilResultImplDTO = createOrFindCountryResolverImpl
			.returnResultWithExistentCountry(BRAZIL_CODE);
		assertEquals(
			BRAZIL_CODE, 
			brazilResultImplDTO.getCountry().getIsoAlpha2Code());
		assertFalse(brazilResultImplDTO.wasCreated());
		assertTrue(brazilResultImplDTO.wasFound());
	}
	
	@Test
	@DisplayName(value="Deve retornar null para país não existente no banco de dados")
	void returnResultWithExistentCountry_returnsNull() {
		assertNull(createOrFindCountryResolverImpl
			.returnResultWithExistentCountry(NON_EXISTENT_COUNTRY_CODE));
	}
	
	@Test
	@DisplayName(value="Deve retornar DTO após criar novo país para referência")
	void returnResultWithNewCountry() {
		CountryResultImplDTO brazilResultImplDTO = createOrFindCountryResolverImpl
			.returnResultWithNewCountry(BRAZIL_CODE);
		assertEquals(
			BRAZIL_CODE, 
			brazilResultImplDTO.getCountry().getIsoAlpha2Code());
		assertTrue(brazilResultImplDTO.wasCreated());
		assertFalse(brazilResultImplDTO.wasFound());
	}
}