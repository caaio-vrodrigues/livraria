package caio.portfolio.livraria.infrastructure.entity.book.dto;

import caio.portfolio.livraria.model.enums.Genre;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class UpdateBookDTO {

	private final Genre genre;
	
	@Positive(message="{authorId.grather.than.zero}")
	private final Long authorId;
	
	@Positive(message="{publisherId.grather.than.zero}")
	private final Long publisherId;
	
	@Pattern(
		regexp="^(?!\s*$).+", 
		message="{title.notBlank}")
	private final String title;
	
	@Pattern(
		regexp="^(?!\s*$).+", 
		message="{isbn.notBlank}")
	private final String isbn;
}