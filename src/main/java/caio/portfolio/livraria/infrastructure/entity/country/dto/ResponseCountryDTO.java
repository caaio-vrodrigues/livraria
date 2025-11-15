package caio.portfolio.livraria.infrastructure.entity.country.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class ResponseCountryDTO {

	@NotNull(message="O campo 'id' não pode estar nulo")
	private Integer id;
	
	@Size(min=2, max=2)
	@NotEmpty(message="O campo 'isoAlpha2Code' não pode estar vazio")
	private String isoAlpha2Code;
	
	@NotEmpty(message="O campo 'name' não pode estar vazio")
	private String name;
}
