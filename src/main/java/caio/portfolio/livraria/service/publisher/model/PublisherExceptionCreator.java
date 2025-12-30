package caio.portfolio.livraria.service.publisher.model;

import caio.portfolio.livraria.exception.custom.publisher.ConcurrentPublisherException;

public interface PublisherExceptionCreator {
	ConcurrentPublisherException createConcurrentPublisherException(String publisherName);
}