package caio.portfolio.livraria.service.country;

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
		// TODO
		return null;
	}

	@Override
	public String resolveNameByIsoAlpha2Code(String isoAlpha2Code) {
		// TODO
		return null;
	}

}
