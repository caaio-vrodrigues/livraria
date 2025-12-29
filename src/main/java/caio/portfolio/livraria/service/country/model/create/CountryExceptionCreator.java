package caio.portfolio.livraria.service.country.model.create;

import caio.portfolio.livraria.exception.custom.country.ConcurrentCountryException;

public interface CountryExceptionCreator {
	ConcurrentCountryException createConcurrentCountryException(String isoAlpha2Code);
	IllegalArgumentException createIllegalArgumentExceptionByBlank();
	IllegalArgumentException createIllegalArgumentExceptionByInvalid(String isoAlpha2Code);
}