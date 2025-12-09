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

	@Min(value=0, message="O campo 'price' não pode ser menor que 0")
	@NotNull(message="O campo 'price' não pode ser nulo")
	private final BigDecimal price;
	
	@Min(value=0, message="O campo 'units' não pode ser menor que 0")
	@NotNull(message="O campo 'units' não pode ser nulo")
	private final Integer units;
}