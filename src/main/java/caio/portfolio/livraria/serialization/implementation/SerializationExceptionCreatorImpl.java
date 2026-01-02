package caio.portfolio.livraria.serialization.implementation;

import java.util.InputMismatchException;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.serialization.model.SerializationExceptionCreator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SerializationExceptionCreatorImpl implements SerializationExceptionCreator {
	
	private final MessageSource messageSource;
	
	@Override
	public InputMismatchException createInputMismatchException() {
		return new InputMismatchException(messageSource.getMessage(
			"input_mismatch_notBlank", 
			new Object[] {},
			LocaleContextHolder.getLocale()));
	}

	@Override
	public InputMismatchException createInputMismatchException(String value) {
		return new InputMismatchException(messageSource.getMessage(
			"input_mismatch_invalid_format", 
			new Object[] {value},
			LocaleContextHolder.getLocale()));
	}
}