package caio.portfolio.livraria.service.publisher.implementation.create;

import org.springframework.stereotype.Component;

import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.ResponsePublisherDTO;
import caio.portfolio.livraria.service.publisher.model.create.ResponsePublisherDTOCreator;

@Component
public class ResponsePublisherDTOCreatorImpl implements ResponsePublisherDTOCreator {

	@Override
	public ResponsePublisherDTO toResponsePublisherDTO(Publisher publisher) {
		return ResponsePublisherDTO.builder()
			.id(publisher.getId())
			.name(publisher.getName())
			.countryId(publisher.getCountry().getId())
			.fullAddress(publisher.getFullAddress())
			.build();
	}
}