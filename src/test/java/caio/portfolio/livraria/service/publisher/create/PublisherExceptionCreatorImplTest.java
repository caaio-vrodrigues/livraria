package caio.portfolio.livraria.service.publisher.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import caio.portfolio.livraria.service.publisher.implementation.create.PublisherExceptionCreatorImpl;

@ExtendWith(MockitoExtension.class)
class PublisherExceptionCreatorImplTest {

	@InjectMocks private PublisherExceptionCreatorImpl publisherExceptionCreatorImpl;
	@Mock private MessageSource publisherMessageSource;
	
	private static final Long ROCCO_ID = 1L;
	private static final String ROCCO_NAME = "Rocco";
	private static final String ROCCO_END = "Rua do Passeio, 38, 11º andar, Centro, Rio de Janeiro - RJ";
	private static final String CONCURRENT_PUBLISHER_EXCEPTION_MESSAGE = "Falha ao tentar criar nova editora: `"+ROCCO_NAME+"`";
	private static final String PUBLISHER_ALREADY_EXISTS_EXCEPTION_MESSAGE = "Não foi possível criar nova editora. Editora com `fullAddress`: `"+ROCCO_END+"` já existe";
	private static final String PUBLISHER_NOT_FOUND_EXCEPTION_MESSAGE = "Não foi possível encontrar editora para o `id` fornecido";
	
	@Test
	@DisplayName("Deve lançar 'ConcurrentPublisherException'")
	void createConcurrentPublisherException() {
		when(publisherMessageSource
			.getMessage(
				anyString(), 
				any(Object[].class), 
				any(Locale.class)))
			.thenReturn(CONCURRENT_PUBLISHER_EXCEPTION_MESSAGE);
		assertEquals(
			CONCURRENT_PUBLISHER_EXCEPTION_MESSAGE, 
			publisherExceptionCreatorImpl
				.createConcurrentPublisherException(ROCCO_NAME)
					.getLocalizedMessage());
		verify(publisherMessageSource, times(1)).getMessage(
			anyString(), 
			any(Object[].class), 
			any(Locale.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'PublisherAlreadyExistsException'")
	void createPublisherAlreadyExistsException() {
		when(publisherMessageSource
			.getMessage(
				anyString(), 
				any(Object[].class), 
				any(Locale.class)))
			.thenReturn(PUBLISHER_ALREADY_EXISTS_EXCEPTION_MESSAGE);
		assertEquals(
			PUBLISHER_ALREADY_EXISTS_EXCEPTION_MESSAGE, 
			publisherExceptionCreatorImpl
				.createPublisherAlreadyExistsException(ROCCO_END)
					.getLocalizedMessage());
		verify(publisherMessageSource, times(1)).getMessage(
			anyString(), 
			any(Object[].class), 
			any(Locale.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'PublisherNotFoundException'")
	void createPublisherNotFoundException() {
		when(publisherMessageSource
			.getMessage(
				anyString(), 
				any(Object[].class), 
				any(Locale.class)))
			.thenReturn(PUBLISHER_NOT_FOUND_EXCEPTION_MESSAGE);
		assertEquals(
			PUBLISHER_NOT_FOUND_EXCEPTION_MESSAGE, 
			publisherExceptionCreatorImpl
				.createPublisherNotFoundException(ROCCO_ID)
					.getLocalizedMessage());
		verify(publisherMessageSource, times(1)).getMessage(
			anyString(), 
			any(Object[].class), 
			any(Locale.class));
	}
}