package caio.portfolio.livraria.service.country;

import org.springframework.stereotype.Service;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;
import caio.portfolio.livraria.service.country.model.ResponseCountryDTOCreator;

@Service
public class ResponseCountryDTOCreatorImpl implements ResponseCountryDTOCreator {

	@Override
	public ResponseCountryDTO toResponseCountryDTO(Country country) {
		return ResponseCountryDTO.builder()
			.id(country.getId())
			.isoAlpha2Code(country.getIsoAlpha2Code())
			.name(country.getName())
			.build();
	}
}