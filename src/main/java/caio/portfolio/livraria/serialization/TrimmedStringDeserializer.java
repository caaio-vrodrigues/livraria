package caio.portfolio.livraria.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class TrimmedStringDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser str, DeserializationContext ctxt) 
		throws IOException 
	{
		String value = str.getText();
        return value != null ? value.trim() : null;
	}
}