package caio.portfolio.livraria.service.country.implementation.save;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import caio.portfolio.livraria.infrastructure.entity.country.Country;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class CountrySaverAndConcurrencyHandleImplIntegrationTest {

	@Autowired private CountrySaverAndConcurrencyHandleImpl countrySaverAndConcurrencyHandleImpl;
	
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	
	private static final Country BRAZIL = Country.builder()
        .isoAlpha2Code(BRAZIL_CODE)
        .name(BRAZIL_NAME)
        .build();
	
	@Test
	@DisplayName("Deve salvar país com sucesso tratando cenário de concorrência")
	void saveAndDealingConcurrency_returnsCountry() {
		Country brazil = countrySaverAndConcurrencyHandleImpl
			.saveAndDealingConcurrency(BRAZIL);
		assertNotNull(brazil);
		assertEquals(BRAZIL_NAME, brazil.getName());
	}
}