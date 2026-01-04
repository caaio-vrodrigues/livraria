package caio.portfolio.livraria.service.country.dto.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;

@ExtendWith(MockitoExtension.class)
class CountryResultImplDTOTest {
	
	private static final String BRAZIL_CODE = "br ";
	private static final String BRAZIL_NAME = "Brazil";
	private static final Integer BRAZIL_ID = 1;
	
	private static final ResponseCountryDTO RESPONSE_COUNTRY_DTO = ResponseCountryDTO.builder()
		.id(BRAZIL_ID)
		.isoAlpha2Code(BRAZIL_CODE)
		.name(BRAZIL_NAME)
		.build();
	
	@Test
	@DisplayName(value="Deve retornar 'false' para criação")
	void wasCreated_returnsFalse() {
		CountryResultImplDTO countryResultImplDTO = CountryResultImplDTO.builder()
			.country(RESPONSE_COUNTRY_DTO)
			.created(false)
			.build();
		Assertions.assertFalse(countryResultImplDTO.wasCreated());
	}
	
	@Test
	@DisplayName(value="Deve retornar 'true' para criação")
	void wasCreated_returnsTrue() {
		CountryResultImplDTO countryResultImplDTO = CountryResultImplDTO.builder()
			.country(RESPONSE_COUNTRY_DTO)
			.created(true)
			.build();
		Assertions.assertTrue(countryResultImplDTO.wasCreated());
	}
	
	@Test
	@DisplayName(value="Deve retornar 'true' para busca")
	void wasFound_returnsTrue() {
		CountryResultImplDTO countryResultImplDTO = CountryResultImplDTO.builder()
			.country(RESPONSE_COUNTRY_DTO)
			.created(false)
			.build();
		Assertions.assertTrue(countryResultImplDTO.wasFound());
	}
	
	@Test
	@DisplayName(value="Deve retornar 'false' para busca")
	void wasFound_returnsFalse() {
		CountryResultImplDTO countryResultImplDTO = CountryResultImplDTO.builder()
			.country(RESPONSE_COUNTRY_DTO)
			.created(true)
			.build();
		Assertions.assertFalse(countryResultImplDTO.wasFound());
	}
}