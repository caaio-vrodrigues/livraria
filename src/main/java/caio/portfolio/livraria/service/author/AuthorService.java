package caio.portfolio.livraria.service.author;

import org.springframework.stereotype.Service;

import caio.portfolio.livraria.infrastructure.entity.author.dto.CreateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {

	private final AuthorRepository repo;

	public ResponseAuthorDTO createAuthor(CreateAuthorDTO dto) {
		return null;
	}
}
