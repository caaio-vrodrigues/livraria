package caio.portfolio.livraria.service.book.salable.model.validate;

import java.math.BigDecimal;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.model.enums.Genre;
import caio.portfolio.livraria.service.book.salable.dto.TitleAndAuthorUpdateDTO;

public interface SalableBookUpdateValidator {
	TitleAndAuthorUpdateDTO validateTitleAndAuthor(
		String currentTitle, String newTitle, Author currentAuthor, Long newAuthorId);
	
	Publisher validatePublisher(Publisher currentPublisher, Long newPublisherId);
	Genre validateGenre(Genre currentGenre, Genre newGenre);
	String validateIsbn(String currentIsbn, String newIsbn);
	BigDecimal validatePrice(BigDecimal currentPrice, BigDecimal newPrice);
	Integer validateUnits(Integer currentUnits, Integer newUnits);
}