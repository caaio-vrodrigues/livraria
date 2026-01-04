package caio.portfolio.livraria.service.country.implementation.create;

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
	
	private static final String BRAZIL_NAME = "Brazil";
	private static final String VALID_BRAZIL_CODE = "BR";
	private static final Integer BRAZIL_ID = 1;

	private static final Country BRAZIL = Country.builder()
		.id(BRAZIL_ID)
		.isoAlpha2Code(VALID_BRAZIL_CODE)
		.name(BRAZIL_NAME)
		.build();
	
	@Test
	@DisplayName("Deve receber um 'Country' e converte-lo em DTO de resposta para então retorna-lo")
	void toResponseCountryDTO_worksCorrectly() {
		ResponseCountryDTO dto = responseCountryDTOCreatorImpl.toResponseCountryDTO(BRAZIL);
		assertThat(dto).isNotNull();
		assertThat(dto.getIsoAlpha2Code()).isEqualTo(VALID_BRAZIL_CODE);
		assertThat(dto.getName()).isEqualTo(BRAZIL_NAME);
		assertThat(dto.getId()).isEqualTo(BRAZIL_ID);
	}
}
