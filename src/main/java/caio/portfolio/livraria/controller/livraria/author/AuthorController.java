package caio.portfolio.livraria.controller.livraria.author;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import caio.portfolio.livraria.infrastructure.entity.author.dto.CreateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.UpdateAuthorDTO;
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
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseAuthorDTO> findAuthorById(@PathVariable Long id){
		return ResponseEntity.ok(service.getAuthorById(id));
	}
	
	@GetMapping("/alias")
	public ResponseEntity<ResponseAuthorDTO> findAuthorAlias(@RequestParam String alias){
		return ResponseEntity.ok(service.getAuthorByAlias(alias));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseAuthorDTO> editAuthorById(
		@PathVariable Long id,
		@Valid @RequestBody UpdateAuthorDTO dto
	){
		return ResponseEntity.ok(service.updateAuthor(id, dto));
	}
}
