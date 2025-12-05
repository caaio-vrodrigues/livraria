package caio.portfolio.livraria.service.author.model;

import caio.portfolio.livraria.infrastructure.entity.author.Author;

public interface AuthorSaverAndConcurrencyHandle {
	Author saveAndHandleConcurrentyAuthor(Author author);
}
