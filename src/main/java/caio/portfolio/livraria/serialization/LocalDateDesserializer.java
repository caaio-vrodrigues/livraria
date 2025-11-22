package caio.portfolio.livraria.serialization;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalDateDesserializer extends JsonDeserializer<LocalDate> {
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String value = p.getText();
		
		if(value == null) return null;
		if(value.trim().isEmpty()) throw new InputMismatchException("A data de nascimento não pode estar vazia ou em branco.");
        
		try{
	        return LocalDate.parse(value, FORMATTER);
	    } 
		catch (DateTimeParseException e) {
			throw new InputMismatchException("Formato de data inválido para 'birthday'. Esperado: 'yyyy-MM-dd'; Recebido: '"+value);
	    }
	}
}
