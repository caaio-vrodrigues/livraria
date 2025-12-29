package caio.portfolio.livraria.service.author.model.create;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.exception.custom.author.ConcurrentAuthorException;

public interface AuthorExceptionCreator {
	AuthorNotFoundException createAuthorNotFoundException(Long id);
	
	ConcurrentAuthorException createConcurrentAuthorException(
		String authorFullName);
	
	AuthorAlreadyExistsException createAuthorAlreadyExistsException(
		String alias,
		String authorFullName);
}