package caio.portfolio.livraria.controller.country;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.service.CountryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountryController {

	private final CountryService service;
	
	@GetMapping
	public ResponseEntity<List<Country>> findAllCountries(){
		return ResponseEntity.ok(service.getAllCountries());
	}
}
