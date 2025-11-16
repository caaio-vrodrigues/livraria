package caio.portfolio.livraria.infrastructure.entity.country.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import caio.portfolio.livraria.serialization.TrimmedStringDeserializer;
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

	@JsonDeserialize(using=TrimmedStringDeserializer.class)
	@NotBlank(message="O campo 'isoAlpha2Code' não pode estar vazio")
	@Size(min=2, max=2)
	private String isoAlpha2Code;
}