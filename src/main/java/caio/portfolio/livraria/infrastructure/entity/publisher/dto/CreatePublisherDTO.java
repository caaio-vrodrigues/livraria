package caio.portfolio.livraria.infrastructure.entity.publisher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePublisherDTO {

	@NotBlank(message ="O campo 'name' não pode estar vazio")
	private final String name;
	
	@NotNull(message ="O campo 'countryId' não pode ser nulo")
	private final Integer countryId;
	
	@NotBlank(message ="O campo 'fullAddress' não pode estar vazio")
	private final String fullAddress;
}