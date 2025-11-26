package caio.portfolio.livraria.infrastructure.entity.country.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ResponseCountryDTO {

	private Integer id;
	private String isoAlpha2Code;
	private String name;
}
