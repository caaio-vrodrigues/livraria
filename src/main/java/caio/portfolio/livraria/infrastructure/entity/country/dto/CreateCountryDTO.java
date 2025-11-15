package caio.portfolio.livraria.infrastructure.entity.country.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateCountryDTO {

	@NotBlank(message="O campo 'isoAlpha2Code' não pode estar vazio")
	@Size(min=2, max=2)
	private String isoAlpha2Code;
}