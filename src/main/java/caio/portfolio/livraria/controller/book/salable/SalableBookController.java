package caio.portfolio.livraria.controller.book.salable;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.BookSellListDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.CreateSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.ResponseSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.UpdateSalableBookDTO;
import caio.portfolio.livraria.model.enums.Genre;
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
	
	@GetMapping("/title")
	public ResponseEntity<List<ResponseSalableBookDTO>> findResponseSalableBookDTOByTitle(
		@RequestParam String title
	){
		return ResponseEntity.ok(service.getResponseSalableBookDTOByTitle(title));
	}
	
	@GetMapping("/genre")
	public ResponseEntity<List<ResponseSalableBookDTO>> findResponseSalableBookDTOByGenre(
		@RequestParam Genre genre
	){
		return ResponseEntity.ok(service.getResponseSalableBookDTOByGenre(genre));
	}
	
	@GetMapping("/isbn/{isbn}")
	public ResponseEntity<List<ResponseSalableBookDTO>> findResponseSalableBookDTOByIsbn(
		@PathVariable String isbn
	){
		return ResponseEntity.ok(service.getResponseSalableBookDTOByIsbn(isbn));
	}
	
	@PutMapping("/{authorId}")
	public ResponseEntity<ResponseSalableBookDTO> editSalableBookByTitleAndAuthor(
		@PathVariable Long authorId,
		@RequestParam String title,
		@RequestBody UpdateSalableBookDTO dto
	){
		return ResponseEntity.ok(service.updateSalableBookByTitleAndAuthor(authorId, title, dto));
	}
	
	@PutMapping("/sell-book/{bookId}/{units}")
	public ResponseEntity<BigDecimal> sellBook(
		@PathVariable Long bookId, 
		@PathVariable int units
	){
		return ResponseEntity.ok(service.sellBook(bookId, units));
	}
	
	@PutMapping("/sell-books")
	public ResponseEntity<BigDecimal> sellBooks(
		@Valid @RequestBody BookSellListDTO bookListDTO
	){
		return ResponseEntity.ok(service.sellBooks(bookListDTO));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteSalableBookById(@PathVariable Long id){
		return ResponseEntity.ok(service.deleteSalableBookById(id));
	}
}