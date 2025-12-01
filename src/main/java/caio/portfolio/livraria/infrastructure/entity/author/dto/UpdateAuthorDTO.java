package caio.portfolio.livraria.infrastructure.entity.author.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import caio.portfolio.livraria.serialization.LocalDateDesserializer;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateAuthorDTO {

	@JsonDeserialize(using=LocalDateDesserializer.class)
	@JsonFormat(pattern="yyyy-MM-dd")
	private final LocalDate birthday;
	
	@Positive(message ="id deve ser um valor maior que 0")
	private final Integer countryId;
	
	@Pattern(regexp="^(?!\s*$).+", message="alias não pode ser vazio ou conter apenas espaços em branco, se presente")
	private final String alias;
	
	@Pattern(regexp="^(?!\s*$).+", message="fullName não pode ser vazio ou conter apenas espaços em branco, se presente")
	private final String fullName;
}
