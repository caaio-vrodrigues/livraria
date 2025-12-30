package caio.portfolio.livraria.service.publisher;

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
import caio.portfolio.livraria.exception.custom.publisher.PublisherAlreadyExistsException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.service.publisher.implementation.validate.PublisherUpdateValidatorImpl;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class PublisherUpdateValidatorImplIntegrationTest {

	@Autowired PublisherUpdateValidatorImpl publisherUpdateValidatorImpl;
	
	private static final Long ROCCO_ID = 1L;
	private static final String ROCCO_NAME = "Rocco";
	private static final String BRAZIL_NAME = "Brazil";
	private static final String ITALY_NAME = "Italy";
	private static final String BRAZIL_ISO_CODE = "BR";
	private static final String ITALY_ISO_CODE = "IT";
	private static final String ROCCO_FULL_ADDRESS = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	private static final String ROCCO_NEW_FULL_ADDRESS = "Rua do Passeio, 888, 8º andar, no Passeio Corporate";
	private static final String GLOBAL_BOOKS_FULL_ADDRESS = "123 Main Street, New York, USA";
	private static final Integer BRAZIL_ID = 1;
	private static final Integer ITALY_ID = 7;
	private static final Integer INVALID_COUNTRY_ID = 300;
	
	private static final Country BRAZIL = Country.builder()
		.id(BRAZIL_ID)
		.name(BRAZIL_NAME)
		.isoAlpha2Code(BRAZIL_ISO_CODE)
		.build(); 
	
	private static final Country ITALY = Country.builder()
		.id(ITALY_ID)
		.name(ITALY_NAME)
		.isoAlpha2Code(ITALY_ISO_CODE)
		.build();
	
	private static final Publisher ROCCO_PUBLISHER = Publisher.builder()
		.id(ROCCO_ID)
		.name(ROCCO_NAME)
		.country(BRAZIL)
		.fullAddress(ROCCO_FULL_ADDRESS)
		.build(); 
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Deve receber 'Country' atual e 'id' de um país diferente para retornar novo 'Country'")
	void validateCountry_returnsNewCountry() {
		Country validatedCountry = publisherUpdateValidatorImpl
			.validateCountry(ROCCO_PUBLISHER.getCountry(), ITALY_ID);
		assertNotNull(validatedCountry);
		assertEquals(
			ITALY.getName(), 
			validatedCountry.getName());
		assertEquals(
			ITALY.getIsoAlpha2Code(), 
			validatedCountry.getIsoAlpha2Code());
		assertEquals(
			ITALY.getId(), 
			validatedCountry.getId());
	}
	
	@Test
	@Sql("/sql/country/insert_brazil.sql")
	@DisplayName("Deve receber 'Country' atual e 'countryId' não existente para lançar 'CountryNotFoundException'")
	void validateCountry_throwsCountryNotFoundException() {
		assertThrows(
			CountryNotFoundException.class, 
			() -> publisherUpdateValidatorImpl
				.validateCountry(
					ROCCO_PUBLISHER.getCountry(), 
					INVALID_COUNTRY_ID));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@DisplayName("Deve receber 'fullAddress' atual e valor diferente para retornar novo 'fullAddress'")
	void validateFullAddress_returnsNewFullAddress() {
		String updatedFullAddress = publisherUpdateValidatorImpl
			.validateFullAddress(
				ROCCO_PUBLISHER.getFullAddress(), 
				ROCCO_NEW_FULL_ADDRESS);
		assertNotNull(updatedFullAddress);
		assertEquals(ROCCO_NEW_FULL_ADDRESS, updatedFullAddress);
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/publisher/insert_publisher_list.sql")
	@DisplayName("Deve receber 'fullAddress' atual e valor já existente para lançar 'PublisherAlreadyExistsException'")
	void validateFullAddress_throwsPublisherAlreadyExistsException() {
		assertThrows(
			PublisherAlreadyExistsException.class, 
			() -> publisherUpdateValidatorImpl.validateFullAddress(
					ROCCO_PUBLISHER.getFullAddress(), 
					GLOBAL_BOOKS_FULL_ADDRESS));
	}
}
