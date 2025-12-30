package caio.portfolio.livraria.service.publisher.model.find;

import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;

public interface PublisherFinder {
	Publisher findByFullAddress(String fullAddress);
	Publisher findById(Long id);
}