package caio.portfolio.livraria.service.country;

import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Service;

import caio.portfolio.livraria.service.country.model.CountryExceptionCreator;
import caio.portfolio.livraria.service.country.model.CountryValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryValidatorImpl implements CountryValidator {
	
	private final Set<String> validIsoCodes;
	private final CountryExceptionCreator countryExceptionCreator;

	@Override
	public String processIsoAlpha2Code(String isoAlpha2Code) {
		boolean isIsoAlpha2CodeNullOrBlank = isoAlpha2Code == null || isoAlpha2Code.isBlank();
		if(isIsoAlpha2CodeNullOrBlank) throw countryExceptionCreator
			.createIllegalArgumentExceptionByBlank();
		String normalizedIsoAlpha2Code = isoAlpha2Code.trim().toUpperCase();
		boolean validIsoAlpha2Code = validIsoCodes.contains(normalizedIsoAlpha2Code);
		if(!validIsoAlpha2Code) throw countryExceptionCreator
			.createIllegalArgumentExceptionByInvalid(normalizedIsoAlpha2Code);
		return normalizedIsoAlpha2Code;
	}

	@Override
	public String resolveNameByIsoAlpha2Code(String isoAlpha2Code) {
		return getNameByValidatedAndNormalizedIsoAlpha2Code(processIsoAlpha2Code(isoAlpha2Code));
	}

	@Override
	public String getNameByValidatedAndNormalizedIsoAlpha2Code(String validIsoAlpha2Code) {
		return new Locale.Builder()
			.setRegion(validIsoAlpha2Code)
			.build()
			.getDisplayName(Locale.ENGLISH);
	}
}
