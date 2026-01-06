package caio.portfolio.livraria.service.publisher.implementation.save;

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
import org.springframework.test.context.jdbc.Sql;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class PublisherSaverAndConcurrencyHandleImplIntegrationTest {

	@Autowired private PublisherSaverAndConcurrencyHandleImpl publisherSaverAndConcurrencyHandleImpl;
	
	private static final String ROCCO_NAME = "Rocco";
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_ISO_CODE = "BR";
	private static final String ROCCO_FULL_ADDRESS = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	private static final Integer BRAZIL_ID = 1;
	
	private static final Country BRAZIL = Country.builder()
		.id(BRAZIL_ID)
		.name(BRAZIL_NAME)
		.isoAlpha2Code(BRAZIL_ISO_CODE)
		.build(); 
	
	private static final Publisher ROCCO_PUBLISHER = Publisher.builder()
		.name(ROCCO_NAME)
		.country(BRAZIL)
		.fullAddress(ROCCO_FULL_ADDRESS)
		.build();
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Deve salvar e retornar editora com sucesso")
	void saveAndHandlePublisherConcurrency_returnsPublisher() {
		Publisher rocco = publisherSaverAndConcurrencyHandleImpl
			.saveAndHandlePublisherConcurrency(ROCCO_PUBLISHER);
		assertNotNull(rocco);
		assertEquals(
			ROCCO_FULL_ADDRESS, 
			rocco.getFullAddress());
	}
}