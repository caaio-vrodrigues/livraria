package caio.portfolio.livraria.service.author.create;

import static org.mockito.Mockito.when;

import java.util.Locale;

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

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.exception.custom.author.ConcurrentAuthorException;
import caio.portfolio.livraria.service.author.implementation.create.AuthorExceptionCreatorImpl;

@ExtendWith(MockitoExtension.class)
class AuthorExceptionCreatorImplTest {

	@InjectMocks private AuthorExceptionCreatorImpl authorExceptionCreatorImpl;
	@Mock private MessageSource authorMessageSource;
	
	private static final Long MONTEIRO_LOBATO_ID = 6L;
	private static final String MONTEIRO_LOBATO_NAME = "Monteiro Lobato";
	private static final String MONTEIRO_LOBATO_ALIAS = "monteiro.lob";
	private static final String CONCURRENT_AUTHOR_MSG = "Falha ao tentar criar autor : `"+MONTEIRO_LOBATO_NAME+"`";
	private static final String AUTHOR_ALREADY_EXISTS_MSG = "`alias`: `"+MONTEIRO_LOBATO_ALIAS+"` já está sendo utilizado pelo autor: `"+MONTEIRO_LOBATO_NAME+"`";
	private static final String AUTHOR_NOT_FOUND_MSG = "Não foi possível encontrar um autor com `id`: `"+MONTEIRO_LOBATO_ID+"`";
	
	@Test
	@DisplayName("Deve retornar 'ConcurrentAuthorException'")
	void createConcurrentAuthorException() {
		when(authorMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(CONCURRENT_AUTHOR_MSG);
		ConcurrentAuthorException exception = authorExceptionCreatorImpl
			.createConcurrentAuthorException(CONCURRENT_AUTHOR_MSG);
		assertNotNull(exception);
		assertEquals(
			CONCURRENT_AUTHOR_MSG, 
			exception.getLocalizedMessage());
		verify(authorMessageSource, times(1)).getMessage(
			anyString(), 
			any(Object[].class),
			any(Locale.class));
	}
	
	@Test
	@DisplayName("Deve retornar 'AuthorAlreadyExistsException'")
	void createAuthorAlreadyExistsException() {
		when(authorMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(AUTHOR_ALREADY_EXISTS_MSG);
		AuthorAlreadyExistsException exception = authorExceptionCreatorImpl
			.createAuthorAlreadyExistsException(
				MONTEIRO_LOBATO_ALIAS, 
				MONTEIRO_LOBATO_NAME);
		assertNotNull(exception);
		assertEquals(
			AUTHOR_ALREADY_EXISTS_MSG, 
			exception.getLocalizedMessage());
		verify(authorMessageSource, times(1)).getMessage(
			anyString(), 
			any(Object[].class),
			any(Locale.class));
	}
	
	@Test
	@DisplayName("Deve retornar 'AuthorNotFoundException'")
	void createAuthorNotFoundException() {
		when(authorMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(AUTHOR_NOT_FOUND_MSG);
		AuthorNotFoundException exception = authorExceptionCreatorImpl
			.createAuthorNotFoundException(MONTEIRO_LOBATO_ID);
		assertNotNull(exception);
		assertEquals(
			AUTHOR_NOT_FOUND_MSG, 
			exception.getLocalizedMessage());
		verify(authorMessageSource, times(1)).getMessage(
			anyString(), 
			any(Object[].class),
			any(Locale.class));
	}
}