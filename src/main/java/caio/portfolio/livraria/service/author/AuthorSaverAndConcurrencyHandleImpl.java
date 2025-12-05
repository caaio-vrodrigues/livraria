package caio.portfolio.livraria.service.author;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.author.ConcurrentAuthorException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import caio.portfolio.livraria.service.author.model.AuthorSaverAndConcurrencyHandle;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthorSaverAndConcurrencyHandleImpl implements AuthorSaverAndConcurrencyHandle {
	
	private final AuthorRepository repo;

	@Override
	public Author saveAndHandleConcurrentyAuthor(Author author) {
		try {
			return repo.saveAndFlush(author);
		}
		catch(DataIntegrityViolationException e) {
			Optional<Author> authorOptional = repo.findByAlias(author.getAlias());
			if(authorOptional.isEmpty()) throw new ConcurrentAuthorException("Falha ao tentar criar autor com 'alias': '"+author.getAlias()+"'; 'fullName': '"+author.getFullName()+"'; 'birthday': '"+author.getBirthday()+"'; 'countryId': '"+author.getCountry().getId()+"'");
			return authorOptional.get();
		}
	}
}
