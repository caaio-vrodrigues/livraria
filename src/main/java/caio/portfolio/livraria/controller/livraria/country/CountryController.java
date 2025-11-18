package caio.portfolio.livraria.controller.livraria.country;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import caio.portfolio.livraria.infrastructure.entity.country.dto.CreateCountryDTO;
import caio.portfolio.livraria.infrastructure.entity.country.dto.ResponseCountryDTO;
import caio.portfolio.livraria.service.country.CountryService;
import caio.portfolio.livraria.service.country.dto.CountryResultImplDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountryController {

	private final CountryService service;
	
	@PostMapping
	public ResponseEntity<ResponseCountryDTO> createOrFindCountry(
		@Valid @RequestBody CreateCountryDTO dto
	) {
		CountryResultImplDTO result = service.createOrFindCountry(dto);
	    if(result.wasCreated()) return ResponseEntity.status(HttpStatus.CREATED)
	    	.body(result.getCountry());
	    return ResponseEntity.ok(result.getCountry());
	}
	
	@GetMapping
	public ResponseEntity<List<ResponseCountryDTO>> findAllCountries(){
		return ResponseEntity.ok(service.getAllCountries());
	}
	
	@GetMapping("iso/{isoAlpha2Code}")
	public ResponseEntity<ResponseCountryDTO> findCountryByIsoAlpha2Code(
		@PathVariable String isoAlpha2Code
	) {
		return ResponseEntity.ok(service.getCountryByIsoAlpha2Code(isoAlpha2Code));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseCountryDTO> findCountryById(
		@PathVariable 
		@Positive(message="ID deve ser um número maior que '0'") 
		Integer id
	) {
		return ResponseEntity.ok(service.getCountryById(id));
	}
}
