package caio.portfolio.livraria.service.country.model;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;

public interface ResponseCountryDTOCreator {
	ResponseCountryDTO toResponseCountryDTO(Country country);
}
