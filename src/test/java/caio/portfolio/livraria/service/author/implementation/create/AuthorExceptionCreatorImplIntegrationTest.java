package caio.portfolio.livraria.service.author.implementation.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthorExceptionCreatorImplIntegrationTest {

	@Autowired private AuthorExceptionCreatorImpl authorExceptionCreatorImpl;
	@Autowired private MessageSource authorMessageSource;
	
	private static final Long MONTEIRO_LOBATO_ID = 6L;
	private static final String MONTEIRO_LOBATO_NAME = "Monteiro Lobato";
	private static final String MONTEIRO_LOBATO_ALIAS = "monteiro.lob";
	
	@Test
	@DisplayName("Deve retornar 'AuthorAlreadyExistsException'")
	void createAuthorAlreadyExistsException() {
		String expectedMsg = authorMessageSource.getMessage(
			"author.already.exists.alias", 
			new Object[]{MONTEIRO_LOBATO_ALIAS, MONTEIRO_LOBATO_NAME},
			LocaleContextHolder.getLocale());
		AuthorAlreadyExistsException exception = authorExceptionCreatorImpl
			.createAuthorAlreadyExistsException(
				MONTEIRO_LOBATO_ALIAS, 
				MONTEIRO_LOBATO_NAME);
		assertNotNull(exception);
		assertEquals(expectedMsg, exception.getLocalizedMessage());
	}
	
	@Test
	@DisplayName("Deve retornar 'AuthorNotFoundException'")
	void createAuthorNotFoundException() {
		String expectedMsg = authorMessageSource.getMessage(
			"author.not.found.id", 
			new Object[]{MONTEIRO_LOBATO_ID},
			LocaleContextHolder.getLocale());
		AuthorNotFoundException exception = authorExceptionCreatorImpl
			.createAuthorNotFoundException(MONTEIRO_LOBATO_ID);
		assertNotNull(exception);
		assertEquals(expectedMsg, exception.getLocalizedMessage());
	}
}