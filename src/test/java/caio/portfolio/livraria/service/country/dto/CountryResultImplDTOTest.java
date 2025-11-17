package caio.portfolio.livraria.service.country.dto;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;

class CountryResultImplDTOTest {

	private final ResponseCountryDTO dto = ResponseCountryDTO.builder()
		.id(1)
		.isoAlpha2Code("BR")
		.name("Brazil")
		.build();
	
	@Test
	@DisplayName("deve retornar true para país criado")
	void wasCreated_shouldCreateResultWhenCountryWasCreated() {
		CountryResultImplDTO result = CountryResultImplDTO.builder()
			.country(dto)
			.created(true)
			.build();
		assertThat(result.getCountry()).isNotNull();
		assertThat(result.wasCreated()).isTrue();
		assertThat(result.wasFound()).isFalse();
		assertThat(result.getCountry().getIsoAlpha2Code()).isEqualTo("BR");
	}
	
	@Test
	@DisplayName("deve retornar true para país encontrado")
	void wasFound_shouldCreateResultWhenCountryWasFound() {
		CountryResultImplDTO result = CountryResultImplDTO.builder()
			.country(dto)
			.created(false)
			.build();
		assertThat(result.getCountry()).isNotNull();
		assertThat(result.wasCreated()).isFalse();
		assertThat(result.wasFound()).isTrue();
		assertThat(result.getCountry().getName()).isEqualTo("Brazil");
	}
}
