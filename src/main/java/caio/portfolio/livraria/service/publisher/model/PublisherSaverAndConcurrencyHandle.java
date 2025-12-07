package caio.portfolio.livraria.service.publisher.model;

import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;

public interface PublisherSaverAndConcurrencyHandle {
	Publisher saveAndHandlePublisherConcurrency(Publisher publisher);
}
