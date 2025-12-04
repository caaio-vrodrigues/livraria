package caio.portfolio.livraria.service.country.model;

import caio.portfolio.livraria.service.country.dto.CountryResultImplDTO;

public interface CreateOrFindCountryResolver {
	CountryResultImplDTO returnResultWithExistentCountry(String isoAlpha2Code);
	CountryResultImplDTO returnResultWithNewCountry(String isoAlpha2Code);
}