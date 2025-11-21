package caio.portfolio.livraria.service.author;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.author.dto.CreateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import caio.portfolio.livraria.service.author.model.ResponseAuthorDTOCreator;
import caio.portfolio.livraria.service.country.CountryService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {

	private final AuthorRepository repo;
	private final ResponseAuthorDTOCreator responseAuthorDTOCreator;
	private final CountryService countryService;
	
	private Author saveAndHandleConcurrentyAuthor(Author author) {
		try {
			return repo.saveAndFlush(author);
		}
		catch(DataIntegrityViolationException e) {
			Optional<Author> authorOptional = repo.findByAlias(author.getAlias());
			if(authorOptional.isEmpty()) throw new RuntimeException("Falha ao tentar criar autor com 'alias': "+author.getAlias()+"; 'fullName': "+author.getFullName()+"; 'birthday': "+author.getBirthday()+"; 'countryId': "+author.getCountry().getId());
			return authorOptional.get();
		}
	}
	
	public ResponseAuthorDTO createAuthor(CreateAuthorDTO dto) {
		Optional<Author> authorOptional = repo.findByAlias(dto.getAlias());
		if(authorOptional.isPresent()) throw new AuthorAlreadyExistsException("'alias': '"+dto.getAlias()+"' já está sendo utilizado pelo autor: '"+authorOptional.get().getFullName()+"'");
		Author newAuthor = Author.builder()
			.alias(dto.getAlias())
			.fullName(dto.getFullName())
			.birthday(dto.getBirthday())
			.country(countryService.getCountryById(dto.getCountryId()))
			.build();
		newAuthor = saveAndHandleConcurrentyAuthor(newAuthor);
		return responseAuthorDTOCreator.toResponseAuthorDTO(newAuthor);
	}
}
