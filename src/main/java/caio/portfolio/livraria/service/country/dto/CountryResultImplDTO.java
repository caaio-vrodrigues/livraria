package caio.portfolio.livraria.service.country.dto;

import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;
import caio.portfolio.livraria.service.country.model.CountryResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CountryResultImplDTO implements CountryResult {
	
	private final ResponseCountryDTO country;
	private final boolean created;
	 
	@Override
	public boolean wasCreated() {
		return created;
	}
	
	@Override
	public boolean wasFound() {
		return !created;
	}
}