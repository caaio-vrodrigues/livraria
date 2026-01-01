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
	
	@Positive(message="{countryId.grather.than.zero}")
	private final Integer countryId;
	
	@Pattern(
		regexp="^(?!\s*$).+", 
		message="{alias.notBlank}")
	private final String alias;
	
	@Pattern(
		regexp="^(?!\s*$).+", 
		message="{fullName.notBlank}")
	private final String fullName;
}