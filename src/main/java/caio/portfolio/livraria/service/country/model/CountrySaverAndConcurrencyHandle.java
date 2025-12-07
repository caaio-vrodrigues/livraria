package caio.portfolio.livraria.service.country.model;

import caio.portfolio.livraria.infrastructure.entity.country.Country;

public interface CountrySaverAndConcurrencyHandle {
	Country saveAndDealingConcurrency(Country country);
}