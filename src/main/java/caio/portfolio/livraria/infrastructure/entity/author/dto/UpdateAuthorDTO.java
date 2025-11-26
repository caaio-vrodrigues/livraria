package caio.portfolio.livraria.infrastructure.entity.author.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import caio.portfolio.livraria.serialization.LocalDateDesserializer;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UpdateAuthorDTO {

	@JsonDeserialize(using=LocalDateDesserializer.class)
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate birthday;
	
	@Positive(message ="id deve ser um valor maior que 0")
	private Integer countryId;
	
	@Pattern(regexp="^(?!\s*$).+", message="alias não pode ser vazio ou conter apenas espaços em branco, se presente")
	private String alias;
	
	@Pattern(regexp="^(?!\s*$).+", message="fullName não pode ser vazio ou conter apenas espaços em branco, se presente")
	private String fullName;
}
