package caio.portfolio.livraria.service.book.salable;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.model.enums.Genre;
import caio.portfolio.livraria.service.author.AuthorService;
import caio.portfolio.livraria.service.book.salable.dto.TitleAndAuthorUpdateDTO;
import caio.portfolio.livraria.service.book.salable.model.SalableBookUpdateValidator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SalableBookUpdateValidatorImpl implements SalableBookUpdateValidator {
	
	private final AuthorService authorService;
	
	@Override
	public TitleAndAuthorUpdateDTO validateTitleAndAuthor(
		String title, String newTitle, Author author, Long newAuthorId
	){
		boolean containsTitleAndIsDifferent = newTitle != null && !title.equals(newTitle);
		boolean containsAuthorAndIsDifferent = newAuthorId != null && !author.getId().equals(newAuthorId);
		if(containsAuthorAndIsDifferent || containsTitleAndIsDifferent) {
			title = containsTitleAndIsDifferent ? newTitle : title;
			if(containsAuthorAndIsDifferent) author = authorService.getAuthorById(newAuthorId);
		}
		return TitleAndAuthorUpdateDTO.builder()
			.title(newTitle)
			.author(author)
			.build();
	}

	@Override
	public Publisher validatePublisher(Publisher publisher, Long newPublisherID) {
		// TODO 
		return null;
	}

	@Override
	public Genre validateGenre(Genre genre, Genre newGenre) {
		// TODO 
		return null;
	}

	@Override
	public String validateIsbn(String isbn, String newIsbn) {
		// TODO 
		return null;
	}

	@Override
	public BigDecimal validatePrice(BigDecimal price, BigDecimal newPrice) {
		// TODO 
		return null;
	}

	@Override
	public Integer validateUnits(Integer unitas, Integer newUnits) {
		// TODO 
		return null;
	}
}
