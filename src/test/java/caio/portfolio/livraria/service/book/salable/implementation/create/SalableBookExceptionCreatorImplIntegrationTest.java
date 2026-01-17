package caio.portfolio.livraria.service.book.salable.implementation.create;

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

import caio.portfolio.livraria.exception.custom.book.salable.ConcurrentSalableBookException;
import caio.portfolio.livraria.exception.custom.book.salable.InsuficientSalableBookUnitsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class SalableBookExceptionCreatorImplIntegrationTest {

	@Autowired private SalableBookExceptionCreatorImpl salableBookExceptionCreatorImpl;
	@Autowired private MessageSource salableBookMessageSource;
	
	private static final int UNITS = 100;
	private static final Long O_ALQUIMISTA_ID = 1L;
	private static final String PAULO_COELHO_FULL_NAME = "Paulo Coelho";
	private static final String O_ALQUIMISTA_TITLE = "O Alquimista";
	
	@Test
	@DisplayName("Deve criar e retornar 'SalableBookAlreadyExistsException'")
	void createSalableBookAlreadyExistsException() {
		String expectedMsg = salableBookMessageSource.getMessage(
	        "book.already.exists.tiltleAndAuthor",
	        new Object[]{O_ALQUIMISTA_TITLE, PAULO_COELHO_FULL_NAME},
	        LocaleContextHolder.getLocale());
		SalableBookAlreadyExistsException exception = salableBookExceptionCreatorImpl
			.createSalableBookAlreadyExistsException(
				PAULO_COELHO_FULL_NAME, 
				O_ALQUIMISTA_TITLE);
		assertNotNull(exception);
		assertEquals(expectedMsg, exception.getLocalizedMessage());
	}
	
	@Test
	@DisplayName("Deve criar e retornar 'ConcurrentSalableBookException'")
	void createConcurrentSalableBookException() {
		String expectedMsg = salableBookMessageSource.getMessage(
	        "concurrent.book",
	        new Object[]{O_ALQUIMISTA_TITLE},
	        LocaleContextHolder.getLocale());
		ConcurrentSalableBookException exception = salableBookExceptionCreatorImpl
			.createConcurrentSalableBookException(O_ALQUIMISTA_TITLE);
		assertNotNull(exception);
		assertEquals(expectedMsg, exception.getLocalizedMessage());
	}
	
	@Test
	@DisplayName("Deve criar e retornar 'SalableBookNotFoundException'")
	void createSalableBookNotFoundException() {
		String expectedMsg = salableBookMessageSource.getMessage(
	        "book.not.found.id",
	        new Object[]{O_ALQUIMISTA_ID},
	        LocaleContextHolder.getLocale());
		SalableBookNotFoundException exception = salableBookExceptionCreatorImpl
			.createSalableBookNotFoundException(O_ALQUIMISTA_ID);
		assertNotNull(exception);
		assertEquals(expectedMsg, exception.getLocalizedMessage());
	}
	
	@Test
	@DisplayName("Deve criar e retornar 'InsuficientSalableBookUnitsException'")
	void createInsuficientSalableBookUnitsException() {
		String expectedMsg = salableBookMessageSource.getMessage(
	        "insuficient.book.units",
	        new Object[]{UNITS},
	        LocaleContextHolder.getLocale());
		InsuficientSalableBookUnitsException exception = salableBookExceptionCreatorImpl
			.createInsuficientSalableBookUnitsException(UNITS);
		assertNotNull(exception);
		assertEquals(expectedMsg, exception.getLocalizedMessage());
	}
}