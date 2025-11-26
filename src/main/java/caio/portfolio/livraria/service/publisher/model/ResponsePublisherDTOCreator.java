package caio.portfolio.livraria.service.publisher.model;

import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.ResponsePublisherDTO;

public interface ResponsePublisherDTOCreator {
	ResponsePublisherDTO toResponsePublisherDTO(Publisher publisher);
}
