package caio.portfolio.livraria.infrastructure.entity.publisher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UpdatePublisherDTO {

	private String name;
	private Integer countryId;
	private String fullAddress;
}
