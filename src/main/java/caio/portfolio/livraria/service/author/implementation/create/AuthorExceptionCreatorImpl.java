package caio.portfolio.livraria.service.author.implementation.create;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.exception.custom.author.ConcurrentAuthorException;
import caio.portfolio.livraria.service.author.model.create.AuthorExceptionCreator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthorExceptionCreatorImpl implements AuthorExceptionCreator {
	
	private final MessageSource authorMessageSource;

	@Override
	public ConcurrentAuthorException createConcurrentAuthorException(
		String authorFullName
	){
		String msg = authorMessageSource.getMessage(
			"concurrent.author", 
			new Object[]{authorFullName},
			LocaleContextHolder.getLocale());
		return new ConcurrentAuthorException(msg);
	}

	@Override
	public AuthorAlreadyExistsException createAuthorAlreadyExistsException(
		String alias, 
		String authorFullName
	){
		String msg = authorMessageSource.getMessage(
			"author.already.exists.alias", 
			new Object[]{alias, authorFullName},
			LocaleContextHolder.getLocale());
		return new AuthorAlreadyExistsException(msg);
	}

	@Override
	public AuthorNotFoundException createAuthorNotFoundException(Long id) {
		String msg = authorMessageSource.getMessage(
			"author.not.found.id", 
			new Object[]{id},
			LocaleContextHolder.getLocale());
		return new AuthorNotFoundException(msg);
	}
}