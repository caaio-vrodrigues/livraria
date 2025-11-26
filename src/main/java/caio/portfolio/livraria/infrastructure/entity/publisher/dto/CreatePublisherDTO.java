package caio.portfolio.livraria.infrastructure.entity.publisher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CreatePublisherDTO {

	@NotBlank(message ="O campo 'name' não pode estar vazio")
	private String name;
	
	@NotNull(message ="O campo 'countryId' não pode ser nulo")
	private Integer countryId;
	
	@NotBlank(message ="O campo 'fullAddress' não pode estar vazio")
	private String fullAddress;
}