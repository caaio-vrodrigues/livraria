package caio.portfolio.livraria.service.publisher;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import caio.portfolio.livraria.exception.custom.country.CountryNotFoundException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherAlreadyExistsException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.repository.PublisherRepository;
import caio.portfolio.livraria.service.country.CountryService;
import caio.portfolio.livraria.service.publisher.implementation.validate.PublisherUpdateValidatorImpl;

@ExtendWith(MockitoExtension.class)
class PublisherUpdateValidatorImplTest {

	@InjectMocks private PublisherUpdateValidatorImpl publisherUpdateValidatorImpl;
	@Mock private CountryService countryService;
	@Mock private PublisherRepository repo;
	
	private static final Long ROCCO_ID = 1L;
	private static final String ROCCO_NAME = "Rocco";
	private static final String ROCCO_NEW_NAME = "Rocco Updated";
	private static final String BRAZIL_NAME = "Brazil";
	private static final String ITALY_NAME = "Italy";
	private static final String BRAZIL_ISO_CODE = "BR";
	private static final String ITALY_ISO_CODE = "IT";
	private static final String ROCCO_FULL_ADDRESS = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	private static final String ROCCO_NEW_FULL_ADDRESS = "Rua do Passeio, 888, 8º andar, no Passeio Corporate";
	private static final String GLOBAL_BOOKS_FULL_ADDRESS = "123 Main Street, New York, USA";
	private static final Integer BRAZIL_ID = 1;
	private static final Integer ITALY_ID = 2;
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
	@DisplayName("Deve receber 'name' atual e novo valor como argumentos para retornar novo valor após validação")
	void validateName_returnsNewValidatedName() {
		String validatedName = publisherUpdateValidatorImpl
			.validateName(
				ROCCO_PUBLISHER.getName(), 
				ROCCO_NEW_NAME);
		assertNotNull(validatedName);
		assertEquals(ROCCO_NEW_NAME, validatedName);
	}
	
	@Test
	@DisplayName("Deve receber 'name' atual e mesmo valor como argumentos para retornar valor atual após validação")
	void validateName_returnsCurrentValidatedName() {
		String validatedName = publisherUpdateValidatorImpl
			.validateName(
				ROCCO_PUBLISHER.getName(), 
				ROCCO_NAME);
		assertNotNull(validatedName);
		assertEquals(
			ROCCO_PUBLISHER.getName(), 
			validatedName);
	}
	
	@Test
	@DisplayName("Deve receber 'name' atual e 'null' como argumentos para retornar valor atual após validação")
	void validateName_nullArgument_returnsCurrentValidatedName() {
		String validatedName = publisherUpdateValidatorImpl
			.validateName(
				ROCCO_PUBLISHER.getName(), 
				null);
		assertNotNull(validatedName);
		assertEquals(
			ROCCO_PUBLISHER.getName(), 
			validatedName);
	}
	
	@Test
	@DisplayName("Deve receber 'Country' atual e 'id' de um país diferente para retornar novo 'Country'")
	void validateCountry_returnsNewCountry() {
		when(countryService.getCountryById(ITALY_ID))
			.thenReturn(ITALY);
		Country validatedCountry = publisherUpdateValidatorImpl
			.validateCountry(
				ROCCO_PUBLISHER.getCountry(), 
				ITALY_ID);
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
		verify(countryService)
			.getCountryById(anyInt());
	}
	
	@Test
	@DisplayName("Deve receber 'Country' atual e 'id' do mesmo país para retornar 'Country' atual")
	void validateCountry_returnsCurrentCountry() {
		Country validatedCountry = publisherUpdateValidatorImpl
			.validateCountry(
				ROCCO_PUBLISHER.getCountry(), 
				BRAZIL_ID);
		assertNotNull(validatedCountry);
		assertEquals(
			BRAZIL.getName(), 
			validatedCountry.getName());
		assertEquals(
			BRAZIL.getIsoAlpha2Code(), 
			validatedCountry.getIsoAlpha2Code());
		assertEquals(
			BRAZIL.getId(), 
			validatedCountry.getId());
		verify(countryService, never())
			.getCountryById(anyInt());
	}
	
