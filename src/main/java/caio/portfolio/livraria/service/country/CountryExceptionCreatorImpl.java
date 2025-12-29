package caio.portfolio.livraria.service.country;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.country.ConcurrentCountryException;
import caio.portfolio.livraria.service.country.model.CountryExceptionCreator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CountryExceptionCreatorImpl implements CountryExceptionCreator {
	
	private final MessageSource countryMessageSource;
	
	@Override
	public ConcurrentCountryException createConcurrentCountryException(String isoAlpha2Code) {
		return new ConcurrentCountryException(
			countryMessageSource.getMessage(
				"concurret.country.iso", 
				new Object[] {isoAlpha2Code},
				LocaleContextHolder.getLocale())
			);
	}
}