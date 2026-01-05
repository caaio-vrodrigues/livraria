package caio.portfolio.livraria.service.publisher.save;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import caio.portfolio.livraria.exception.custom.publisher.ConcurrentPublisherException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.repository.PublisherRepository;
import caio.portfolio.livraria.service.publisher.implementation.save.PublisherSaverAndConcurrencyHandleImpl;
import caio.portfolio.livraria.service.publisher.model.create.PublisherExceptionCreator;

@ExtendWith(MockitoExtension.class)
class PublisherSaverAndConcurrencyHandleImplTest {

	@InjectMocks private PublisherSaverAndConcurrencyHandleImpl publisherSaverAndConcurrencyHandleImpl;
	@Mock private PublisherRepository repo;
	@Mock private PublisherExceptionCreator publisherExceptionCreator;
	
	private static final Long ROCCO_ID = 1L;
	private static final String ROCCO_NAME = "Rocco";
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_ISO_CODE = "BR";
	private static final String ROCCO_FULL_ADDRESS = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	private static final String CONCURRENT_PUBLISHER_EXCEPTION_MSG = "Falha de concorrência";
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
		.fullAddress(ROCCO_FULL_ADDRESS)
		.build();
	
	@Test
	@DisplayName("Deve salvar e retornar editora com sucesso")
	void saveAndHandlePublisherConcurrency_returnsPublisher() {
		when(repo.saveAndFlush(any(Publisher.class)))
			.thenReturn(ROCCO_PUBLISHER);
		Publisher rocco = publisherSaverAndConcurrencyHandleImpl
			.saveAndHandlePublisherConcurrency(ROCCO_PUBLISHER);
		assertNotNull(rocco);
		assertEquals(
			ROCCO_FULL_ADDRESS, 
			rocco.getFullAddress());
		verify(repo, times(1)).saveAndFlush(any(Publisher.class));
	}
	
	@Test
	@DisplayName("Deve retornar editora existente após falha ao tentar salvar nova editora")
	void saveAndHandlePublisherConcurrency_returnsPublisherAfterFail() {
		when(repo.saveAndFlush(any(Publisher.class)))
			.thenThrow(DataIntegrityViolationException.class);
		when(repo.findByFullAddress(anyString()))
			.thenReturn(Optional.of(ROCCO_PUBLISHER));
		Publisher rocco = publisherSaverAndConcurrencyHandleImpl
			.saveAndHandlePublisherConcurrency(ROCCO_PUBLISHER);
		assertNotNull(rocco);
		assertEquals(
			ROCCO_FULL_ADDRESS, 
			rocco.getFullAddress());
		verify(repo, times(1)).saveAndFlush(any(Publisher.class));
		verify(repo, times(1)).findByFullAddress(anyString());
	}
	
	@Test
	@DisplayName("Deve lançar 'ConcurrentPublisherException' após falha ao tentar salvar nova editora")
	void saveAndHandlePublisherConcurrency_throwsConcurrentPublisherException() {
		when(repo.saveAndFlush(any(Publisher.class)))
			.thenThrow(DataIntegrityViolationException.class);
		when(repo.findByFullAddress(anyString()))
			.thenReturn(Optional.empty());
		when(publisherExceptionCreator
				.createConcurrentPublisherException(anyString()))
			.thenReturn(new ConcurrentPublisherException(CONCURRENT_PUBLISHER_EXCEPTION_MSG));
		assertThrows(
			ConcurrentPublisherException.class,
			() -> publisherSaverAndConcurrencyHandleImpl
				.saveAndHandlePublisherConcurrency(ROCCO_PUBLISHER));
		verify(repo, times(1)).saveAndFlush(any(Publisher.class));
		verify(repo, times(1)).findByFullAddress(anyString());
		verify(publisherExceptionCreator, times(1))
			.createConcurrentPublisherException(anyString());
	}
}