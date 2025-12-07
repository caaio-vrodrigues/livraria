package caio.portfolio.livraria.service.book.salable.model;

import caio.portfolio.livraria.service.book.salable.dto.TitleAndAuthorUpdateDTO;

public interface SalableBookUniquenessValidator {
	void validateUniqueness(
		TitleAndAuthorUpdateDTO titleAndAuthorUpdateDTO, String title, Long authorId);
}