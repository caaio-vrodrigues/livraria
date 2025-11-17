package caio.portfolio.livraria.service.country;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;

@ExtendWith(MockitoExtension.class)
class ResponseCountryDTOCreatorImplTest {
	
	@InjectMocks private ResponseCountryDTOCreatorImpl responseCountryDTOCreatorImpl;

	Country brazil = Country.builder()
		.id(1)
		.isoAlpha2Code("BR")
		.name("Brazil")
		.build();
	
	@Test
	@DisplayName("Deve receber um 'Country' e converte-lo em DTO de resposta para então retorna-lo")
	void toResponseCountryDTO_worksCorrectly() {
		ResponseCountryDTO dto = responseCountryDTOCreatorImpl.toResponseCountryDTO(brazil);
		assertThat(dto).isNotNull();
		assertThat(dto.getIsoAlpha2Code()).isEqualTo("BR");
		assertThat(dto.getName()).isEqualTo("Brazil");
		assertThat(dto.getId()).isEqualTo(1);
	}
}
