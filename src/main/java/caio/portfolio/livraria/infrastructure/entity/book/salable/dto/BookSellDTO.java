package caio.portfolio.livraria.infrastructure.entity.book.salable.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class BookSellDTO {

	@NotNull(message="O campo 'bookId' não pode ser nulo")
	private Long bookId;
	
	@Min(value=1, message ="O campo 'units' deve conter ao menos 1 unidade")
	private int units;
}