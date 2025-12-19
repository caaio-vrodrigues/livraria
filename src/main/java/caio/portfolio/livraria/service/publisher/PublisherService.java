package caio.portfolio.livraria.service.publisher;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.exception.custom.publisher.PublisherAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.CreatePublisherDTO;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.ResponsePublisherDTO;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.UpdatePublisherDTO;
import caio.portfolio.livraria.infrastructure.repository.PublisherRepository;
import caio.portfolio.livraria.service.country.CountryService;
import caio.portfolio.livraria.service.publisher.model.PublisherSaverAndConcurrencyHandle;
import caio.portfolio.livraria.service.publisher.model.PublisherUpdateValidator;
import caio.portfolio.livraria.service.publisher.model.ResponsePublisherDTOCreator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublisherService {

	private final PublisherRepository repo;
	private final ResponsePublisherDTOCreator responsePublisherDTOCreator;
	private final PublisherUpdateValidator publisherUpdateValidator;
	private final PublisherSaverAndConcurrencyHandle publisherSaverAndConcurrencyHandle;
	private final CountryService countryService;

	@Transactional
	public ResponsePublisherDTO createPublisher(CreatePublisherDTO dto) {
		Optional<Publisher> existingPublisherOptional = repo.findByFullAddress(dto.getFullAddress());
		if(existingPublisherOptional.isPresent()) 
			throw new PublisherAlreadyExistsException("Não foi possível criar nova editora. Editora com 'fullAddress': "+dto.getFullAddress()+" já existe");
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
	public ResponsePublisherDTO getResponsePublisherDTOByFullAddress(String fullAddress) {
		return responsePublisherDTOCreator
			.toResponsePublisherDTO(repo.findByFullAddress(fullAddress).orElseThrow(() ->
				new PublisherNotFoundException("Não possível encontrar uma editora com 'fullAddress': "+fullAddress)));
	}

	@Transactional(readOnly=true)
	public ResponsePublisherDTO getResponsePublisherDTOById(Long id) {
		return responsePublisherDTOCreator
			.toResponsePublisherDTO(repo.findById(id).orElseThrow(() -> 
				new PublisherNotFoundException("Não possível encontrar uma editora com 'id': "+id)));
	}

	@Transactional
	public ResponsePublisherDTO updatePublisher(Long id, UpdatePublisherDTO dto) {
		Publisher currentPublisher = repo.findById(id).orElseThrow(() ->
			new PublisherNotFoundException("Não possível encontrar uma editora com 'id': "+id));
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
		return repo.findById(id).orElseThrow(() -> 
			new PublisherNotFoundException("Não possível encontrar uma editora com 'id': "+id));
	}

	public Boolean deletePublisherById(Long id) {
		// TODO
		return null;
	}
}