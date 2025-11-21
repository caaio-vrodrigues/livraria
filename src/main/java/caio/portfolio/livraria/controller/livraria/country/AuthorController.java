package caio.portfolio.livraria.controller.livraria.country;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import caio.portfolio.livraria.infrastructure.entity.author.dto.CreateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.service.author.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {

	private final AuthorService service;
	
	@PostMapping
	public ResponseEntity<ResponseAuthorDTO> newAuthor(@Valid @RequestBody CreateAuthorDTO dto){
		return ResponseEntity.status(HttpStatus.CREATED).body(service.createAuthor(dto));
	}
	
	@GetMapping
	public ResponseEntity<List<ResponseAuthorDTO>> findAllAuthors(){
		return ResponseEntity.ok(service.getAllAuthors());
	}
}
