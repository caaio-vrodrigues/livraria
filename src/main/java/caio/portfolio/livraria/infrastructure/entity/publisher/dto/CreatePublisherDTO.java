package caio.portfolio.livraria.infrastructure.entity.publisher.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import caio.portfolio.livraria.serialization.TrimmedStringDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePublisherDTO {

	@NotBlank(message="{name.notBlank}")
	@JsonDeserialize(using=TrimmedStringDeserializer.class)
	private final String name;
	
	@NotNull(message="{countryId.notNull}")
	@Positive(message="{countryId.grather.than.zero}")
	private final Integer countryId;
	
	@NotBlank(message="{fullAddress.notBlank}")
	@JsonDeserialize(using=TrimmedStringDeserializer.class)
	private final String fullAddress;
}