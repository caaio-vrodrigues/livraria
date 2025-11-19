package caio.portfolio.livraria.service.country.dto;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;

class CountryResultImplDTOTest {
	
	private static final String BRAZIL_NAME = "Brazil";
	private static final String VALID_BRAZIL_CODE = "BR";
	private static final Integer BRAZIL_ID = 1;

	private static final ResponseCountryDTO RESPONSE_BRAZILDTO = ResponseCountryDTO.builder()
		.id(BRAZIL_ID)
		.isoAlpha2Code(VALID_BRAZIL_CODE)
		.name(BRAZIL_NAME)
		.build();
	
	@Test
	@DisplayName("deve retornar true para país criado")
	void wasCreated_shouldCreateResultWhenCountryWasCreated() {
		CountryResultImplDTO result = CountryResultImplDTO.builder()
			.country(RESPONSE_BRAZILDTO)
			.created(true)
			.build();
		assertThat(result.getCountry()).isNotNull();
		assertThat(result.wasCreated()).isTrue();
		assertThat(result.wasFound()).isFalse();
		assertThat(result.getCountry().getIsoAlpha2Code()).isEqualTo(VALID_BRAZIL_CODE);
	}
	
	@Test
	@DisplayName("deve retornar true para país encontrado")
	void wasFound_shouldCreateResultWhenCountryWasFound() {
		CountryResultImplDTO result = CountryResultImplDTO.builder()
			.country(RESPONSE_BRAZILDTO)
			.created(false)
			.build();
		assertThat(result.getCountry()).isNotNull();
		assertThat(result.wasCreated()).isFalse();
		assertThat(result.wasFound()).isTrue();
		assertThat(result.getCountry().getName()).isEqualTo(BRAZIL_NAME);
	}
}