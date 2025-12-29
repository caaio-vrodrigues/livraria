package caio.portfolio.livraria.service.country;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.country.dto.CreateCountryDTO;
import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;
import caio.portfolio.livraria.infrastructure.repository.CountryRepository;
import caio.portfolio.livraria.service.country.dto.CountryResultImplDTO;
import caio.portfolio.livraria.service.country.model.CountryFinder;
import caio.portfolio.livraria.service.country.model.CountryValidator;
import caio.portfolio.livraria.service.country.model.CreateOrFindCountryResolver;
import caio.portfolio.livraria.service.country.model.ResponseCountryDTOCreator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryService {

	private final CountryRepository repo;
	private final CountryValidator countryValidator;
	private final ResponseCountryDTOCreator responseCountryDTOCreator;
	private final CreateOrFindCountryResolver createOrFindCountryResolver;
	private final CountryFinder countryFinder;
	
	@Transactional
	public CountryResultImplDTO createOrFindCountry(CreateCountryDTO createCountryDTO) {
		String validIsoAlpha2Code = countryValidator
			.processIsoAlpha2Code(createCountryDTO.getIsoAlpha2Code());
		CountryResultImplDTO resullWithExistentCountry = createOrFindCountryResolver
			.returnResultWithExistentCountry(validIsoAlpha2Code);
		if(resullWithExistentCountry != null) return resullWithExistentCountry;
		return createOrFindCountryResolver
			.returnResultWithNewCountry(validIsoAlpha2Code);
	}
	
	@Transactional(readOnly=true)
	public List<ResponseCountryDTO> getAllCountries(){
		return repo.findAll().stream()
			.map(responseCountryDTOCreator::toResponseCountryDTO)
			.toList();
	}
	
	@Transactional(readOnly=true)
	public ResponseCountryDTO getCountryByIsoAlpha2Code(String isoAlpha2Code) {
		String validIsoAlpha2Code = countryValidator.processIsoAlpha2Code(isoAlpha2Code);
		Country country = countryFinder.findByIsoAlpha2Code(validIsoAlpha2Code);
		return responseCountryDTOCreator.toResponseCountryDTO(country);
	}
	
	@Transactional(readOnly=true)
	public ResponseCountryDTO getResponseCountryDTOById(Integer id){
		Country country = countryFinder.findById(id);
		return responseCountryDTOCreator.toResponseCountryDTO(country);
	}
	
	@Transactional(readOnly=true)
	public Country getCountryById(Integer id) {
		return countryFinder.findById(id);
	}
}