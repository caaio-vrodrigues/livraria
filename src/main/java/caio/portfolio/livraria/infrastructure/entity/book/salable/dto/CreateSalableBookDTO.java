package caio.portfolio.livraria.infrastructure.entity.book.salable.dto;

import java.math.BigDecimal;

import caio.portfolio.livraria.infrastructure.entity.book.dto.CreateBookDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CreateSalableBookDTO extends CreateBookDTO {

	@NotNull(message="O campo 'price' não pode ser nulo")
	private final BigDecimal price;
	
	@NotNull(message="O campo 'units' não pode ser nulo")
	private final Integer units;
}