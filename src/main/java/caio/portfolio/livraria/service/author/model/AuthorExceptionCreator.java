package caio.portfolio.livraria.service.author.model;

import caio.portfolio.livraria.exception.custom.author.ConcurrentAuthorException;

public interface AuthorExceptionCreator {
	ConcurrentAuthorException createConcurrentAuthorException(String fullName);
}