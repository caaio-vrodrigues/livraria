package caio.portfolio.livraria.service.author.implementation.create;

import org.springframework.stereotype.Component;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.service.author.model.create.ResponseAuthorDTOCreator;

@Component
public class ResponseAuthorDTOCreatorImpl implements ResponseAuthorDTOCreator {

	@Override
	public ResponseAuthorDTO toResponseAuthorDTO(Author author) {
		return ResponseAuthorDTO.builder()
			.id(author.getId())
			.alias(author.getAlias())
			.fullName(author.getFullName())
			.birthday(author.getBirthday())
			.countryId(author.getCountry().getId())
			.build();
	}
}
