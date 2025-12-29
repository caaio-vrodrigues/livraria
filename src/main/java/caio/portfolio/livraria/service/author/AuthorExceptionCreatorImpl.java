package caio.portfolio.livraria.service.author;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.author.ConcurrentAuthorException;
import caio.portfolio.livraria.service.author.model.AuthorExceptionCreator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthorExceptionCreatorImpl implements AuthorExceptionCreator {
	
	private final MessageSource authorMessageSource;

	@Override
	public ConcurrentAuthorException createConcurrentAuthorException(String fullName) {
		String msg = authorMessageSource.getMessage(
			"concurrent", 
			new Object[]{fullName},
			LocaleContextHolder.getLocale());
		return new ConcurrentAuthorException(msg);
	}
}