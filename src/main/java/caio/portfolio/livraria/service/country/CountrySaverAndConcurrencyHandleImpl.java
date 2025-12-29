package caio.portfolio.livraria.service.country;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.repository.CountryRepository;
import caio.portfolio.livraria.service.country.model.CountryExceptionCreator;
import caio.portfolio.livraria.service.country.model.CountrySaverAndConcurrencyHandle;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CountrySaverAndConcurrencyHandleImpl implements CountrySaverAndConcurrencyHandle {
	
	private final CountryRepository repo;
	private final CountryExceptionCreator countryExceptionCreator;

	@Override
	public Country saveAndDealingConcurrency(Country country) {
		try {
			return repo.saveAndFlush(country);
		}
		catch(DataIntegrityViolationException e) {
			Optional<Country> concurrentlyCreatedCountry = repo
				.findByIsoAlpha2Code(country.getIsoAlpha2Code());
			if(concurrentlyCreatedCountry.isEmpty()) 
				throw countryExceptionCreator
					.createConcurrentCountryException(country.getIsoAlpha2Code());
			return concurrentlyCreatedCountry.get();
		}
	}
}