package caio.portfolio.livraria.service.publisher.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import caio.portfolio.livraria.exception.custom.publisher.PublisherNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.repository.PublisherRepository;
import caio.portfolio.livraria.service.publisher.implementation.find.PublisherFinderImpl;

@ExtendWith(MockitoExtension.class)
class PublisherFinderImplTest {

	@InjectMocks private PublisherFinderImpl publisherFinderImpl;
	@Mock private PublisherRepository repo;
	@Mock private MessageSource publisherMessageSource;
	
	private static final Long ROCCO_ID = 1L;
	private static final String ROCCO_NAME = "Rocco";
	private static final String ROCCO_END = "Rua do Passeio, 38, 11º andar, Centro, Rio de Janeiro - RJ";
	private static final String PUBLISHER_NOT_FOUND_ADDRESS_MSG = "Não foi possível encontrar editora com `fullAddress`: `"+ROCCO_END+"`";
	private static final String PUBLISHER_NOT_FOUND_ID_MSG = "Não foi possível encontrar editora com `id`: `"+ROCCO_ID+"`";
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_ISO_CODE = "BR";
	private static final Integer BRAZIL_ID = 1;
	
	private static final Country BRAZIL = Country.builder()
		.id(BRAZIL_ID)
		.name(BRAZIL_NAME)
		.isoAlpha2Code(BRAZIL_ISO_CODE)
		.build(); 
	
	private static final Publisher ROCCO_PUBLISHER = Publisher.builder()
		.id(ROCCO_ID)
		.name(ROCCO_NAME)
		.country(BRAZIL)
		.fullAddress(ROCCO_END)
		.build();
	
	@Test
	@DisplayName("Deve buscar editora por 'fullAddres' e retorna-la")
	void findByFullAddress_returnPublisher() {
		when(publisherMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(PUBLISHER_NOT_FOUND_ADDRESS_MSG);
		when(repo.findByFullAddress(anyString()))
			.thenReturn(Optional.of(ROCCO_PUBLISHER));
		Publisher rocco = publisherFinderImpl.findByFullAddress(ROCCO_END);
		assertNotNull(rocco);
		assertEquals(ROCCO_NAME, rocco.getName());
		assertEquals(ROCCO_END, rocco.getFullAddress());
		verify(publisherMessageSource, times(1)).getMessage(
			anyString(), 
			any(Object[].class),
			any(Locale.class));
		verify(repo, times(1)).findByFullAddress(anyString());
	}
	
	@Test
	@DisplayName("Deve lançar 'PublisherNotFoundException' ao buscar editora por 'fullAddres'")
	void findByFullAddress_throwsPublisherNotFoundException() {
		when(publisherMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(PUBLISHER_NOT_FOUND_ADDRESS_MSG);
		when(repo.findByFullAddress(anyString()))
			.thenReturn(Optional.empty());
		assertThrows(
			PublisherNotFoundException.class,
			() -> publisherFinderImpl.findByFullAddress(ROCCO_END));
		verify(publisherMessageSource, times(1)).getMessage(
			anyString(), 
			any(Object[].class),
			any(Locale.class));
		verify(repo, times(1)).findByFullAddress(anyString());
	}
	
	@Test
	@DisplayName("Deve buscar editora por 'id' e retorna-la")
	void findById_returnPublisher() {
		when(publisherMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(PUBLISHER_NOT_FOUND_ID_MSG);
		when(repo.findById(anyLong()))
			.thenReturn(Optional.of(ROCCO_PUBLISHER));
		Publisher rocco = publisherFinderImpl.findById(ROCCO_ID);
		assertNotNull(rocco);
		assertEquals(ROCCO_NAME, rocco.getName());
		assertEquals(ROCCO_END, rocco.getFullAddress());
		verify(publisherMessageSource, times(1)).getMessage(
			anyString(), 
			any(Object[].class),
			any(Locale.class));
		verify(repo, times(1)).findById(anyLong());
	}
	
	@Test
	@DisplayName("Deve lançar 'PublisherNotFoundException' ao buscar editora por 'id'")
	void findById_throwsPublisherNotFoundException() {
		when(publisherMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(PUBLISHER_NOT_FOUND_ID_MSG);
		when(repo.findById(anyLong()))
			.thenReturn(Optional.empty());
		assertThrows(
			PublisherNotFoundException.class,
			() -> publisherFinderImpl.findById(ROCCO_ID));
		verify(publisherMessageSource, times(1)).getMessage(
			anyString(), 
			any(Object[].class),
			any(Locale.class));
		verify(repo, times(1)).findById(anyLong());
	}
}