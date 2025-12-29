package caio.portfolio.livraria.service.book.salable.model.validate;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.service.book.salable.dto.TitleAndAuthorUpdateDTO;

public interface SalableBookUniquenessValidator {
	void validateUniquenessOnUpdate(
		TitleAndAuthorUpdateDTO titleAndAuthorUpdateDTO, String title, Long authorId);
	
	void validateUniquenessOnCreate(Author author, String title);
}