package caio.portfolio.livraria.service.country.implementation.find;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.country.CountryNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.repository.CountryRepository;
import caio.portfolio.livraria.service.country.model.find.CountryFinder;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CountryFinderImpl implements CountryFinder {
	
	private final CountryRepository repo;
	private final MessageSource countryMessageSource;
	
	@Override
	public Country findByIsoAlpha2Code(String isoAlpha2Code) {
		String msg = countryMessageSource.getMessage(
			"country.not.found.iso", 
			new Object[]{isoAlpha2Code},
			LocaleContextHolder.getLocale());
		return repo.findByIsoAlpha2Code(isoAlpha2Code)
			.orElseThrow(() -> new CountryNotFoundException(msg));
	}

	@Override
	public Country findById(Integer id) {
		String msg = countryMessageSource.getMessage(
			"country.not.found.id", 
			new Object[]{id},
			LocaleContextHolder.getLocale());
		return repo.findById(id)
			.orElseThrow(() -> new CountryNotFoundException(msg));
	}
}