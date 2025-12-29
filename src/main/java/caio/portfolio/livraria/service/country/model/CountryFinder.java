package caio.portfolio.livraria.service.country.model;

import caio.portfolio.livraria.infrastructure.entity.country.Country;

public interface CountryFinder {
	Country findByIsoAlpha2Code(String isoAlpha2Code);
	Country findById(Integer id);
}