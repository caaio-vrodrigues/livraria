package caio.portfolio.livraria.service.country.implementation.create;

import java.util.Optional;

import org.springframework.stereotype.Component;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;
import caio.portfolio.livraria.infrastructure.repository.CountryRepository;
import caio.portfolio.livraria.service.country.dto.implementation.CountryResultImplDTO;
import caio.portfolio.livraria.service.country.model.create.CreateOrFindCountryResolver;
import caio.portfolio.livraria.service.country.model.create.ResponseCountryDTOCreator;
import caio.portfolio.livraria.service.country.model.save.CountrySaverAndConcurrencyHandle;
import caio.portfolio.livraria.service.country.model.validate.CountryValidator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateOrFindCountryResolverImpl implements CreateOrFindCountryResolver {
	
	private final CountryRepository repo;
	private final CountryValidator countryValidator;
	private final ResponseCountryDTOCreator responseCountryDTOCreator;
	private final CountrySaverAndConcurrencyHandle saverAndConcurrencyHandleImpl;

	@Override
	public CountryResultImplDTO returnResultWithExistentCountry(String isoAlpha2Code) {
		Optional<Country> countryOptional = repo.findByIsoAlpha2Code(isoAlpha2Code);
		boolean countryAlreadyExists = countryOptional.isPresent();
		if(countryAlreadyExists) {
			ResponseCountryDTO respCountryDTO = responseCountryDTOCreator
				.toResponseCountryDTO(countryOptional.get());
			return CountryResultImplDTO.builder()
				.country(respCountryDTO)
				.created(false)
				.build();
		}
		return null;
	}
	
	@Override
	public CountryResultImplDTO returnResultWithNewCountry(String isoAlpha2Code) {
		String validCountryName = countryValidator
			.getNameByValidatedAndNormalizedIsoAlpha2Code(isoAlpha2Code);
		Country newCountry = Country.builder()
			.isoAlpha2Code(isoAlpha2Code)
			.name(validCountryName)
			.build();
		newCountry = saverAndConcurrencyHandleImpl.saveAndDealingConcurrency(newCountry);
		ResponseCountryDTO respCountryDTO = responseCountryDTOCreator
			.toResponseCountryDTO(newCountry);
		return CountryResultImplDTO.builder()
			.country(respCountryDTO)
			.created(true)
			.build();
	}

}