package caio.portfolio.livraria.service.author;

import java.util.Optional;

import org.springframework.stereotype.Service;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.author.dto.CreateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import caio.portfolio.livraria.service.author.model.ResponseAuthorDTOCreator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {

	private final AuthorRepository repo;
	private final ResponseAuthorDTOCreator responseAuthorDTOCreator;
	
	public ResponseAuthorDTO createAuthor(CreateAuthorDTO dto) {
		Optional<Author> authorOptional = repo.findByAlias(dto.getAlias());
		if(authorOptional.isPresent()) 
			return responseAuthorDTOCreator.toResponseAuthorDTO(authorOptional.get());
		
		// em desenvolvimento
		return null;
	}
}
