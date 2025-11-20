package caio.portfolio.livraria.infrastructure.entity.author.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateAuthorDTO {

	@NotBlank(message="O campo 'fullName' não pode estar vazio")
	private String fullName;
	
	@NotNull(message="O campo 'birthday' não pode ser nulo")
	private LocalDate birthday;
	
	@NotNull(message="O campo 'countryId' não pode ser nulo")
	private Integer countryId;
}
