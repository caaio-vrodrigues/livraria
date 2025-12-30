package caio.portfolio.livraria.service.publisher.model.validate;

import caio.portfolio.livraria.infrastructure.entity.country.Country;

public interface PublisherUpdateValidator {
	String validateName(String currentName, String newName);
	Country validateCountry(Country currentCountry, Integer newCountryId);
	String validateFullAddress(String currentAddress, String newAddress);
}
