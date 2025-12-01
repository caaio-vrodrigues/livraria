package caio.portfolio.livraria.infrastructure.entity.author.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseAuthorDTO {
	
	private final Long id;
	private final String alias;
	private final String fullName;
	private final LocalDate birthday;
	private final Integer countryId;
}
