package caio.portfolio.livraria.service.book.salable.model;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.service.book.salable.dto.TitleAndAuthorUpdateDTO;

public interface SalableBookUpdateValidator {
	TitleAndAuthorUpdateDTO validateTitleAndAuthor(String title, String newTitle, Author author, Long authorId);
}
