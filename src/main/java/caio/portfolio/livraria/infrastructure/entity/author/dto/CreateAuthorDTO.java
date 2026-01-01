package caio.portfolio.livraria.infrastructure.entity.author.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateAuthorDTO {
	
	@NotBlank(message="{alias.notBlank}")
	private final String alias;

	@NotBlank(message="{fullName.notBlank}")
	private final String fullName;
	
	@NotNull(message="{birthday.notNull}")
	private final LocalDate birthday;
	
	@NotNull(message="{countryId.notNull}")
	@Positive(message="{countryId.grather.than.zero}")
	private final Integer countryId;
}