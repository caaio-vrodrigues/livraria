package caio.portfolio.livraria.service.publisher;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.CreatePublisherDTO;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.ResponsePublisherDTO;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.UpdatePublisherDTO;
import caio.portfolio.livraria.infrastructure.repository.PublisherRepository;
import caio.portfolio.livraria.service.country.CountryService;
import caio.portfolio.livraria.service.publisher.model.create.PublisherExceptionCreator;
import caio.portfolio.livraria.service.publisher.model.create.ResponsePublisherDTOCreator;
import caio.portfolio.livraria.service.publisher.model.find.PublisherFinder;
import caio.portfolio.livraria.service.publisher.model.save.PublisherSaverAndConcurrencyHandle;
import caio.portfolio.livraria.service.publisher.model.validate.PublisherUpdateValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublisherService {

	private final PublisherRepository repo;
	private final ResponsePublisherDTOCreator responsePublisherDTOCreator;
	private final PublisherUpdateValidator publisherUpdateValidator;
	private final PublisherSaverAndConcurrencyHandle publisherSaverAndConcurrencyHandle;
	private final PublisherExceptionCreator publisherExceptionCreator;
	private final PublisherFinder publisherFinder;
	private final CountryService countryService;

	@Transactional
	public ResponsePublisherDTO createPublisher(CreatePublisherDTO dto) {
		Optional<Publisher> existingPublisherOptional = repo
			.findByFullAddress(dto.getFullAddress());
		if(existingPublisherOptional.isPresent()) 
			throw publisherExceptionCreator
				.createPublisherAlreadyExistsException(dto.getFullAddress());
		Publisher newPublisher = Publisher.builder()
			.name(dto.getName())
			.fullAddress(dto.getFullAddress())
			.country(countryService.getCountryById(dto.getCountryId()))
			.build();
		return responsePublisherDTOCreator
			.toResponsePublisherDTO(publisherSaverAndConcurrencyHandle
				.saveAndHandlePublisherConcurrency(newPublisher));
	}

	@Transactional(readOnly=true)
	public List<ResponsePublisherDTO> getAllResponsePublisherDTOs() {
		return repo.findAll().stream()
			.map(responsePublisherDTOCreator::toResponsePublisherDTO).toList();
	}
	
	@Transactional(readOnly=true)
	public ResponsePublisherDTO getResponsePublisherDTOByFullAddress(
		String fullAddress
	){
		return responsePublisherDTOCreator
			.toResponsePublisherDTO(publisherFinder
				.findByFullAddress(fullAddress));
	}

	@Transactional(readOnly=true)
	public ResponsePublisherDTO getResponsePublisherDTOById(Long id) {
		return responsePublisherDTOCreator
			.toResponsePublisherDTO(publisherFinder.findById(id));
	}

	@Transactional
	public ResponsePublisherDTO updatePublisher(
		Long id, 
		UpdatePublisherDTO dto
	){
		Publisher currentPublisher = publisherFinder.findById(id);
		Publisher updatedPublisher = Publisher.builder()
			.id(currentPublisher.getId())
			.name(publisherUpdateValidator.validateName(
				currentPublisher.getName(), 
				dto.getName()))
			.country(publisherUpdateValidator.validateCountry(
				currentPublisher.getCountry(), 
				dto.getCountryId()))
			.fullAddress(publisherUpdateValidator.validateFullAddress(
				currentPublisher.getFullAddress(), 
				dto.getFullAddress()))
			.build();
		return responsePublisherDTOCreator
			.toResponsePublisherDTO(publisherSaverAndConcurrencyHandle
				.saveAndHandlePublisherConcurrency(updatedPublisher));
	}
	
	@Transactional(readOnly=true)
	public Publisher getPublisherById(Long id) {
		return publisherFinder.findById(id);
	}

	public Boolean deletePublisherById(Long id) {
		if(!repo.existsById(id)) 
			throw publisherExceptionCreator
				.createPublisherNotFoundException(id);
		repo.deleteById(id);
		return true;
	}
}