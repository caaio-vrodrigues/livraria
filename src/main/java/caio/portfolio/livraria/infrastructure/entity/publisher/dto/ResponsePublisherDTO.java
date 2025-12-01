package caio.portfolio.livraria.infrastructure.entity.publisher.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponsePublisherDTO {

	private final Long id;
	private final String name;
	private final Integer countryId;
	private final String fullAddress;
}