package caio.portfolio.livraria.service.country;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import caio.portfolio.livraria.exception.custom.country.CountryNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.country.dto.CreateCountryDTO;
import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;
import caio.portfolio.livraria.infrastructure.repository.CountryRepository;
import caio.portfolio.livraria.service.country.dto.implementation.CountryResultImplDTO;
import caio.portfolio.livraria.service.country.model.create.CreateOrFindCountryResolver;
import caio.portfolio.livraria.service.country.model.create.ResponseCountryDTOCreator;
import caio.portfolio.livraria.service.country.model.find.CountryFinder;
import caio.portfolio.livraria.service.country.model.validate.CountryValidator;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

	@Mock private ResponseCountryDTOCreator responseCountryDTOCreator;
	@Mock private CountryRepository repo;
	@Mock private CountryValidator countryValidator; 
	@Mock private CountryFinder countryFinder;
	@Mock private CreateOrFindCountryResolver createOrFindCountryResolver;
	@InjectMocks private CountryService service;

	private static final String RAW_BRAZIL_CODE = "br ";
	private static final String VALID_BRAZIL_CODE = "BR";
	private static final Integer BRAZIL_ID = 1;
	private static final String BRAZIL_NAME = "Brazil";
	private static final String VALID_IRELAND_CODE = "IR";
	private static final String IRELAND_NAME = "Ireland";
	private static final Integer IRELAND_ID = 2;
	private static final Integer NON_EXISTING_ID = 100;
	
	private static final Country BRAZIL = Country.builder()
        .id(BRAZIL_ID)
        .isoAlpha2Code(VALID_BRAZIL_CODE)
        .name(BRAZIL_NAME)
        .build();
	
	private static final ResponseCountryDTO RESPONSE_BRAZILDTO = ResponseCountryDTO
		.builder()
        .id(BRAZIL_ID)
        .isoAlpha2Code(VALID_BRAZIL_CODE)
        .name(BRAZIL_NAME)
        .build();
	
	private static final Country IRELAND = Country.builder()
	    .id(IRELAND_ID)
	    .isoAlpha2Code(VALID_IRELAND_CODE)
	    .name(IRELAND_NAME)
	    .build();
		
	private static final ResponseCountryDTO RESPONSE_IRELANDDTO = ResponseCountryDTO
		.builder()
        .id(IRELAND_ID)
        .isoAlpha2Code(VALID_IRELAND_CODE)
        .name(IRELAND_NAME)
        .build();
	
	private static final CreateCountryDTO CREATE_COUNTRYDTO = CreateCountryDTO
		.builder()
	    .isoAlpha2Code(RAW_BRAZIL_CODE)
	    .build();
	
	private static final CountryResultImplDTO COUNTRY_RESULT_DTO = CountryResultImplDTO
		.builder()
		.country(RESPONSE_BRAZILDTO)
		.build();
	
	private static final List<Country> COUNTRIES_LIST = List.of(BRAZIL, IRELAND);
	
	@Test
	@DisplayName("Deve retornar país ao buscar com 'id' existente")
	void getCountryById_returnsCountry() {
		when(countryFinder.findById(anyInt()))
			.thenReturn(BRAZIL);
		when(responseCountryDTOCreator.toResponseCountryDTO(any(Country.class)))
			.thenReturn(RESPONSE_BRAZILDTO);
		ResponseCountryDTO result = service.getResponseCountryDTOById(BRAZIL_ID);
		assertNotNull(result);
		assertEquals(BRAZIL_ID, result.getId());
		assertEquals(BRAZIL_NAME, result.getName());
		verify(countryFinder, times(1))
			.findById(anyInt());
		verify(responseCountryDTOCreator, times(1))
			.toResponseCountryDTO(any(Country.class));
	 }
	 
	@Test
	@DisplayName("Deve retornar 'CountryNotFoundException' ao buscar com 'id' não existente")
	void getCountryById_returnsException() {
		when(countryFinder.findById(anyInt()))
			.thenThrow(CountryNotFoundException.class);
		assertThrows(
			CountryNotFoundException.class,
            () -> service.getCountryById(NON_EXISTING_ID));
		verify(countryFinder, times(1)).findById(NON_EXISTING_ID);
	}
	 
	@Test
	@DisplayName("Deve retornar país ao buscar com 'isoAlpha2Code' existente")
	void getCountryByIsoAlpha2Code_returnsCountry() {
		when(countryValidator.processIsoAlpha2Code(anyString()))
			.thenReturn(VALID_BRAZIL_CODE);
		when(countryFinder.findByIsoAlpha2Code(anyString()))
			.thenReturn(BRAZIL);
		when(responseCountryDTOCreator.toResponseCountryDTO(any(Country.class)))
			.thenReturn(RESPONSE_BRAZILDTO);
		ResponseCountryDTO reponseBrazilDTO = service
			.getCountryByIsoAlpha2Code(VALID_BRAZIL_CODE);
		assertEquals(
			VALID_BRAZIL_CODE, 
			reponseBrazilDTO.getIsoAlpha2Code());
		assertEquals(
			BRAZIL_NAME, 
			reponseBrazilDTO.getName());
		verify(countryValidator, times(1))
			.processIsoAlpha2Code(anyString());
		verify(countryFinder, times(1))
			.findByIsoAlpha2Code(anyString());
		verify(responseCountryDTOCreator, times(1))
			.toResponseCountryDTO(any(Country.class));
	}
	 
	@Test
	@DisplayName("Deve retornar 'CountryNotFoundException' ao buscar com 'isoAlpha2Code' não existente")
	void getCountryByIsoAlpha2Code_returnsException() {
		when(countryValidator.processIsoAlpha2Code(anyString()))
			.thenReturn(VALID_BRAZIL_CODE);
		when(countryFinder.findByIsoAlpha2Code(anyString()))
			.thenThrow(CountryNotFoundException.class);
	    assertThrows(
	    	CountryNotFoundException.class,
            () -> service.getCountryByIsoAlpha2Code(RAW_BRAZIL_CODE)
        );
	    verify(countryValidator, times(1))
			.processIsoAlpha2Code(anyString());
		verify(countryFinder, times(1))
			.findByIsoAlpha2Code(anyString());
	}
	 
	@Test
	@DisplayName("Deve retornar lista de dto's de países quando existem países cadastrados")
	void getAllCountries_returnsResponseCountryDTOList() {
		when(repo.findAll()).thenReturn(COUNTRIES_LIST);
		when(responseCountryDTOCreator.toResponseCountryDTO(any(Country.class)))
			.thenReturn(RESPONSE_BRAZILDTO)
			.thenReturn(RESPONSE_IRELANDDTO);
		List<ResponseCountryDTO> result = service.getAllCountries();
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(
			VALID_BRAZIL_CODE, 
			result.get(0).getIsoAlpha2Code());
		assertEquals(
			VALID_IRELAND_CODE, 
			result.get(1).getIsoAlpha2Code());
		verify(repo, times(1)).findAll();
		verify(responseCountryDTOCreator, times(2))
			.toResponseCountryDTO(any(Country.class));
	}
	 
	@Test
	@DisplayName("Deve retornar lista vazia quando não existem países cadastrados")
	void getAllCountries_returnsEmptyList() {
		when(repo.findAll()).thenReturn(List.of());
		List<ResponseCountryDTO> result = service.getAllCountries();
		assertNotNull(result);
		assertTrue(result.isEmpty());
		assertEquals(0, result.size());
		verify(repo, times(1)).findAll();
	}
	 
	@Test
	@DisplayName("Deve retornar país existente quando 'isoAlpha2Code' já está cadastrado")
	void createOrFindCountry_returnsExistingCountry() {
	    when(countryValidator.processIsoAlpha2Code(anyString()))
	        .thenReturn(VALID_BRAZIL_CODE);
	    when(createOrFindCountryResolver
	    	.returnResultWithExistentCountry(anyString()))
    			.thenReturn(COUNTRY_RESULT_DTO);
	    CountryResultImplDTO result = service
	    	.createOrFindCountry(CREATE_COUNTRYDTO);
	    assertNotNull(result);
	    assertEquals(
	    	VALID_BRAZIL_CODE, 
	    	result.getCountry().getIsoAlpha2Code());
	    assertEquals(
	    	BRAZIL_NAME, 
	    	result.getCountry().getName());
	    assertFalse(result.isCreated());
	    verify(countryValidator, times(1))
	    	.processIsoAlpha2Code(anyString());
	    verify(createOrFindCountryResolver, times(1))
	    	.returnResultWithExistentCountry(anyString());
	}
	 
	@Test
	@DisplayName("Deve criar novo país quando 'isoAlpha2Code' não existe")
	void createOrFindCountry_createNewCountry() {
	    when(countryValidator.processIsoAlpha2Code(anyString()))
	        .thenReturn(VALID_BRAZIL_CODE);
	    when(createOrFindCountryResolver
	    	.returnResultWithNewCountry(anyString()))
				.thenReturn(COUNTRY_RESULT_DTO);
	    CountryResultImplDTO result = service
	    	.createOrFindCountry(CREATE_COUNTRYDTO);
	    assertNotNull(result);
	    assertEquals(
	    	VALID_BRAZIL_CODE, 
	    	result.getCountry().getIsoAlpha2Code());
	    assertEquals(
	    	BRAZIL_NAME, 
	    	result.getCountry().getName());
	    verify(countryValidator, times(1))
	    	.processIsoAlpha2Code(anyString());
	    verify(createOrFindCountryResolver, times(1))
	    	.returnResultWithExistentCountry(anyString());
	}
}