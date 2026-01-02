package caio.portfolio.livraria.infrastructure.entity.publisher.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import caio.portfolio.livraria.serialization.TrimmedStringDeserializer;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePublisherDTO {

	@Positive(message="{countryId.grather.than.zero}")
	private final Integer countryId;

	@Pattern(
		regexp="^(?!\s*$).+",
		message="{name.notBlank}")
	@JsonDeserialize(using=TrimmedStringDeserializer.class)
	private final String name;

	@Pattern(
		regexp="^(?!\s*$).+",
		message="{fullAddress.notBlank}")
	@JsonDeserialize(using=TrimmedStringDeserializer.class)
	private final String fullAddress;
}