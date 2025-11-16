package caio.portfolio.livraria.service.country;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.country.dto.CreateCountryDTO;
import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;
import caio.portfolio.livraria.infrastructure.repository.CountryRepository;
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
	public ResponseCountryDTO createOrFindCountry(CreateCountryDTO createCountryDTO) {
		String validIsoAlpha2Code = countryValidator.processIsoAlpha2Code(createCountryDTO.getIsoAlpha2Code());
		Optional<Country> countryOptional = repo.findByIsoAlpha2Code(validIsoAlpha2Code);
		boolean countryAlreadyExists = countryOptional.isPresent();
		if(countryAlreadyExists) return responseCountryDTOCreator.toResponseCountryDTO(countryOptional.get());
		String validCountryName = countryValidator.getNameByValidatedAndNormalizedIsoAlpha2Code(validIsoAlpha2Code);
		Country newCountry = Country.builder()
			.isoAlpha2Code(validIsoAlpha2Code)
			.name(validCountryName)
			.build();
		repo.saveAndFlush(newCountry);
		return responseCountryDTOCreator.toResponseCountryDTO(newCountry);
	}
	
	@Transactional(readOnly=true)
	public List<ResponseCountryDTO> getAllCountries(){
		return repo.findAll().stream()
			.map(responseCountryDTOCreator::toResponseCountryDTO)
			.toList();
	}
}