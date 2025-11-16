package caio.portfolio.livraria.service.country;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.country.dto.CreateCountryDTO;
import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;
import caio.portfolio.livraria.infrastructure.repository.CountryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryService {

	private final CountryRepository repo;
	
	public ResponseCountryDTO createCountry(CreateCountryDTO createCountryDTO) {
		// to-do
		return null;
	}
	
	@Transactional(readOnly=true)
	public List<Country> getAllCountries(){
		return repo.findAll();
	}
}