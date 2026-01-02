package caio.portfolio.livraria.infrastructure.entity.author.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import caio.portfolio.livraria.serialization.LocalDateDesserializer;
import caio.portfolio.livraria.serialization.TrimmedStringDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateAuthorDTO {
	
	@NotBlank(message="{alias.notBlank}")
	@JsonDeserialize(using=TrimmedStringDeserializer.class)
	private final String alias;

	@NotBlank(message="{fullName.notBlank}")
	@JsonDeserialize(using=TrimmedStringDeserializer.class)
	private final String fullName;
	
	@NotNull(message="{birthday.notNull}")
	@JsonDeserialize(using=LocalDateDesserializer.class)
	private final LocalDate birthday;
	
	@NotNull(message="{countryId.notNull}")
	@Positive(message="{countryId.grather.than.zero}")
	private final Integer countryId;
}