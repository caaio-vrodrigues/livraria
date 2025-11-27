package caio.portfolio.livraria.service.publisher;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import caio.portfolio.livraria.exception.custom.publisher.ConcurrentPublisherException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherAlreadyExistsException;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.CreatePublisherDTO;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.ResponsePublisherDTO;
import caio.portfolio.livraria.infrastructure.repository.PublisherRepository;
import caio.portfolio.livraria.service.country.CountryService;
import caio.portfolio.livraria.service.publisher.model.ResponsePublisherDTOCreator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublisherService {

	private final PublisherRepository repo;
	private final CountryService countryService;
	private final ResponsePublisherDTOCreator responsePublisherDTOCreator;
	
	private Publisher saveAndHandlePublisherConcurrency(Publisher publisher) {
		try {
			return repo.saveAndFlush(publisher);
		}
		catch(DataIntegrityViolationException e) {
			Optional<Publisher> publisherOptional = repo.findByFullAddress(publisher.getFullAddress());
			if(publisherOptional.isPresent()) return publisherOptional.get();
			throw new ConcurrentPublisherException("Falha ao tentar criar nova editora com 'name': "+publisher.getName()+"; 'fullAddress': "+publisher.getFullAddress()+"; 'countryId': "+publisher.getCountry());
		}
	}

	public ResponsePublisherDTO createPublisher(CreatePublisherDTO dto) {
		Optional<Publisher> existingPublisher = repo.findByFullAddress(dto.getFullAddress());
		if(existingPublisher.isPresent()) throw new PublisherAlreadyExistsException("Não foi possível criar nova editora. Editora com 'fullAddress': "+dto.getFullAddress()+" já existe");
		Publisher newPublisher = Publisher.builder()
			.name(dto.getName())
			.fullAddress(dto.getFullAddress())
			.country(countryService.getCountryById(dto.getCountryId()))
			.build();
		newPublisher = saveAndHandlePublisherConcurrency(newPublisher);
		return responsePublisherDTOCreator.toResponsePublisherDTO(newPublisher);
	}

	public List<ResponsePublisherDTO> getAllPublishers() {
		return repo.findAll().stream()
			.map(responsePublisherDTOCreator::toResponsePublisherDTO).toList();
	}
}