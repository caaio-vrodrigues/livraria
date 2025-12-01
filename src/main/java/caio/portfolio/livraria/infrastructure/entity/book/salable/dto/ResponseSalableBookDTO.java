package caio.portfolio.livraria.infrastructure.entity.book.salable.dto;

import java.math.BigDecimal;

import caio.portfolio.livraria.infrastructure.entity.book.dto.ResponseBookDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ResponseSalableBookDTO extends ResponseBookDTO {
	
	private final Long id;
	private final BigDecimal price;
	private final Integer units;
}