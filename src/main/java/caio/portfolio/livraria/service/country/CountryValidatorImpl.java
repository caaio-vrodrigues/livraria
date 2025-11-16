package caio.portfolio.livraria.service.country;

import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Service;

import caio.portfolio.livraria.service.country.model.CountryValidator;

@Service
public class CountryValidatorImpl implements CountryValidator {
	
	private static final Set<String> VALID_ISO_CODES = Set.of(Locale.getISOCountries());

	@Override
	public String processIsoAlpha2Code(String isoAlpha2Code) {
		boolean isIsoAlpha2CodeNullOrBlank = isoAlpha2Code == null || isoAlpha2Code.isBlank();
		if(isIsoAlpha2CodeNullOrBlank) throw new IllegalArgumentException("O campo 'isoAlpha2Code' não pode estar vazio");
		String normalizedIsoAlpha2Code = isoAlpha2Code.trim().toUpperCase();
		boolean validIsoAlpha2Code = VALID_ISO_CODES.contains(normalizedIsoAlpha2Code);
		if(!validIsoAlpha2Code) throw new IllegalArgumentException("O código '"+isoAlpha2Code+"' não corresponde a um código válido.");
		return normalizedIsoAlpha2Code;
	}

	@Override
	public String resolveNameByIsoAlpha2Code(String isoAlpha2Code) {
		String validIsoAlpha2Code = processIsoAlpha2Code(isoAlpha2Code);
		return getNameByValidatedAndNormalizedIsoAlpha2Code(validIsoAlpha2Code);
	}

	@Override
	public String getNameByValidatedAndNormalizedIsoAlpha2Code(String validIsoAlpha2Code) {
		Locale countryLocale = new Locale.Builder().setRegion(validIsoAlpha2Code).build();
		return countryLocale.getDisplayName(Locale.ENGLISH);
	}
}
