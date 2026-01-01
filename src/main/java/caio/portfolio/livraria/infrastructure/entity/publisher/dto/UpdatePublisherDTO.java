package caio.portfolio.livraria.infrastructure.entity.publisher.dto;

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
	private final String name;

	@Pattern(
		regexp="^(?!\s*$).+",
		message="{fullAddress.notBlank}")
	private final String fullAddress;
}