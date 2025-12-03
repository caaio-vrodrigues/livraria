package caio.portfolio.livraria.infrastructure.entity.book.salable.dto;

import java.math.BigDecimal;

import caio.portfolio.livraria.infrastructure.entity.book.dto.UpdateBookDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@SuperBuilder
@Jacksonized
public class UpdateSalableBookDTO extends UpdateBookDTO {

	private final BigDecimal price;
	private final Integer units;
}