package caio.portfolio.livraria.service.country;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.country.dto.CreateCountryDTO;
import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;
import caio.portfolio.livraria.infrastructure.repository.CountryRepository;
import caio.portfolio.livraria.service.country.dto.CountryResultImplDTO;
import caio.portfolio.livraria.service.country.model.CountryValidator;
import caio.portfolio.livraria.service.country.model.ResponseCountryDTOCreator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryService {

	private final CountryRepository repo;
	private final CountryValidator countryValidator;
	private final ResponseCountryDTOCreator responseCountryDTOCreator;
	
	@Transactional
	public CountryResultImplDTO createOrFindCountry(CreateCountryDTO createCountryDTO) {
		String validIsoAlpha2Code = countryValidator.processIsoAlpha2Code(createCountryDTO.getIsoAlpha2Code());
		Optional<Country> countryOptional = repo.findByIsoAlpha2Code(validIsoAlpha2Code);
		boolean countryAlreadyExists = countryOptional.isPresent();
		if(countryAlreadyExists) {
			ResponseCountryDTO respCountryDTO = responseCountryDTOCreator.toResponseCountryDTO(countryOptional.get());
			return CountryResultImplDTO.builder()
				.country(respCountryDTO)
				.created(false)
				.build();
		}
		String validCountryName = countryValidator.getNameByValidatedAndNormalizedIsoAlpha2Code(validIsoAlpha2Code);
		Country newCountry = Country.builder()
			.isoAlpha2Code(validIsoAlpha2Code)
			.name(validCountryName)
			.build();
		repo.saveAndFlush(newCountry);
		ResponseCountryDTO respCountryDTO = responseCountryDTOCreator.toResponseCountryDTO(newCountry);
		return CountryResultImplDTO.builder()
			.country(respCountryDTO)
			.created(true)
			.build();
	}
	
	@Transactional(readOnly=true)
	public List<ResponseCountryDTO> getAllCountries(){
		return repo.findAll().stream()
			.map(responseCountryDTOCreator::toResponseCountryDTO)
			.toList();
	}
}