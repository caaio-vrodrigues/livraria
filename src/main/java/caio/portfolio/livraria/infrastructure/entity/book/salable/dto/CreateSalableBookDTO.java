package caio.portfolio.livraria.infrastructure.entity.book.salable.dto;

import java.math.BigDecimal;

import caio.portfolio.livraria.infrastructure.entity.book.dto.CreateBookDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@SuperBuilder
@Jacksonized
public class CreateSalableBookDTO extends CreateBookDTO {

	@Min(value=0, message="{price.positive.number}")
	@NotNull(message="{price.notNull}")
	private final BigDecimal price;
	
	@Min(value=0, message="{units.positive.number}")
	@NotNull(message="{units.notNull}")
	private final Integer units;
}