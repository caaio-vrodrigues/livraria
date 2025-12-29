package caio.portfolio.livraria.service.country.model.create;

import caio.portfolio.livraria.service.country.dto.implementation.CountryResultImplDTO;

public interface CreateOrFindCountryResolver {
	CountryResultImplDTO returnResultWithExistentCountry(String isoAlpha2Code);
	CountryResultImplDTO returnResultWithNewCountry(String isoAlpha2Code);
}