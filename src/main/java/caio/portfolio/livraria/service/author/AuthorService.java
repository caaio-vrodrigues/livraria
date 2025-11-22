package caio.portfolio.livraria.service.author;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.exception.custom.author.ConcurrentAuthorException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.author.dto.CreateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.UpdateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
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
	public List<ResponseAuthorDTO> getAllAuthors() {
		return repo.findAll().stream()
			.map(responseAuthorDTOCreator::toResponseAuthorDTO)
			.toList();
	}

	@Transactional(readOnly=true)
	public ResponseAuthorDTO getAuthorById(Long id) {
		return responseAuthorDTOCreator.toResponseAuthorDTO(repo.findById(id).orElseThrow(() -> 
			new AuthorNotFoundException("Não foi possível encontrar um autor com 'id': '"+id+"'")));
	}

	@Transactional(readOnly=true)
	public ResponseAuthorDTO getAuthorByAlias(String alias) {
		Optional<Author> authorOptional = repo.findByAlias(alias);
		if(authorOptional.isEmpty()) throw new AuthorNotFoundException("Não foi possível encontrar um autor com 'alias': '"+alias+"'");
		return responseAuthorDTOCreator.toResponseAuthorDTO(authorOptional.get());
	}

	@Transactional
	public ResponseAuthorDTO updateAuthor(Long id, UpdateAuthorDTO dto) {
		
		Optional<Author> existingAuthor = repo.findById(id);
		if(existingAuthor.isEmpty()) throw new AuthorNotFoundException("Não foi possível encontrar um autor com 'id': '"+id+"' para realizar atualizações");
		
		String updatedAlias = existingAuthor.get().getAlias();
		boolean containsAliasAndIsDifferent = dto.getAlias() != null && 
			!existingAuthor.get().getAlias().equals(dto.getAlias());
		if(containsAliasAndIsDifferent) {
			Optional<Author> authorUsingExpectedAlias = repo.findByAlias(dto.getAlias());
			if(authorUsingExpectedAlias.isPresent()) throw new AuthorAlreadyExistsException("'alias': '"+dto.getAlias()+"' já está sendo utilizado pelo autor: '"+authorUsingExpectedAlias.get().getFullName()+"'");
			updatedAlias = dto.getAlias();
		}
		
		String updatedFullName = existingAuthor.get().getFullName();
		boolean containsFullNameAndIsDifferent = dto.getFullName() != null && 
			!existingAuthor.get().getFullName().equals(dto.getFullName());
		if(containsFullNameAndIsDifferent) updatedFullName = dto.getFullName();
		
		LocalDate updatedBirthday = existingAuthor.get().getBirthday();
		boolean containsBirthdayAndIsDifferent = dto.getBirthday() != null && 
			!existingAuthor.get().getBirthday().equals(dto.getBirthday());
		if(containsBirthdayAndIsDifferent) updatedBirthday = dto.getBirthday();
		
		Country updatedCountry = existingAuthor.get().getCountry();
		boolean containsCountryIdAndIsDifferent = dto.getCountryId() != null && 
			!existingAuthor.get().getCountry().getId().equals(dto.getCountryId());
		if(containsCountryIdAndIsDifferent) 
			updatedCountry = countryService.getCountryById(dto.getCountryId());
		
		Author updatedAuthor = Author.builder()
			.id(existingAuthor.get().getId())
			.alias(updatedAlias)
			.fullName(updatedFullName)
			.birthday(updatedBirthday)
			.country(updatedCountry)
			.build();
		
		saveAndHandleConcurrentyAuthor(updatedAuthor);
		
		return responseAuthorDTOCreator.toResponseAuthorDTO(updatedAuthor);
	}
}
