package caio.portfolio.livraria.service.book.salable.dto;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TitleAndAuthorUpdateDTO {

	private final Author author;
	private final String title;
}