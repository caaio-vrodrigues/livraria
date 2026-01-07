package caio.portfolio.livraria.service.book.salable.implementation.create;

import java.util.Locale;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import caio.portfolio.livraria.exception.custom.book.salable.ConcurrentSalableBookException;
import caio.portfolio.livraria.exception.custom.book.salable.InsuficientSalableBookUnitsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;

@ExtendWith(MockitoExtension.class)
class SalableBookExceptionCreatorImplTest {

	@InjectMocks private SalableBookExceptionCreatorImpl salableBookExceptionCreatorImpl;
	@Mock private MessageSource salableBookMessageSource;
	
	private static final int UNITS = 100;
	private static final Long O_ALQUIMISTA_ID = 1L;
	private static final String PAULO_COELHO_FULL_NAME = "Paulo Coelho";
	private static final String O_ALQUIMISTA_TITLE = "O Alquimista";
	private static final String SALABLE_BOOK_ALREADY_EXISTS_MSG = "Não foi possível realizar a operação. Livro com `title`: `"+O_ALQUIMISTA_TITLE+"` e `author`: `"+PAULO_COELHO_FULL_NAME+"` já existe";
	private static final String CONCURRENT_BOOK_MSG = "Não foi possível criar livro: `"+O_ALQUIMISTA_TITLE+"` por falha de concorrência. Verifique se o livro já existe ou tente novamente se necessário";
	private static final String BOOK_NOT_FOUND_ID_MSG = "Não foi possível encontrar livro com `id`: `"+O_ALQUIMISTA_ID+"`";
	private static final String INSUFICIENT_BOOK_UNITS_MSG = "Quantidade de livros insuficiente para realizar a venda. Estoque atual: `"+UNITS+"`";
	
	@Test
	@DisplayName("Deve criar e retornar 'SalableBookAlreadyExistsException'")
	void createSalableBookAlreadyExistsException() {
		when(salableBookMessageSource
			.getMessage(
				anyString(),
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(SALABLE_BOOK_ALREADY_EXISTS_MSG);
		SalableBookAlreadyExistsException exception = salableBookExceptionCreatorImpl
			.createSalableBookAlreadyExistsException(
				PAULO_COELHO_FULL_NAME, 
				O_ALQUIMISTA_TITLE);
		assertNotNull(exception);
		assertEquals(
			SALABLE_BOOK_ALREADY_EXISTS_MSG,
			exception.getLocalizedMessage());
		verify(salableBookMessageSource, times(1))
			.getMessage(
		        anyString(),
		        any(Object[].class),
		        any(Locale.class));
	}
	
	@Test
	@DisplayName("Deve criar e retornar 'ConcurrentSalableBookException'")
	void createConcurrentSalableBookException() {
		when(salableBookMessageSource
			.getMessage(
				anyString(),
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(CONCURRENT_BOOK_MSG);
		ConcurrentSalableBookException exception = salableBookExceptionCreatorImpl
			.createConcurrentSalableBookException(O_ALQUIMISTA_TITLE);
		assertNotNull(exception);
		assertEquals(
			CONCURRENT_BOOK_MSG,
			exception.getLocalizedMessage());
		verify(salableBookMessageSource, times(1))
			.getMessage(
		        anyString(),
		        any(Object[].class),
		        any(Locale.class));
	}
	
	@Test
	@DisplayName("Deve criar e retornar 'SalableBookNotFoundException'")
	void createSalableBookNotFoundException() {
		when(salableBookMessageSource
			.getMessage(
				anyString(),
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(BOOK_NOT_FOUND_ID_MSG);
		SalableBookNotFoundException exception = salableBookExceptionCreatorImpl
			.createSalableBookNotFoundException(O_ALQUIMISTA_ID);
		assertNotNull(exception);
		assertEquals(
			BOOK_NOT_FOUND_ID_MSG,
			exception.getLocalizedMessage());
		verify(salableBookMessageSource, times(1))
			.getMessage(
		        anyString(),
		        any(Object[].class),
		        any(Locale.class));
	}
	
	@Test
	@DisplayName("Deve criar e retornar 'InsuficientSalableBookUnitsException'")
	void createInsuficientSalableBookUnitsException() {
		when(salableBookMessageSource
			.getMessage(
				anyString(),
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(INSUFICIENT_BOOK_UNITS_MSG);
		InsuficientSalableBookUnitsException exception = salableBookExceptionCreatorImpl
			.createInsuficientSalableBookUnitsException(UNITS);
		assertNotNull(exception);
		assertEquals(
			INSUFICIENT_BOOK_UNITS_MSG,
			exception.getLocalizedMessage());
		verify(salableBookMessageSource, times(1))
			.getMessage(
		        anyString(),
		        any(Object[].class),
		        any(Locale.class));
	}
}