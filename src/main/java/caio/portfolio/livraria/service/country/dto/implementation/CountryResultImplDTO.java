package caio.portfolio.livraria.service.country.dto.implementation;

import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;
import caio.portfolio.livraria.service.country.dto.model.CountryResult;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
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