package caio.portfolio.livraria.service.author.model;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.exception.custom.author.ConcurrentAuthorException;

public interface AuthorExceptionCreator {
	ConcurrentAuthorException createConcurrentAuthorException(String authorFullName);
	AuthorAlreadyExistsException createAuthorAlreadyExistsException(String alias, String authorFullName);
	AuthorNotFoundException createAuthorNotFoundException(Long id);
}