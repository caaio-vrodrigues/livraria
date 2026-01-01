package caio.portfolio.livraria.infrastructure.entity.book.dto;

import caio.portfolio.livraria.model.enums.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class CreateBookDTO {

	@NotBlank(message="{title.notBlank}")
	private final String title;
	
	@NotNull(message="{authorId.notNull}")
	@Positive(message="{authorId.grather.than.zero}")
	private final Long authorId;
	
	@NotNull(message="{publisherId.notNull}")
	@Positive(message="{publisherId.grather.than.zero}")
	private final Long publisherId;
	
	@NotNull(message="{genre.notNull}")
	private final Genre genre;
	
	@NotBlank(message="{isbn.notBlank}")
	private final String isbn;
}