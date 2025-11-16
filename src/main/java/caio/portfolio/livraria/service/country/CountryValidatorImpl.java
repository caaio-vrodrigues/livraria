package caio.portfolio.livraria.service.country;

import java.util.Arrays;
import java.util.Locale;

import org.springframework.stereotype.Service;

import caio.portfolio.livraria.infrastructure.entity.country.dto.CreateCountryDTO;
import caio.portfolio.livraria.service.country.model.CountryValidator;

@Service
public class CountryValidatorImpl implements CountryValidator {

	@Override
	public void validateProperties(CreateCountryDTO dto) {
		
	}

	@Override
	public String processIsoAlpha2Code(String isoAlpha2Code) {
		boolean isIsoAlpha2CodeEmptyOrBlank = isoAlpha2Code.trim().isEmpty() || 
			isoAlpha2Code.trim().isBlank();
		
		if(isIsoAlpha2CodeEmptyOrBlank) throw new IllegalArgumentException("O campo 'isoAlpha2Code' não pode estar vazio");
		
		String normalizedIsoAlpha2Code = isoAlpha2Code.toUpperCase();
		boolean validIsoAlpha2Code = Arrays.asList(Locale.getISOCountries()).contains(normalizedIsoAlpha2Code);
		if(!validIsoAlpha2Code) throw new IllegalArgumentException("O código '"+isoAlpha2Code+"' não corresponde a um código válido.");
		  
		return null;
	}

	@Override
	public String resolveNameByIsoAlpha2Code(String isoAlpha2Code) {
		// TODO
		return null;
	}

}
