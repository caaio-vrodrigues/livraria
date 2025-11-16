package caio.portfolio.livraria.service.country.model;

import caio.portfolio.livraria.infrastructure.entity.country.dto.CreateCountryDTO;

public interface CountryValidator {
	void validateProperties(CreateCountryDTO dto);
}