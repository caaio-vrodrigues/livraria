package caio.portfolio.livraria.service.author.implementation.save;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import caio.portfolio.livraria.service.author.model.create.AuthorExceptionCreator;
import caio.portfolio.livraria.service.author.model.save.AuthorSaverAndConcurrencyHandle;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthorSaverAndConcurrencyHandleImpl implements AuthorSaverAndConcurrencyHandle {
	
	private final AuthorRepository repo;
	private final AuthorExceptionCreator authorExceptionCreator;

	@Override
	public Author saveAndHandleConcurrentyAuthor(Author author) {
		try {
			return repo.saveAndFlush(author);
		}
		catch(DataIntegrityViolationException e) {
			Optional<Author> authorOptional = repo.findByAlias(author.getAlias());
			if(authorOptional.isEmpty()) throw authorExceptionCreator
				.createConcurrentAuthorException(author.getFullName());
			return authorOptional.get();
		}
	}
}