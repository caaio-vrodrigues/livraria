package caio.portfolio.livraria.service.country;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.exception.custom.country.CountryNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.country.dto.CreateCountryDTO;
import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;
import caio.portfolio.livraria.infrastructure.repository.CountryRepository;
import caio.portfolio.livraria.service.country.dto.implementation.CountryResultImplDTO;

@DirtiesContext(classMode=DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.ANY)
class CountryServiceIntegrationTest {

	@Autowired private CountryService service;
	@Autowired private CountryRepository repo;
	
	private static final String BRAZIL_VALID_CODE = "BR";
	private static final String RAW_BRAZIL_CODE = "br ";
	private static final String UNITED_STATES_VALID_CODE = "US";
	private static final String BRAZIL_NAME = "Brazil";
	private static final String NON_EXISTENT_COUNTRY_CODE = "ir";
	private static final String INVALID_COUNTRY_CODE = "ur";
	
	private static final CreateCountryDTO CREATE_BRAZIL_DTO = CreateCountryDTO
		.builder()
	    .isoAlpha2Code(RAW_BRAZIL_CODE)
	    .build();
	
	@Test
	@Transactional(readOnly=true)
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Deve retornar país ao buscar com 'id' existente")
	void getCountryById_returnsCountry() {
		ResponseCountryDTO brazilRespDTO = service.getResponseCountryDTOById(1);
		assertNotNull(brazilRespDTO);
		assertEquals(
			BRAZIL_VALID_CODE, 
			brazilRespDTO.getIsoAlpha2Code());
	}
	
	@Test
	@Transactional(readOnly=true)
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Deve retornar 'CountryNotFoundException' ao buscar com 'id' inexistente")
	void getCountryById_returnsException() {
		assertThrows(
			CountryNotFoundException.class, 
			() -> service.getCountryById(1000)
		);
	}
	
	@Test
	@Transactional(readOnly=true)
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Deve retornar país ao buscar com 'isoAlpha2Code' existente")
	void getCountryByIsoAlpha2Code_returnsCountry() {
		ResponseCountryDTO result = service
			.getCountryByIsoAlpha2Code(RAW_BRAZIL_CODE);
		assertEquals(
			BRAZIL_VALID_CODE, 
			result.getIsoAlpha2Code());
		assertEquals(
			BRAZIL_NAME, 
			result.getName());
	}
	 
	@Test
	@Transactional(readOnly=true)
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Deve retornar 'CountryNotFoundException' ao buscar com 'isoAlpha2Code' não existente")
	void getCountryByIsoAlpha2Code_returnsException() {
	    assertThrows(
           CountryNotFoundException.class,
           () -> service.getCountryByIsoAlpha2Code(NON_EXISTENT_COUNTRY_CODE)
	    );
	}
	
	@Test
	@Transactional(readOnly=true)
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Deve retornar 'IllegalArgumentException' ao buscar com 'isoAlpha2Code' inválido")
	void getCountryByIsoAlpha2Code_returnsIllegalArgExcep() {
	    assertThrows(
           IllegalArgumentException.class,
           () -> service.getCountryByIsoAlpha2Code(INVALID_COUNTRY_CODE)
	    );
	}
	
	@Test
	@Transactional(readOnly=true)
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Deve retornar lista de dto's de países quando existem países cadastrados")
	void getAllCountries_returnsResponseCountryDTOList() {
		List<ResponseCountryDTO> result = service.getAllCountries();
		assertNotNull(result);
		assertEquals(10, result.size());
		assertEquals(
			BRAZIL_VALID_CODE, 
			result.get(0).getIsoAlpha2Code());
		assertEquals(
			UNITED_STATES_VALID_CODE, 
			result.get(1).getIsoAlpha2Code());
	}
	 
	@Test
	@Transactional(readOnly=true)
	@DisplayName("Deve retornar lista vazia quando não existem países cadastrados")
	void getAllCountries_returnsEmptyList() {
		List<ResponseCountryDTO> result = service.getAllCountries();
		assertNotNull(result);
		assertTrue(result.isEmpty());
		assertEquals(0, result.size());
	}
	
	@Test
	@Transactional
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Deve retornar país existente quando 'isoAlpha2Code' já está cadastrado")
	void createOrFindCountry_returnsExistingCountry() {
		CountryResultImplDTO result = service
			.createOrFindCountry(CREATE_BRAZIL_DTO);
	    assertNotNull(result);
	    assertEquals(
	    	BRAZIL_VALID_CODE, 
	    	result.getCountry().getIsoAlpha2Code());
	    assertEquals(
	    	BRAZIL_NAME, 
	    	result.getCountry().getName());
	    assertFalse(result.isCreated());
	}
	 
	@Test
	@Transactional
	@DisplayName("Deve criar novo país quando 'isoAlpha2Code' não existe")
	void createOrFindCountry_createsNewCountry() {
		long initialCount = repo.count();
		CountryResultImplDTO result = service
			.createOrFindCountry(CREATE_BRAZIL_DTO);
	    assertNotNull(result);
	    assertEquals(
	    	BRAZIL_VALID_CODE, 
	    	result.getCountry().getIsoAlpha2Code());
	    assertEquals(
	    	BRAZIL_NAME, 
	    	result.getCountry().getName());
	    assertTrue(result.isCreated());
	    assertEquals(initialCount + 1, repo.count());
	}
	 
	@Test
	@Transactional
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Não deve salvar quando país já existe")
	void createOrFindCountry_doesNotSaveWhenCountryExists() {
		long initialCount = repo.count();
		service.createOrFindCountry(CREATE_BRAZIL_DTO);
		assertEquals(initialCount, repo.count());
	}
}
