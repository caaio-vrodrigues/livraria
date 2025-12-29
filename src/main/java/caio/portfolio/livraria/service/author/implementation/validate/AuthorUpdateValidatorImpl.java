package caio.portfolio.livraria.service.author.implementation.validate;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Component;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import caio.portfolio.livraria.service.author.model.create.AuthorExceptionCreator;
import caio.portfolio.livraria.service.author.model.validate.AuthorUpdateValidator;
import caio.portfolio.livraria.service.country.CountryService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthorUpdateValidatorImpl implements AuthorUpdateValidator {
	
	private final AuthorRepository repo;
	private final AuthorExceptionCreator authorExceptionCreator;
	private final CountryService countryService;

	@Override
	public String validateAlias(String existingAlias, String aliasToUpdate) {
		boolean containsAliasAndIsDifferent = aliasToUpdate != null && 
			!existingAlias.equals(aliasToUpdate);
		if(containsAliasAndIsDifferent) {
			Optional<Author> authorUsingExpectedAlias = repo
				.findByAlias(aliasToUpdate);
			if(authorUsingExpectedAlias.isPresent()) throw authorExceptionCreator
				.createAuthorAlreadyExistsException(
					aliasToUpdate, 
					authorUsingExpectedAlias.get().getFullName());
			return aliasToUpdate;
		}
		return existingAlias;
	}

	@Override
	public String validateFullName(
		String existingFullName, 
		String fullNameToUpdate
	){
		boolean containsFullNameAndIsDifferent = fullNameToUpdate != null && 
			!existingFullName.equals(fullNameToUpdate);
		if(containsFullNameAndIsDifferent) return fullNameToUpdate;
		return existingFullName;
	}

	@Override
	public LocalDate validateBirthday(
		LocalDate existingBirthday, 
		LocalDate birthdayToUpdate
	){
		boolean containsBirthdayAndIsDifferent = birthdayToUpdate != null && 
			!existingBirthday.equals(birthdayToUpdate);
		if(containsBirthdayAndIsDifferent) return birthdayToUpdate;
		return existingBirthday;
	}

	@Override
	public Country validateCountry(
		Country existingCountry, 
		Integer countryIdToUpdate
	){
		boolean containsCountryIdAndIsDifferent = countryIdToUpdate != null &&
			!existingCountry.getId().equals(countryIdToUpdate);
		if(containsCountryIdAndIsDifferent) 
			return countryService.getCountryById(countryIdToUpdate);
		return existingCountry;
	}
}