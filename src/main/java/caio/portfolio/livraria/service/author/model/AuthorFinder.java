package caio.portfolio.livraria.service.author.model;

import caio.portfolio.livraria.infrastructure.entity.author.Author;

public interface AuthorFinder {
	Author findById(Long id);
	Author findByAlias(String alias);
}