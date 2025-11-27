package caio.portfolio.livraria.controller.livraria.publisher;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import caio.portfolio.livraria.infrastructure.entity.publisher.dto.CreatePublisherDTO;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.ResponsePublisherDTO;
import caio.portfolio.livraria.service.publisher.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/publishers")
public class PublisherController {

	private final PublisherService service;
	
	@PostMapping
	public ResponseEntity<ResponsePublisherDTO> newPublisher(
		@Valid @RequestBody CreatePublisherDTO dto
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.createPublisher(dto));
	}
	
	@GetMapping
	public ResponseEntity<List<ResponsePublisherDTO>> searchAllPublishers(){
		return ResponseEntity.ok(service.getAllPublishers());
	}
}
