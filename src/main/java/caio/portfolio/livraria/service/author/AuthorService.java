package caio.portfolio.livraria.service.author;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.exception.custom.author.ConcurrentAuthorException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.author.dto.CreateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.UpdateAuthorDTO;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import caio.portfolio.livraria.service.author.model.ResponseAuthorDTOCreator;
import caio.portfolio.livraria.service.country.CountryService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {

	private final AuthorRepository repo;
	private final ResponseAuthorDTOCreator responseAuthorDTOCreator;
	private final AuthorUpdateValidatorImpl authorupdateValidator;
	private final CountryService countryService;
	
	private Author saveAndHandleConcurrentyAuthor(Author author) {
		try {
			return repo.saveAndFlush(author);
		}
		catch(DataIntegrityViolationException e) {
			Optional<Author> authorOptional = repo.findByAlias(author.getAlias());
			if(authorOptional.isEmpty()) throw new ConcurrentAuthorException("Falha ao tentar criar autor com 'alias': '"+author.getAlias()+"'; 'fullName': '"+author.getFullName()+"'; 'birthday': '"+author.getBirthday()+"'; 'countryId': '"+author.getCountry().getId()+"'");
			return authorOptional.get();
		}
	}
	
	@Transactional
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

	@Transactional(readOnly=true)
	public List<ResponseAuthorDTO> getAllResponseAuthorDTOs() {
		return repo.findAll().stream()
			.map(responseAuthorDTOCreator::toResponseAuthorDTO)
			.toList();
	}

	@Transactional(readOnly=true)
	public ResponseAuthorDTO getResponseAuthorDTOById(Long id) {
		return responseAuthorDTOCreator.toResponseAuthorDTO(repo.findById(id).orElseThrow(() -> 
			new AuthorNotFoundException("Não foi possível encontrar um autor com 'id': '"+id+"'")));
	}

	@Transactional(readOnly=true)
	public ResponseAuthorDTO getResponseAuthorDTOByAlias(String alias) {
		Optional<Author> authorOptional = repo.findByAlias(alias);
		if(authorOptional.isEmpty()) throw new AuthorNotFoundException("Não foi possível encontrar um autor com 'alias': '"+alias+"'");
		return responseAuthorDTOCreator.toResponseAuthorDTO(authorOptional.get());
	}

	@Transactional
	public ResponseAuthorDTO updateAuthor(Long id, UpdateAuthorDTO dto) {
		Optional<Author> existingAuthor = repo.findById(id);
		if(existingAuthor.isEmpty()) throw new AuthorNotFoundException("Não foi possível encontrar um autor com 'id': '"+id+"' para realizar atualizações");
		Author updatedAuthor = Author.builder()
			.id(existingAuthor.get().getId())
			.alias(authorupdateValidator.validateAlias(
				existingAuthor.get().getAlias(), dto.getAlias()))
			.fullName(authorupdateValidator.validateFullName(
				existingAuthor.get().getFullName(), dto.getFullName()))
			.birthday(authorupdateValidator.validateBirthday(
				existingAuthor.get().getBirthday(), dto.getBirthday()))
			.country(authorupdateValidator.validateCountry(
				existingAuthor.get().getCountry(), dto.getCountryId()))
			.build();
		saveAndHandleConcurrentyAuthor(updatedAuthor);
		return responseAuthorDTOCreator.toResponseAuthorDTO(updatedAuthor);
	}
	
	@Transactional(readOnly=true)
	public Author getAuthorById(Long id) {
		return repo.findById(id).orElseThrow(() ->
			new AuthorNotFoundException("Não foi possível encontrar um autor com 'id': '"+id+"' para realizar atualizações"));
	}
}
