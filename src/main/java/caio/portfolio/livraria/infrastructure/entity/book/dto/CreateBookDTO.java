package caio.portfolio.livraria.infrastructure.entity.book.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import caio.portfolio.livraria.model.enums.Genre;
import caio.portfolio.livraria.serialization.TrimmedStringDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class CreateBookDTO {
	
	@NotNull(message="{genre.notNull}")
	private final Genre genre;
	
	@NotNull(message="{authorId.notNull}")
	@Positive(message="{authorId.grather.than.zero}")
	private final Long authorId;
	
	@NotNull(message="{publisherId.notNull}")
	@Positive(message="{publisherId.grather.than.zero}")
	private final Long publisherId;
	
	@NotBlank(message="{isbn.notBlank}")
	@JsonDeserialize(using=TrimmedStringDeserializer.class)
	private final String isbn;
	
	@NotBlank(message="{title.notBlank}")
	@JsonDeserialize(using=TrimmedStringDeserializer.class)
	private final String title;
}