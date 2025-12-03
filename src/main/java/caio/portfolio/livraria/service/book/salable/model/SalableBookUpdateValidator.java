package caio.portfolio.livraria.service.book.salable.model;

import java.math.BigDecimal;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.model.enums.Genre;
import caio.portfolio.livraria.service.book.salable.dto.TitleAndAuthorUpdateDTO;

public interface SalableBookUpdateValidator {
	TitleAndAuthorUpdateDTO validateTitleAndAuthor(
		String title, String newTitle, Author author, Long nweAuthorId);
	
	Publisher validatePublisher(Publisher publisher, Long newPublisherID);
	Genre validateGenre(Genre genre, Genre newGenre);
	String validateIsbn(String isbn, String newIsbn);
	BigDecimal validatePrice(BigDecimal price, BigDecimal newPrice);
	Integer validateUnits(Integer unitas, Integer newUnits);
}