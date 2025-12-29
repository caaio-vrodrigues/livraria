package caio.portfolio.livraria.service.author.model.create;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;

public interface ResponseAuthorDTOCreator {
	ResponseAuthorDTO toResponseAuthorDTO(Author author);
}
