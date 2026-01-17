package caio.portfolio.livraria.service.publisher.implementation.create;

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

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class PublisherExceptionCreatorImplIntegrationTest {

	@Autowired private PublisherExceptionCreatorImpl publisherExceptionCreatorImpl;
	@Autowired private MessageSource publisherMessageSource;
	
	private static final Long ROCCO_ID = 1L;
	private static final String ROCCO_NAME = "Rocco";
	private static final String ROCCO_END = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	
	@Test
	@DisplayName("Deve lançar 'ConcurrentPublisherException'")
	void createConcurrentPublisherException() {
		String expectedMsg = publisherMessageSource.getMessage(
			"publisher.concurrent", 
			new Object[]{ROCCO_NAME},
			LocaleContextHolder.getLocale());
		String msg = publisherExceptionCreatorImpl
			.createConcurrentPublisherException(ROCCO_NAME)
			.getLocalizedMessage();
		assertNotNull(msg);
		assertEquals(expectedMsg, msg);
	}
	
	@Test
	@DisplayName("Deve lançar 'PublisherAlreadyExistsException'")
	void createPublisherAlreadyExistsException() {
		String expectedMsg = publisherMessageSource.getMessage(
			"publisher.already.exists.fullAddress", 
			new Object[]{ROCCO_END},
			LocaleContextHolder.getLocale());
		String msg = publisherExceptionCreatorImpl
			.createPublisherAlreadyExistsException(ROCCO_END)
			.getLocalizedMessage();
		assertNotNull(msg);
		assertEquals(expectedMsg, msg);
	}
	
	@Test
	@DisplayName("Deve lançar 'PublisherNotFoundException'")
	void createPublisherNotFoundException() {
		String expectedMsg = publisherMessageSource.getMessage(
			"publisher.not.found.id.throws", 
			new Object[]{ROCCO_ID},
			LocaleContextHolder.getLocale());
		String msg = publisherExceptionCreatorImpl
			.createPublisherNotFoundException(ROCCO_ID)
			.getLocalizedMessage();
		assertNotNull(msg);
		assertEquals(expectedMsg, msg);
	}
}