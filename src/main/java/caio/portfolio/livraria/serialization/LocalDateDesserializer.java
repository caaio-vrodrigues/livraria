package caio.portfolio.livraria.serialization;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import caio.portfolio.livraria.serialization.model.SerializationExceptionCreator;
import lombok.RequiredArgsConstructor;

@JsonComponent
@RequiredArgsConstructor
public class LocalDateDesserializer extends JsonDeserializer<LocalDate> {
	
	private final SerializationExceptionCreator serializationExceptionCreator;
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter
		.ofPattern("yyyy-MM-dd");

	@Override
	public LocalDate deserialize(
		JsonParser p, 
		DeserializationContext ctxt
	) throws IOException {
		String value = p.getText();
		if(value == null) return null;
		if(value.trim().isEmpty()) 
			throw serializationExceptionCreator
				.createInputMismatchException();
		try{
	        return LocalDate.parse(value, FORMATTER);
	    } 
		catch (DateTimeParseException e) {
			throw serializationExceptionCreator
				.createInputMismatchException(value);
	    }
	}
}