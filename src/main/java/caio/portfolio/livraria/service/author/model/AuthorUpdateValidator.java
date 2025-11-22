package caio.portfolio.livraria.service.author.model;

import java.time.LocalDate;

import caio.portfolio.livraria.infrastructure.entity.country.Country;

public interface AuthorUpdateValidator {
	String validateAlias(String existingAlias, String aliasToUpdate);
	String validateFullName(String existingFullName, String fullNameToUpdate);
	LocalDate validateBirthday(LocalDate existingBirthday, LocalDate birthdayToUpdate);
	Country validateCountry(Country existingCountry, Integer countryIdToUpdate);
}
