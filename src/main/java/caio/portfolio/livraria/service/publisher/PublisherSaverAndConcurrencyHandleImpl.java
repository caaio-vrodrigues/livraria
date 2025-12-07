package caio.portfolio.livraria.service.publisher;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.publisher.ConcurrentPublisherException;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.repository.PublisherRepository;
import caio.portfolio.livraria.service.publisher.model.PublisherSaverAndConcurrencyHandle;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublisherSaverAndConcurrencyHandleImpl implements PublisherSaverAndConcurrencyHandle {
	
	private final PublisherRepository repo;
	
	@Override
	public Publisher saveAndHandlePublisherConcurrency(Publisher publisher) {
		try {
			return repo.saveAndFlush(publisher);
		}
		catch(DataIntegrityViolationException e) {
			Optional<Publisher> publisherOptional = repo
				.findByFullAddress(publisher.getFullAddress());
			if(publisherOptional.isPresent()) 
				return publisherOptional.get();
			throw new ConcurrentPublisherException("Falha ao tentar criar nova editora com 'name': "+publisher.getName()+"; 'fullAddress': "+publisher.getFullAddress()+"; 'countryId': "+publisher.getCountry());
		}
	}
}
