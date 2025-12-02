package caio.portfolio.livraria.controller.livraria.author.book.salable;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.CreateSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.ResponseSalableBookDTO;
import caio.portfolio.livraria.service.book.salable.SalableBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books/salable")
public class SalableBookController {

	private final SalableBookService service;
	
	@PostMapping
	public ResponseEntity<ResponseSalableBookDTO> newSalableBook(
		@Valid @RequestBody CreateSalableBookDTO dto
	){
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(service.createSalableBook(dto));
	}
	
	@GetMapping
	public ResponseEntity<List<ResponseSalableBookDTO>> findAllResponseSalableBookDTOs(){
		return ResponseEntity.ok(service.getAllResponseSalableBookDTOs());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseSalableBookDTO> findResponseSalableBookDTOById(
		@PathVariable Long id
	){
		return ResponseEntity.ok(service.getResponseSalableBookDTOById(id));
	}
	
	@GetMapping("/author/{authorId}")
	public ResponseEntity<List<ResponseSalableBookDTO>> findResponseSalableBookDTOByAuthorId(
		@PathVariable Long authorId
	){
		return ResponseEntity.ok(service.getResponseSalableBookDTOByAuthorId(authorId));
	}
	
	@GetMapping("/publisher/{publisherId}")
	public ResponseEntity<List<ResponseSalableBookDTO>> findResponseSalableBookDTOByPublisherId(
		@PathVariable Long publisherId
	){
		return ResponseEntity.ok(service.getResponseSalableBookDTOByPublisherId(publisherId));
	}
}