package caio.portfolio.livraria.infrastructure.entity.author.dto;

import java.time.LocalDate;
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
public class UpdateAuthorDTO {

	private String alias;
	private String fullName;
	private LocalDate birthday;
	private Integer countryId;
}
