package caio.portfolio.livraria.service.book.salable;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.book.salable.ConcurrentSalableBookException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookAlreadyExistsException;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.repository.SalableBookRepository;
import caio.portfolio.livraria.service.book.salable.model.SalableBookSaverAndConcurrencyHandle;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SalableBookSaverAndConcurrencyHandleImpl implements SalableBookSaverAndConcurrencyHandle {
	
	private final SalableBookRepository repo;
	
	@Override
	public SalableBook saveAndHandleConcurrency(SalableBook book) {
		try {
			return repo.saveAndFlush(book);
		}
		catch(DataIntegrityViolationException e) {
			Optional<SalableBook> salableBookOptional = repo
				.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
			if(salableBookOptional.isPresent()) throw new SalableBookAlreadyExistsException("Não foi possível realizar a operação. Livro: '"+salableBookOptional.get().getTitle()+"' já existe");
			throw new ConcurrentSalableBookException("Não foi possível criar livro: '"+book.getTitle()+"' por falha de concorrência. Verifique se o livro já existe ou tente novamente se necessário");
		}
	}
}
