package caio.portfolio.livraria.infrastructure.entity.country.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import caio.portfolio.livraria.serialization.TrimmedStringDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCountryDTO {

	@JsonDeserialize(using=TrimmedStringDeserializer.class)
	@NotBlank(message="O campo 'isoAlpha2Code' não pode estar vazio")
	@Size(min=2, max=2)
	private final String isoAlpha2Code;
}