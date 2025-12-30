package caio.portfolio.livraria.service.publisher.model.create;

import caio.portfolio.livraria.exception.custom.publisher.ConcurrentPublisherException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherNotFoundException;

public interface PublisherExceptionCreator {
	ConcurrentPublisherException createConcurrentPublisherException(String publisherName);
	PublisherAlreadyExistsException createPublisherAlreadyExistsException(String fullAddress);
	PublisherNotFoundException createPublisherNotFoundException(Long id);
}