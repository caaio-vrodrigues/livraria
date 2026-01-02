package caio.portfolio.livraria.serialization.model;

import java.util.InputMismatchException;

public interface SerializationExceptionCreator {
	InputMismatchException createInputMismatchException();
	InputMismatchException createInputMismatchException(String value);
}