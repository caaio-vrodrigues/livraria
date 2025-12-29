package caio.portfolio.livraria.service.book.salable.implementation.save;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.repository.SalableBookRepository;
import caio.portfolio.livraria.service.book.salable.model.create.SalableBookExceptionCreator;
import caio.portfolio.livraria.service.book.salable.model.save.SalableBookSaverAndConcurrencyHandle;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SalableBookSaverAndConcurrencyHandleImpl implements SalableBookSaverAndConcurrencyHandle {
	
	private final SalableBookRepository repo;
	private final SalableBookExceptionCreator salableBookExceptionCreator;
	
	@Override
	public SalableBook saveAndHandleConcurrency(SalableBook book) {
		try {
			return repo.saveAndFlush(book);
		}
		catch(DataIntegrityViolationException e) {
			Optional<SalableBook> salableBookOptional = repo.findByTitleAndAuthor(
				book.getTitle(), 
				book.getAuthor());
			if(salableBookOptional.isPresent()) 
				throw salableBookExceptionCreator.createSalableBookAlreadyExistsException(
					book.getAuthor().getFullName(), 
					book.getTitle());
			throw salableBookExceptionCreator
				.createConcurrentSalableBookException(book.getTitle());
		}
	}
}