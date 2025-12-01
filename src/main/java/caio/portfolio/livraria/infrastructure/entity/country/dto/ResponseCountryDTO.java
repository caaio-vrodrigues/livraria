package caio.portfolio.livraria.infrastructure.entity.country.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseCountryDTO {

	private final Integer id;
	private final String isoAlpha2Code;
	private final String name;
}
