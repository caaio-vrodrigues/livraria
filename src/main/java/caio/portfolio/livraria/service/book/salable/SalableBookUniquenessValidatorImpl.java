package caio.portfolio.livraria.service.book.salable;

import java.util.Optional;

import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.book.salable.SalableBookAlreadyExistsException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.repository.SalableBookRepository;
import caio.portfolio.livraria.service.book.salable.dto.TitleAndAuthorUpdateDTO;
import caio.portfolio.livraria.service.book.salable.model.SalableBookUniquenessValidator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SalableBookUniquenessValidatorImpl implements SalableBookUniquenessValidator {
	
	private final SalableBookRepository repo;
	
	@Override
	public void validateUniquenessOnUpdate(
		TitleAndAuthorUpdateDTO titleAndAuthorUpdateDTO, 
		String title, 
		Long authorId
	){
		boolean isDifferentTitleOrAuthor = !titleAndAuthorUpdateDTO.getTitle()
				.equals(title) || 
			!titleAndAuthorUpdateDTO.getAuthor().getId()
				.equals(authorId);
		if(isDifferentTitleOrAuthor) {
			Optional<SalableBook> existingBookOptional = repo
				.findByTitleAndAuthor(
					titleAndAuthorUpdateDTO.getTitle(), 
					titleAndAuthorUpdateDTO.getAuthor());
			if(existingBookOptional.isPresent()) 
				throw new SalableBookAlreadyExistsException("Não foi possível realizar a operação. Livro: '"+titleAndAuthorUpdateDTO.getTitle()+"' já existe pelo autor: '"+titleAndAuthorUpdateDTO.getAuthor().getFullName()+"'");
		}
	}

	@Override
	public void validateUniquenessOnCreate(Author author, String title) {
		Optional<SalableBook> salableBookOptional = repo
			.findByTitleAndAuthor(title, author);
		if(salableBookOptional.isPresent()) 
			throw new SalableBookAlreadyExistsException("Não foi possível realizar a operação. Livro: '"+salableBookOptional.get().getTitle()+"' já existe");
	}
}
