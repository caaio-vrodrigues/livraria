package caio.portfolio.livraria.infrastructure.entity.author.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateAuthorDTO {
	
	@NotBlank(message="O campo 'alias' não pode estar vazio")
	private final String alias;

	@NotBlank(message="O campo 'fullName' não pode estar vazio")
	private final String fullName;
	
	@NotNull(message="O campo 'birthday' não pode ser nulo")
	private final LocalDate birthday;
	
	@NotNull(message="O campo 'countryId' não pode ser nulo")
	private final Integer countryId;
}
