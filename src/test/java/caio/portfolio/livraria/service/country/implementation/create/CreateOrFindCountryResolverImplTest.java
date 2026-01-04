package caio.portfolio.livraria.service.country.implementation.create;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;
import caio.portfolio.livraria.infrastructure.repository.CountryRepository;
import caio.portfolio.livraria.service.country.dto.implementation.CountryResultImplDTO;
import caio.portfolio.livraria.service.country.model.create.ResponseCountryDTOCreator;
import caio.portfolio.livraria.service.country.model.save.CountrySaverAndConcurrencyHandle;
import caio.portfolio.livraria.service.country.model.validate.CountryValidator;

@ExtendWith(MockitoExtension.class)
class CreateOrFindCountryResolverImplTest {

	@InjectMocks CreateOrFindCountryResolverImpl createOrFindCountryResolverImpl;
	@Mock private CountrySaverAndConcurrencyHandle saverAndConcurrencyHandleImpl;
	@Mock private ResponseCountryDTOCreator responseCountryDTOCreator;
	@Mock private CountryValidator countryValidator;
	@Mock private CountryRepository repo;
	
	private static final int BRAZIL_ID = 1;
	private static final int RESPONSE_BRAZIL_DTO_ID = 1;
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	private static final String NON_EXISTENT_COUNTRY_CODE = "IT ";
	
	private static final Country BRAZIL = Country.builder()
        .id(BRAZIL_ID)
        .isoAlpha2Code(BRAZIL_CODE)
        .name(BRAZIL_NAME)
        .build();
	
	private static final ResponseCountryDTO RESPONSE_BRAZIL_DTO = ResponseCountryDTO
		.builder()
		.id(RESPONSE_BRAZIL_DTO_ID)
		.isoAlpha2Code(BRAZIL_CODE)
		.name(BRAZIL_NAME)
		.build();
	
	@Test
	@DisplayName(value="Deve retornar DTO utilizando país já existente como referência")
	void returnResultWithExistentCountry() {
		when(repo.findByIsoAlpha2Code(anyString()))
			.thenReturn(Optional.of(BRAZIL));
		when(responseCountryDTOCreator
				.toResponseCountryDTO(any(Country.class)))
			.thenReturn(RESPONSE_BRAZIL_DTO);
		CountryResultImplDTO brazilResultImplDTO = createOrFindCountryResolverImpl
			.returnResultWithExistentCountry(BRAZIL_CODE);
		assertEquals(
			BRAZIL_CODE, 
			brazilResultImplDTO.getCountry().getIsoAlpha2Code());
		assertFalse(brazilResultImplDTO.wasCreated());
		assertTrue(brazilResultImplDTO.wasFound());
		verify(repo, times(1)).findByIsoAlpha2Code(anyString());
		verify(responseCountryDTOCreator, times(1))
			.toResponseCountryDTO(any(Country.class));
	}
	
	@Test
	@DisplayName(value="Deve retornar null para país não existente no banco de dados")
	void returnResultWithExistentCountry_returnsNull() {
		when(repo.findByIsoAlpha2Code(NON_EXISTENT_COUNTRY_CODE))
			.thenReturn(Optional.empty());
		assertNull(createOrFindCountryResolverImpl
			.returnResultWithExistentCountry(NON_EXISTENT_COUNTRY_CODE));
	}
	
	@Test
	@DisplayName(value="Deve retornar DTO após criar novo país para referência")
	void returnResultWithNewCountry() {
		when(countryValidator
				.getNameByValidatedAndNormalizedIsoAlpha2Code(anyString()))
			.thenReturn(BRAZIL_NAME);
		when(saverAndConcurrencyHandleImpl
				.saveAndDealingConcurrency(any(Country.class)))
			.thenReturn(BRAZIL);
		when(responseCountryDTOCreator
				.toResponseCountryDTO(any(Country.class)))
			.thenReturn(RESPONSE_BRAZIL_DTO);
		CountryResultImplDTO brazilResultImplDTO = createOrFindCountryResolverImpl
			.returnResultWithNewCountry(BRAZIL_CODE);
		assertEquals(
			BRAZIL_CODE, 
			brazilResultImplDTO.getCountry().getIsoAlpha2Code());
		assertTrue(brazilResultImplDTO.wasCreated());
		assertFalse(brazilResultImplDTO.wasFound());
		verify(countryValidator, times(1))
			.getNameByValidatedAndNormalizedIsoAlpha2Code(anyString());
		verify(saverAndConcurrencyHandleImpl, times(1))
			.saveAndDealingConcurrency(any(Country.class));
		verify(responseCountryDTOCreator, times(1))
			.toResponseCountryDTO(any(Country.class));
	}
}