package caio.portfolio.livraria.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.infrastructure.entity.Country;
import caio.portfolio.livraria.infrastructure.repository.CountryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryService {

	private final CountryRepository repo;
	
	@Transactional(readOnly=true)
	public List<Country> getAllCountries(){
		return repo.findAll();
	}
}