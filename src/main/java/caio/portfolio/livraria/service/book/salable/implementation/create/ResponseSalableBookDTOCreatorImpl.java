package caio.portfolio.livraria.service.book.salable.implementation.create;

import org.springframework.stereotype.Component;

import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.ResponseSalableBookDTO;
import caio.portfolio.livraria.service.book.salable.model.create.ResponseSalableBookDTOCreator;

@Component
public class ResponseSalableBookDTOCreatorImpl implements ResponseSalableBookDTOCreator {

	@Override
	public ResponseSalableBookDTO toResponseSalableBookDTO(SalableBook book) {
		return ResponseSalableBookDTO.builder()
			.id(book.getId())
			.title(book.getTitle())
			.genre(book.getGenre())
			.authorId(book.getAuthor().getId())
			.publisherId(book.getPublisher().getId())
			.isbn(book.getIsbn())
			.price(book.getPrice())
			.units(book.getUnits())
			.build();
	}
}
