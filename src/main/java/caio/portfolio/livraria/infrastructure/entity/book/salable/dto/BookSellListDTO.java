package caio.portfolio.livraria.infrastructure.entity.book.salable.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class BookSellListDTO {

	@Valid
	@NotEmpty(message="{sellList.notEmpty}")
	private List<BookSellDTO> sellList;
}