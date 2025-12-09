package caio.portfolio.livraria.service.book.salable;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.model.enums.Genre;
import caio.portfolio.livraria.service.author.AuthorService;
import caio.portfolio.livraria.service.book.salable.dto.TitleAndAuthorUpdateDTO;
import caio.portfolio.livraria.service.book.salable.model.SalableBookUpdateValidator;
import caio.portfolio.livraria.service.publisher.PublisherService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SalableBookUpdateValidatorImpl implements SalableBookUpdateValidator {
	
	private final AuthorService authorService;
	private final PublisherService publisherService;
	
	@Override
	public TitleAndAuthorUpdateDTO validateTitleAndAuthor(
		String title, String newTitle, Author author, Long newAuthorId
	){
		boolean containsTitleAndIsDifferent = newTitle != null && 
			!title.equals(newTitle);
		boolean containsAuthorAndIsDifferent = newAuthorId != null && 
			!author.getId().equals(newAuthorId);
		if(containsAuthorAndIsDifferent || containsTitleAndIsDifferent) {
			title = containsTitleAndIsDifferent ? newTitle : title;
			if(containsAuthorAndIsDifferent) 
				author = authorService.getAuthorById(newAuthorId);
		}
		return TitleAndAuthorUpdateDTO.builder()
			.title(title)
			.author(author)
			.build();
	}

	@Override
	public Publisher validatePublisher(
		Publisher currentPublisher, Long newPublisherId
	){
		boolean containsPublisherAndIsDifferent = newPublisherId != null && 
			!currentPublisher.getId().equals(newPublisherId);
		if(containsPublisherAndIsDifferent) 
			return publisherService.getPublisherById(newPublisherId);
		return currentPublisher;
	}

	@Override
	public Genre validateGenre(Genre currentGenre, Genre newGenre) {
		boolean containsGenreAndIsDifferent = newGenre != null && 
			!currentGenre.getGenreType()
				.equals(newGenre.getGenreType());
		if(containsGenreAndIsDifferent) return newGenre;
		return currentGenre;
	}

	@Override
	public String validateIsbn(String currentIsbn, String newIsbn) {
		boolean containsIsbnAndIsDifferent = newIsbn != null && 
			!currentIsbn.equals(newIsbn);
		if(containsIsbnAndIsDifferent) return newIsbn;
		return currentIsbn;
	}

	@Override
	public BigDecimal validatePrice(
		BigDecimal currentPrice, BigDecimal newPrice
	){
		boolean containsPriceAndIsDifferent = newPrice != null && 
			!currentPrice.equals(newPrice);
		if(containsPriceAndIsDifferent) return newPrice;
		return currentPrice;
	}

	@Override
	public Integer validateUnits(Integer currentUnits, Integer newUnits) {
		boolean containsNewUnitsAndIsDifferent = newUnits != null && 
			!currentUnits.equals(newUnits);
		if(containsNewUnitsAndIsDifferent) return newUnits;
		return currentUnits;
	}
}