	@Test
	@DisplayName("Deve receber 'Country' atual e 'null' para retornar 'Country' atual")
	void validateCountry_nullArgument_returnsCurrentCountry() {
		Country validatedCountry = publisherUpdateValidatorImpl
			.validateCountry(ROCCO_PUBLISHER.getCountry(), null);
		assertNotNull(validatedCountry);
		assertEquals(
			BRAZIL.getName(), 
			validatedCountry.getName());
		assertEquals(
			BRAZIL.getIsoAlpha2Code(), 
			validatedCountry.getIsoAlpha2Code());
		assertEquals(
			BRAZIL.getId(), 
			validatedCountry.getId());
		verify(countryService, never())
			.getCountryById(anyInt());
	}
	
	@Test
	@DisplayName("Deve receber 'Country' atual e 'countryId' não existente para lançar 'CountryNotFoundException'")
	void validateCountry_throwsCountryNotFoundException() {
		when(countryService.getCountryById(INVALID_COUNTRY_ID))
			.thenThrow(new CountryNotFoundException("Não foi possível encontrar um país para o 'id' fornecido"));
		assertThrows(
			CountryNotFoundException.class, 
			() -> publisherUpdateValidatorImpl.validateCountry(
					ROCCO_PUBLISHER.getCountry(), 
					INVALID_COUNTRY_ID));
		verify(countryService)
			.getCountryById(anyInt());
	}
	
	@Test
	@DisplayName("Deve receber 'fullAddress' atual e valor diferente para retornar novo 'fullAddress'")
	void validateFullAddress_returnsNewFullAddress() {
		when(repo.findByFullAddress(ROCCO_NEW_FULL_ADDRESS)).thenReturn(Optional.empty());
		String updatedFullAddress = publisherUpdateValidatorImpl
			.validateFullAddress(ROCCO_PUBLISHER.getFullAddress(), ROCCO_NEW_FULL_ADDRESS);
		assertNotNull(updatedFullAddress);
		assertEquals(ROCCO_NEW_FULL_ADDRESS, updatedFullAddress);
		verify(repo)
			.findByFullAddress(anyString());
	}
	
	@Test
	@DisplayName("Deve receber 'fullAddress' atual e mesmo valor como argumentos para retornar 'fullAddress' atual")
	void validateFullAddress_returnsCurrentFullAddress() {
		String updatedFullAddress = publisherUpdateValidatorImpl
			.validateFullAddress(
				ROCCO_PUBLISHER.getFullAddress(), 
				ROCCO_FULL_ADDRESS);
		assertNotNull(updatedFullAddress);
		assertEquals(
			ROCCO_PUBLISHER.getFullAddress(), 
			updatedFullAddress);
		verify(repo, never())
			.findByFullAddress(anyString());
	}
	
	@Test
	@DisplayName("Deve receber 'fullAddress' atual e 'null' como argumentos para retornar 'fullAddress' atual")
	void validateFullAddress_nullArgument_returnsCurrentFullAddress() {
		String updatedFullAddress = publisherUpdateValidatorImpl
			.validateFullAddress(
				ROCCO_PUBLISHER.getFullAddress(), 
				null);
		assertNotNull(updatedFullAddress);
		assertEquals(
			ROCCO_PUBLISHER.getFullAddress(), 
			updatedFullAddress);
		verify(repo, never())
			.findByFullAddress(anyString());
	}
	
	@Test
	@DisplayName("Deve receber 'fullAddress' atual e valor já existente para lançar 'PublisherAlreadyExistsException'")
	void validateFullAddress_throwsPublisherAlreadyExistsException() {
		when(repo.findByFullAddress(GLOBAL_BOOKS_FULL_ADDRESS))
			.thenThrow(new PublisherAlreadyExistsException("'fullAddress' em uso"));
		assertThrows(
			PublisherAlreadyExistsException.class, 
			() -> publisherUpdateValidatorImpl.validateFullAddress(
					ROCCO_PUBLISHER.getFullAddress(), 
					GLOBAL_BOOKS_FULL_ADDRESS));
		verify(repo)
			.findByFullAddress(anyString());
	}
}
