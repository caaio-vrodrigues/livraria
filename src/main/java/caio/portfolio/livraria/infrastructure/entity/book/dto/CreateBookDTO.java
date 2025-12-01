package caio.portfolio.livraria.infrastructure.entity.book.dto;

import caio.portfolio.livraria.model.enums.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class CreateBookDTO {

	@NotBlank(message="O campo 'title' não pode estar vazio")
	private final String title;
	
	@NotNull(message="O campo 'authorId' não pode ser nulo")
	private final Long authorId;
	
	@NotNull(message="O campo 'publisherId' não pode ser nulo")
	private final Long publisherId;
	
	@NotNull(message="O campo 'genre' não pode ser nulo")
	private final Genre genre;
	
	@NotBlank(message="O campo 'isbn' não pode estar vazio")
	private final String isbn;
}
