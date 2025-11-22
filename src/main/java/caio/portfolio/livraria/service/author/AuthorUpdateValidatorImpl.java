package caio.portfolio.livraria.service.author;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.service.author.model.AuthorUpdateValidator;

@Component
public class AuthorUpdateValidatorImpl implements AuthorUpdateValidator {

	@Override
	public String validateAlias(String existingAlias, String aliasToUpdate) {
		// TODO 
		return null;
	}

	@Override
	public String validateFullName(String existingFullName, String fullNameToUpdate) {
		// TODO  
		return null;
	}

	@Override
	public LocalDate validateBirthday(LocalDate existingBirthday, LocalDate birthdayToUpdate) {
		// TODO 
		return null;
	}

	@Override
	public Country validateCountry(Integer existingCountryId, Integer countryIdToUpdate) {
		// TODO 
		return null;
	}

}
