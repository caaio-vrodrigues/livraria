package caio.portfolio.livraria.service.publisher.implementation.create;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class PublisherExceptionCreatorImplIntegrationTest {

	@Autowired private PublisherExceptionCreatorImpl publisherExceptionCreatorImpl;
	
	private static final Long ROCCO_ID = 1L;
	private static final String ROCCO_NAME = "Rocco";
	private static final String ROCCO_END = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	private static final String CONCURRENT_PUBLISHER_EXCEPTION_MESSAGE = "Falha ao tentar criar nova editora: `"+ROCCO_NAME+"`";
	private static final String PUBLISHER_ALREADY_EXISTS_EXCEPTION_MESSAGE = "Não foi possível criar nova editora. Editora com `fullAddress`: `"+ROCCO_END+"` já existe";
	private static final String PUBLISHER_NOT_FOUND_EXCEPTION_MESSAGE = "Não foi possível encontrar editora com `id`: `"+ROCCO_ID+"`";
	
	@Test
	@DisplayName("Deve lançar 'ConcurrentPublisherException'")
	void createConcurrentPublisherException() {
		assertEquals(
			CONCURRENT_PUBLISHER_EXCEPTION_MESSAGE, 
			publisherExceptionCreatorImpl
				.createConcurrentPublisherException(ROCCO_NAME)
					.getLocalizedMessage());
	}
	
	@Test
	@DisplayName("Deve lançar 'PublisherAlreadyExistsException'")
	void createPublisherAlreadyExistsException() {
		assertEquals(
			PUBLISHER_ALREADY_EXISTS_EXCEPTION_MESSAGE, 
			publisherExceptionCreatorImpl
				.createPublisherAlreadyExistsException(ROCCO_END)
					.getLocalizedMessage());
	}
	
	@Test
	@DisplayName("Deve lançar 'PublisherNotFoundException'")
	void createPublisherNotFoundException() {
		assertEquals(
			PUBLISHER_NOT_FOUND_EXCEPTION_MESSAGE, 
			publisherExceptionCreatorImpl
				.createPublisherNotFoundException(ROCCO_ID)
					.getLocalizedMessage());
	}
}