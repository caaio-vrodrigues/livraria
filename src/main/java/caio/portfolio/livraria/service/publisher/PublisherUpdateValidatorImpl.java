package caio.portfolio.livraria.service.publisher;

import java.util.Optional;

import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.publisher.PublisherAlreadyExistsException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.repository.PublisherRepository;
import caio.portfolio.livraria.service.country.CountryService;
import caio.portfolio.livraria.service.publisher.model.PublisherUpdateValidator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublisherUpdateValidatorImpl implements PublisherUpdateValidator {
	
	private final PublisherRepository repo;
	private final CountryService countryService;
	
	@Override
	public String validateName(String currentName, String newName) {
		boolean containsFullNameAndIsDifferent = newName != null && !currentName.equals(newName);
		if(containsFullNameAndIsDifferent) return newName;
		return currentName;
	}

	@Override
	public Country validateCountry(Country currentCountry, Integer newCountryId) {
		boolean containsCountryIdAndIsDifferent = newCountryId != null && !currentCountry.getId().equals(newCountryId);
		if(containsCountryIdAndIsDifferent) return countryService.getCountryById(newCountryId);
		return currentCountry;
	}

	@Override
	public String validateFullAddress(String currentAddress, String newAddress) {
		boolean containsFullAddressAndIsDifferent = newAddress != null && !currentAddress.equals(newAddress);
		if(containsFullAddressAndIsDifferent) {
			Optional<Publisher> publisherOptionalByFullAddress = repo.findByFullAddress(newAddress); 
			if(publisherOptionalByFullAddress.isPresent()) throw new PublisherAlreadyExistsException("Falha na atualização, 'fullAddress': "+newAddress+" já está em uso pela editora: "+publisherOptionalByFullAddress.get().getName());
			return newAddress;
		}
		return currentAddress;
	}
}
