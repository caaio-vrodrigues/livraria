package caio.portfolio.livraria.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Genre {
	ROMANCE("Romance"),
	FICTION("Ficção"),
	FANTASY("Fantasia"),
	SUSPENSE("Suspense"),
	CHILDISH("Infantil");
	
	private final String genreType;
}