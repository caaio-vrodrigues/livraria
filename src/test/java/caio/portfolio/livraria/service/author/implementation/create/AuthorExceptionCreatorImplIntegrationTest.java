package caio.portfolio.livraria.service.author.implementation.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthorExceptionCreatorImplIntegrationTest {

	@Autowired private AuthorExceptionCreatorImpl authorExceptionCreatorImpl;
	
	private static final Long MONTEIRO_LOBATO_ID = 6L;
	private static final String MONTEIRO_LOBATO_NAME = "Monteiro Lobato";
	private static final String MONTEIRO_LOBATO_ALIAS = "monteiro.lob";
	private static final String AUTHOR_ALREADY_EXISTS_MSG = "`alias`: `"+MONTEIRO_LOBATO_ALIAS+"` já está sendo utilizado pelo autor: `"+MONTEIRO_LOBATO_NAME+"`";
	private static final String AUTHOR_NOT_FOUND_MSG = "Não foi possível encontrar um autor com `id`: `"+MONTEIRO_LOBATO_ID+"`";
	
	@Test
	@DisplayName("Deve retornar 'AuthorAlreadyExistsException'")
	void createAuthorAlreadyExistsException() {
		AuthorAlreadyExistsException exception = authorExceptionCreatorImpl
			.createAuthorAlreadyExistsException(
				MONTEIRO_LOBATO_ALIAS, 
				MONTEIRO_LOBATO_NAME);
		assertNotNull(exception);
		assertEquals(
			AUTHOR_ALREADY_EXISTS_MSG, 
			exception.getLocalizedMessage());
	}
	
	@Test
	@DisplayName("Deve retornar 'AuthorNotFoundException'")
	void createAuthorNotFoundException() {
		AuthorNotFoundException exception = authorExceptionCreatorImpl
			.createAuthorNotFoundException(MONTEIRO_LOBATO_ID);
		assertNotNull(exception);
		assertEquals(
			AUTHOR_NOT_FOUND_MSG, 
			exception.getLocalizedMessage());
	}
}