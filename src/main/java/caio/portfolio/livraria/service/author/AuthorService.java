package caio.portfolio.livraria.service.author;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.author.dto.CreateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.UpdateAuthorDTO;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import caio.portfolio.livraria.service.author.model.find.AuthorFinder;
import caio.portfolio.livraria.service.author.model.save.AuthorSaverAndConcurrencyHandle;
import caio.portfolio.livraria.service.author.model.validate.AuthorUpdateValidator;
import caio.portfolio.livraria.service.author.model.create.AuthorExceptionCreator;
import caio.portfolio.livraria.service.author.model.create.ResponseAuthorDTOCreator;
import caio.portfolio.livraria.service.country.CountryService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {

	private final AuthorRepository repo;
	private final ResponseAuthorDTOCreator responseAuthorDTOCreator;
	private final AuthorSaverAndConcurrencyHandle saverAndConcurrencyHandle;
	private final AuthorUpdateValidator authorupdateValidator;
	private final AuthorExceptionCreator authorExceptionCreator;
	private final AuthorFinder authorFinder;
	private final CountryService countryService;
	
	@Transactional
	public ResponseAuthorDTO createAuthor(CreateAuthorDTO dto) {
		Optional<Author> authorOptional = repo.findByAlias(dto.getAlias());
		if(authorOptional.isPresent()) throw authorExceptionCreator
			.createAuthorAlreadyExistsException(
				dto.getAlias(), 
				dto.getFullName());
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
			.toResponseAuthorDTO(authorFinder.findById(id));
	}

	@Transactional(readOnly=true)
	public ResponseAuthorDTO getResponseAuthorDTOByAlias(String alias) {
		return responseAuthorDTOCreator
			.toResponseAuthorDTO(authorFinder.findByAlias(alias));
	}

	@Transactional
	public ResponseAuthorDTO updateAuthor(Long id, UpdateAuthorDTO dto) {
		Author existingAuthor = authorFinder.findById(id);
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
		return authorFinder.findById(id);
	}

	public Boolean deleteAuthorById(Long id) {
		if(!repo.existsById(id)) throw authorExceptionCreator
			.createAuthorNotFoundException(id);
		repo.deleteById(id);
		return true;
	}
}