package caio.portfolio.livraria.service.author;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.author.dto.CreateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.UpdateAuthorDTO;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import caio.portfolio.livraria.service.author.model.ResponseAuthorDTOCreator;
import caio.portfolio.livraria.service.author.model.AuthorSaverAndConcurrencyHandle;
import caio.portfolio.livraria.service.country.CountryService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {

	private final AuthorRepository repo;
	private final ResponseAuthorDTOCreator responseAuthorDTOCreator;
	private final AuthorSaverAndConcurrencyHandle saverAndConcurrencyHandle;
	private final AuthorUpdateValidatorImpl authorupdateValidator;
	private final CountryService countryService;
	
	@Transactional
	public ResponseAuthorDTO createAuthor(CreateAuthorDTO dto) {
		repo.findByAlias(dto.getAlias()).orElseThrow(() ->
			new AuthorAlreadyExistsException("'alias': '"+dto.getAlias()+"' já está sendo utilizado pelo autor: '"+dto.getFullName()+"'"));
		Author newAuthor = Author.builder()
			.alias(dto.getAlias())
			.fullName(dto.getFullName())
			.birthday(dto.getBirthday())
			.country(countryService.getCountryById(dto.getCountryId()))
			.build();
		return responseAuthorDTOCreator
			.toResponseAuthorDTO(saverAndConcurrencyHandle
				.saveAndHandleConcurrentyAuthor(newAuthor));
	}

	@Transactional(readOnly=true)
	public List<ResponseAuthorDTO> getAllResponseAuthorDTOs() {
		return repo.findAll().stream()
			.map(responseAuthorDTOCreator::toResponseAuthorDTO)
			.toList();
	}

	@Transactional(readOnly=true)
	public ResponseAuthorDTO getResponseAuthorDTOById(Long id) {
		return responseAuthorDTOCreator
			.toResponseAuthorDTO(repo.findById(id).orElseThrow(() -> 
				new AuthorNotFoundException("Não foi possível encontrar um autor com 'id': '"+id+"'")));
	}

	@Transactional(readOnly=true)
	public ResponseAuthorDTO getResponseAuthorDTOByAlias(String alias) {
		return responseAuthorDTOCreator
			.toResponseAuthorDTO(repo.findByAlias(alias).orElseThrow(() -> 
				new AuthorNotFoundException("Não foi possível encontrar um autor com 'alias': '"+alias+"'")));
	}

	@Transactional
	public ResponseAuthorDTO updateAuthor(Long id, UpdateAuthorDTO dto) {
		Author existingAuthor = repo.findById(id).orElseThrow(() -> 
			new AuthorNotFoundException("Não foi possível encontrar um autor com 'id': '"+id+"' para realizar atualizações"));
		Author updatedAuthor = Author.builder()
			.id(existingAuthor.getId())
			.alias(authorupdateValidator.validateAlias(
				existingAuthor.getAlias(), dto.getAlias()))
			.fullName(authorupdateValidator.validateFullName(
				existingAuthor.getFullName(), dto.getFullName()))
			.birthday(authorupdateValidator.validateBirthday(
				existingAuthor.getBirthday(), dto.getBirthday()))
			.country(authorupdateValidator.validateCountry(
				existingAuthor.getCountry(), dto.getCountryId()))
			.build();
		return responseAuthorDTOCreator
			.toResponseAuthorDTO(saverAndConcurrencyHandle
				.saveAndHandleConcurrentyAuthor(updatedAuthor));
	}
	
	@Transactional(readOnly=true)
	public Author getAuthorById(Long id) {
		return repo.findById(id).orElseThrow(() ->
			new AuthorNotFoundException("Não foi possível encontrar um autor com 'id': '"+id+"' para realizar atualizações"));
	}
}