package caio.portfolio.livraria.infrastructure.entity.book.salable.dto;

import java.math.BigDecimal;

import caio.portfolio.livraria.infrastructure.entity.book.dto.UpdateBookDTO;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@SuperBuilder
@Jacksonized
public class UpdateSalableBookDTO extends UpdateBookDTO {

	@Min(value=0, message="{price.positive.number}")
	private final BigDecimal price;
	
	@Min(value=0, message="{units.positive.number}")
	private final Integer units;
}