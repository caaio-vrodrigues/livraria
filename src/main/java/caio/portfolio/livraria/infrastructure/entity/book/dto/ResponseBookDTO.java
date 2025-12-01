package caio.portfolio.livraria.infrastructure.entity.book.dto;

import caio.portfolio.livraria.model.enums.Genre;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ResponseBookDTO {

	private final String title;
	private final Long authorId;
	private final Long publisherId;
	private final Genre genre;
	private final String isbn;
}