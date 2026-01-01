package caio.portfolio.livraria.infrastructure.entity.book.salable.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class BookSellDTO {

	@NotNull(message="{bookId.notNull}")
	@Positive(message="{bookId.grather.than.zero}")
	private Long bookId;
	
	@Min(value=1, message="{units.grather.than.zero}")
	private int units;
}