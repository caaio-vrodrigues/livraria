package caio.portfolio.livraria.service.book.salable.implementation.create;

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

import caio.portfolio.livraria.exception.custom.book.salable.ConcurrentSalableBookException;
import caio.portfolio.livraria.exception.custom.book.salable.InsuficientSalableBookUnitsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class SalableBookExceptionCreatorImplIntegrationTest {

	@Autowired private SalableBookExceptionCreatorImpl salableBookExceptionCreatorImpl;
	
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
		SalableBookAlreadyExistsException exception = salableBookExceptionCreatorImpl
			.createSalableBookAlreadyExistsException(
				PAULO_COELHO_FULL_NAME, 
				O_ALQUIMISTA_TITLE);
		assertNotNull(exception);
		assertEquals(
			SALABLE_BOOK_ALREADY_EXISTS_MSG,
			exception.getLocalizedMessage());
	}
	
	@Test
	@DisplayName("Deve criar e retornar 'ConcurrentSalableBookException'")
	void createConcurrentSalableBookException() {
		ConcurrentSalableBookException exception = salableBookExceptionCreatorImpl
			.createConcurrentSalableBookException(O_ALQUIMISTA_TITLE);
		assertNotNull(exception);
		assertEquals(
			CONCURRENT_BOOK_MSG,
			exception.getLocalizedMessage());
	}
	
	@Test
	@DisplayName("Deve criar e retornar 'SalableBookNotFoundException'")
	void createSalableBookNotFoundException() {
		SalableBookNotFoundException exception = salableBookExceptionCreatorImpl
			.createSalableBookNotFoundException(O_ALQUIMISTA_ID);
		assertNotNull(exception);
		assertEquals(
			BOOK_NOT_FOUND_ID_MSG,
			exception.getLocalizedMessage());
	}
	
	@Test
	@DisplayName("Deve criar e retornar 'InsuficientSalableBookUnitsException'")
	void createInsuficientSalableBookUnitsException() {
		InsuficientSalableBookUnitsException exception = salableBookExceptionCreatorImpl
			.createInsuficientSalableBookUnitsException(UNITS);
		assertNotNull(exception);
		assertEquals(
			INSUFICIENT_BOOK_UNITS_MSG,
			exception.getLocalizedMessage());
	}
}