package caio.portfolio.livraria.service.author;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import caio.portfolio.livraria.service.author.model.AuthorFinder;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthorFinderImpl implements AuthorFinder {
	
	private final AuthorRepository repo;
	private final MessageSource authorMessageSource;
	
	@Override
	public Author findById(Long id) {
		String msg = authorMessageSource.getMessage(
			"author.not.found.id", 
			new Object[] {id},
			LocaleContextHolder.getLocale());
		return repo.findById(id).orElseThrow(() -> 
			new AuthorNotFoundException(msg));
	}

	@Override
	public Author findByAlias(String alias) {
		String msg = authorMessageSource.getMessage(
			"author.not.found.alias", 
			new Object[] {alias},
			LocaleContextHolder.getLocale());
		return repo.findByAlias(alias).orElseThrow(() -> 
			new AuthorNotFoundException(msg));
	}
}