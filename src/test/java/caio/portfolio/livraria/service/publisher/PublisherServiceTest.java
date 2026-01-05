package caio.portfolio.livraria.service.publisher;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import caio.portfolio.livraria.exception.custom.publisher.ConcurrentPublisherException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.CreatePublisherDTO;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.ResponsePublisherDTO;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.UpdatePublisherDTO;
import caio.portfolio.livraria.infrastructure.repository.PublisherRepository;
import caio.portfolio.livraria.service.country.CountryService;
import caio.portfolio.livraria.service.publisher.model.create.PublisherExceptionCreator;
import caio.portfolio.livraria.service.publisher.model.create.ResponsePublisherDTOCreator;
import caio.portfolio.livraria.service.publisher.model.find.PublisherFinder;
import caio.portfolio.livraria.service.publisher.model.save.PublisherSaverAndConcurrencyHandle;
import caio.portfolio.livraria.service.publisher.model.validate.PublisherUpdateValidator;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {
	
	@InjectMocks private PublisherService service;
	@Mock private CountryService countryService;
	@Mock private PublisherRepository repo;
	@Mock private ResponsePublisherDTOCreator responsePublisherDTOCreator;
	@Mock private PublisherUpdateValidator publisherUpdateValidator;
	@Mock private PublisherSaverAndConcurrencyHandle publisherSaverAndConcurrencyHandle;
	@Mock private PublisherExceptionCreator publisherExceptionCreator;
	@Mock private PublisherFinder publisherFinder;
	
	private static final Long ROCCO_ID = 1L;
	private static final Long GLOBAL_BOOKS_ID = 2L;
	private static final String ROCCO_NAME = "Rocco";
	private static final String GLOBAL_BOOKS_NAME = "Global Books";
	private static final String BRAZIL_NAME = "Brazil";
	private static final String USA_NAME = "Estados Unidos";
	private static final String BRAZIL_ISO_CODE = "BR";
	private static final String USA_ISO_CODE = "US";
	private static final String ROCCO_FULL_ADDRESS = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	private static final String GLOBAL_BOOKS_FULL_ADDRESS = "123 Main Street, New York, USA";
	private static final String ROCCO_NEW_FULL_ADDRESS = "Rua do Passeio, 888, 8º andar, no Passeio Corporate";
	private static final String PUBLISHER_ALREADY_EXISTS_MSG = "Endereço de editora já em uso";
	private static final String PUBLISHER_NOT_FOUND_MSG = "Editora não encontrada";
	private static final Integer BRAZIL_ID = 1;
	private static final Integer USA_ID = 2;
	
	private static final Country BRAZIL = Country.builder()
		.id(BRAZIL_ID)
		.name(BRAZIL_NAME)
		.isoAlpha2Code(BRAZIL_ISO_CODE)
		.build(); 
	
	private static final Country USA = Country.builder()
		.id(USA_ID)
		.name(USA_NAME)
		.isoAlpha2Code(USA_ISO_CODE)
		.build();
	
	private static final Publisher ROCCO_PUBLISHER = Publisher.builder()
		.id(ROCCO_ID)
		.name(ROCCO_NAME)
		.country(BRAZIL)
		.fullAddress(ROCCO_FULL_ADDRESS)
		.build();
	
	private static final Publisher GLOBAL_BOOKS_PUBLISHER = Publisher.builder()
		.id(GLOBAL_BOOKS_ID)
		.name(GLOBAL_BOOKS_NAME)
		.fullAddress(GLOBAL_BOOKS_FULL_ADDRESS)
		.country(USA)
		.build();
	
	private static final Publisher UPDATED_ROCCO_PUBLISHER = Publisher.builder()
		.id(ROCCO_ID)
		.name(ROCCO_NAME)
		.country(BRAZIL)
		.fullAddress(ROCCO_NEW_FULL_ADDRESS)
		.build();
	
	private static final CreatePublisherDTO CREATE_ROCCO_DTO = CreatePublisherDTO
		.builder()
		.name(ROCCO_NAME)
		.fullAddress(ROCCO_FULL_ADDRESS)
		.countryId(BRAZIL_ID)
		.build();
	
	private static final ResponsePublisherDTO RESPONSE_ROCCO_DTO = ResponsePublisherDTO
		.builder()
		.id(ROCCO_ID)
		.name(ROCCO_NAME)
		.fullAddress(ROCCO_FULL_ADDRESS)
		.countryId(BRAZIL_ID)
		.build();
	
	private static final ResponsePublisherDTO RESPONSE_GLOBAL_BOOKS_DTO = ResponsePublisherDTO
		.builder()
		.id(ROCCO_ID)
		.name(ROCCO_NAME)
		.fullAddress(ROCCO_FULL_ADDRESS)
		.countryId(BRAZIL_ID)
		.build();
	
	private static final ResponsePublisherDTO UPDATED_RESPONSE_ROCCO_DTO = ResponsePublisherDTO
		.builder()
		.id(ROCCO_ID)
		.name(ROCCO_NAME)
		.countryId(BRAZIL_ID)
		.fullAddress(ROCCO_NEW_FULL_ADDRESS)
		.build();
	
	private static final UpdatePublisherDTO UPDATE_ROCCO_DTO = UpdatePublisherDTO.builder()
		.name(ROCCO_NAME)
		.countryId(null)
		.fullAddress(ROCCO_NEW_FULL_ADDRESS)
		.build();
	
	private static final List<Publisher> PUBLISHER_LIST = List.of(
		ROCCO_PUBLISHER, GLOBAL_BOOKS_PUBLISHER);

	@Test
	@DisplayName("Deve receber um 'CreatePublisherDTO' e retornar um 'ResponsePublisherDTO' após criação")
	void createPublisher_returnsResponsePublisherDTO() {
		when(repo.findByFullAddress(anyString()))
			.thenReturn(Optional.empty());
		when(countryService.getCountryById(anyInt()))
			.thenReturn(BRAZIL);
		when(publisherSaverAndConcurrencyHandle
				.saveAndHandlePublisherConcurrency(any(Publisher.class)))
			.thenReturn(ROCCO_PUBLISHER);
		when(responsePublisherDTOCreator
				.toResponsePublisherDTO(any(Publisher.class)))
			.thenReturn(RESPONSE_ROCCO_DTO);
		ResponsePublisherDTO responsePublisherDTO = service
			.createPublisher(CREATE_ROCCO_DTO);
		assertNotNull(responsePublisherDTO);
		assertEquals(
			RESPONSE_ROCCO_DTO.getId(), 
			responsePublisherDTO.getId());
		assertEquals(
			RESPONSE_ROCCO_DTO.getName(), 
			responsePublisherDTO.getName());
		assertEquals(
			RESPONSE_ROCCO_DTO.getFullAddress(), 
			responsePublisherDTO.getFullAddress());
		assertEquals(
			RESPONSE_ROCCO_DTO.getCountryId(), 
			responsePublisherDTO.getCountryId());
		verify(countryService)
			.getCountryById(anyInt());
		verify(responsePublisherDTOCreator)
			.toResponsePublisherDTO(any(Publisher.class));
	}
	
	@Test
	@DisplayName("Deve receber um 'CreatePublisherDTO' e lançar 'PublisherAlreadyExistsException' por 'fullAddress' já em uso")
	void createPublisher_throwsPublisherAlreadyExistsException() {
		when(repo.findByFullAddress(anyString()))
			.thenReturn(Optional.of(ROCCO_PUBLISHER));
		when(publisherExceptionCreator
				.createPublisherAlreadyExistsException(anyString()))
			.thenReturn(new PublisherAlreadyExistsException(PUBLISHER_ALREADY_EXISTS_MSG));
		assertThrows(
			PublisherAlreadyExistsException.class, 
			() -> service.createPublisher(CREATE_ROCCO_DTO));
		verify(repo, times(1)).findByFullAddress(anyString());
		verify(publisherExceptionCreator, times(1))
			.createPublisherAlreadyExistsException(anyString());
	}
	
	@Test
	@DisplayName("Deve receber um 'CreatePublisherDTO' e lançar 'ConcurrentPublisherException' por violação de concorrência")
	void createPublisher_throwsConcurrentPublisherException() {
		when(repo.findByFullAddress(anyString()))
			.thenReturn(Optional.empty());
		when(countryService.getCountryById(anyInt()))
			.thenReturn(BRAZIL);
		when(publisherSaverAndConcurrencyHandle
				.saveAndHandlePublisherConcurrency(any(Publisher.class)))
			.thenThrow(ConcurrentPublisherException.class);
		assertThrows(
			ConcurrentPublisherException.class, 
			() -> service.createPublisher(CREATE_ROCCO_DTO));
		verify(countryService)
			.getCountryById(anyInt());
		verify(responsePublisherDTOCreator, never())
			.toResponsePublisherDTO(any(Publisher.class));
	}
	
	@Test
	@DisplayName("Deve retornar lista de 'ResponsePublisherDTO' ao chamar método")
	void getAllPublishers_returnsResponsePublisherDTOList() {
		when(repo.findAll()).thenReturn(PUBLISHER_LIST);
		when(responsePublisherDTOCreator
				.toResponsePublisherDTO(any(Publisher.class)))
			.thenReturn(RESPONSE_ROCCO_DTO, RESPONSE_GLOBAL_BOOKS_DTO);
		List<ResponsePublisherDTO> responsePublisherDTOListResult = service.getAllResponsePublisherDTOs();
		assertEquals(2, responsePublisherDTOListResult.size());
		assertTrue(responsePublisherDTOListResult
			.contains(RESPONSE_ROCCO_DTO));
		assertTrue(responsePublisherDTOListResult
			.contains(RESPONSE_GLOBAL_BOOKS_DTO));
		verify(repo).findAll();
		verify(responsePublisherDTOCreator, times(2))
			.toResponsePublisherDTO(any(Publisher.class));
	}
	
	@Test
	@DisplayName("Deve retornar lista vazia ao chamar método")
	void getAllPublishers_returnsEmptyList() {
		when(repo.findAll()).thenReturn(List.of());
		List<ResponsePublisherDTO> responsePublisherDTOListResult = service.getAllResponsePublisherDTOs();
		assertEquals(0, responsePublisherDTOListResult.size());
		verify(repo).findAll();
		verify(responsePublisherDTOCreator, never())
			.toResponsePublisherDTO(any(Publisher.class));
	}
	
	@Test
	@DisplayName("Deve receber 'fullAddress' para buscar editora e retornar 'ResponsePublisherDTO'")
	void getPublisherByFullAddress_returnsResponsePublisherDTO() {
		when(publisherFinder.findByFullAddress(anyString()))
			.thenReturn(ROCCO_PUBLISHER);
		when(responsePublisherDTOCreator
				.toResponsePublisherDTO(any(Publisher.class)))
			.thenReturn(RESPONSE_ROCCO_DTO);
		ResponsePublisherDTO responsePublisherDTO = service
			.getResponsePublisherDTOByFullAddress(ROCCO_FULL_ADDRESS);
		assertNotNull(responsePublisherDTO);
		assertEquals(
			ROCCO_PUBLISHER.getId(), 
			responsePublisherDTO.getId());
		assertEquals(
			ROCCO_PUBLISHER.getName(), 
			responsePublisherDTO.getName());
		assertEquals(
			ROCCO_PUBLISHER.getFullAddress(), 
			responsePublisherDTO.getFullAddress());
		assertEquals(
			ROCCO_PUBLISHER.getCountry().getId(), 
			responsePublisherDTO.getCountryId());
		verify(publisherFinder, times(1))
			.findByFullAddress(anyString());
		verify(responsePublisherDTOCreator, times(1))
			.toResponsePublisherDTO(any(Publisher.class));
	}
	
	@Test
	@DisplayName("Deve receber 'fullAddress' para buscar editora e lançar 'PublisherNotFoundException' por não encontrar")
	void getPublisherByFullAddress_throwsPublisherNotFoundException() {
		when(publisherFinder.findByFullAddress(anyString()))
			.thenThrow(PublisherNotFoundException.class);
		assertThrows(
			PublisherNotFoundException.class,
			() -> service.getResponsePublisherDTOByFullAddress(ROCCO_FULL_ADDRESS));
		verify(publisherFinder, times(1))
			.findByFullAddress(anyString());
	}
	
	@Test
	@DisplayName("Deve receber 'id' para buscar editora e retornar 'ResponsePublisherDTO'")
	void getPublisherById_returnsResponsePublisherDTO() {
		when(publisherFinder.findById(anyLong()))
			.thenReturn(ROCCO_PUBLISHER);
		when(responsePublisherDTOCreator
				.toResponsePublisherDTO(any(Publisher.class)))
			.thenReturn(RESPONSE_ROCCO_DTO);
		ResponsePublisherDTO responsePublisherDTO = service
			.getResponsePublisherDTOById(ROCCO_ID);
		assertNotNull(responsePublisherDTO);
		assertEquals(
			ROCCO_PUBLISHER.getId(), 
			responsePublisherDTO.getId());
		assertEquals(
			ROCCO_PUBLISHER.getName(), 
			responsePublisherDTO.getName());
		assertEquals(
			ROCCO_PUBLISHER.getFullAddress(), 
			responsePublisherDTO.getFullAddress());
		assertEquals(
			ROCCO_PUBLISHER.getCountry().getId(), 
			responsePublisherDTO.getCountryId());
		verify(publisherFinder, times(1))
			.findById(anyLong());
		verify(responsePublisherDTOCreator, times(1))
			.toResponsePublisherDTO(any(Publisher.class));
	}
	
	@Test
	@DisplayName("Deve receber 'id' para buscar editora e lançar 'PublisherNotFoundException' por não encontrar")
	void getPublisherById_throwsPublisherNotFoundException() {
		when(publisherFinder.findById(anyLong()))
			.thenThrow(PublisherNotFoundException.class);
		assertThrows(
			PublisherNotFoundException.class,
			() -> service.getResponsePublisherDTOById(ROCCO_ID));
		verify(publisherFinder, times(1))
			.findById(anyLong());
	}
	
	@Test
	@DisplayName("Deve receber 'id' de editora existente e 'UpdatePublisherDTO' para atualização e retorno de 'ResponsePublisherDTO'")
	void updatePublisher_returnsResponsePublisherDTO() {
		when(publisherFinder.findById(anyLong()))
			.thenReturn(ROCCO_PUBLISHER);
		when(publisherUpdateValidator
				.validateName(anyString(), anyString()))
			.thenReturn(ROCCO_NAME);
		when(publisherUpdateValidator
				.validateCountry(BRAZIL, null))
			.thenReturn(BRAZIL);
		when(publisherUpdateValidator
				.validateFullAddress(anyString(), anyString()))
			.thenReturn(ROCCO_NEW_FULL_ADDRESS);
		when(publisherSaverAndConcurrencyHandle
				.saveAndHandlePublisherConcurrency(any(Publisher.class)))
			.thenReturn(UPDATED_ROCCO_PUBLISHER);
		when(responsePublisherDTOCreator
				.toResponsePublisherDTO(any(Publisher.class)))
			.thenReturn(UPDATED_RESPONSE_ROCCO_DTO);
		ResponsePublisherDTO responsePublisherDTO = service.updatePublisher(
			ROCCO_ID, 
			UPDATE_ROCCO_DTO);
		assertNotNull(responsePublisherDTO);
		assertEquals(
			ROCCO_PUBLISHER.getId(), 
			responsePublisherDTO.getId());
		assertEquals(
			ROCCO_PUBLISHER.getName(), 
			responsePublisherDTO.getName());
		assertEquals(
			ROCCO_NEW_FULL_ADDRESS, 
			responsePublisherDTO.getFullAddress());
		assertEquals(
			ROCCO_PUBLISHER.getCountry().getId(), 
			responsePublisherDTO.getCountryId());
		verify(publisherFinder, times(1))
			.findById(anyLong());
		verify(publisherUpdateValidator, times(1))
			.validateName(anyString(), anyString());
		verify(publisherUpdateValidator, times(1))
			.validateCountry(BRAZIL, null);
		verify(publisherUpdateValidator, times(1))
			.validateFullAddress(anyString(), anyString());
		verify(publisherSaverAndConcurrencyHandle, times(1))
			.saveAndHandlePublisherConcurrency(any(Publisher.class));
		verify(responsePublisherDTOCreator, times(1))
			.toResponsePublisherDTO(any(Publisher.class));
	}
	
	@Test
	@DisplayName("Deve receber 'id' de editora não existente e lançar 'PublisherNotFoundException'")
	void updatePublisher_throwsPublisherNotFoundException() {
		when(publisherFinder.findById(anyLong()))
			.thenThrow(PublisherNotFoundException.class);
		assertThrows(
			PublisherNotFoundException.class, 
			() -> service.updatePublisher(ROCCO_ID, UPDATE_ROCCO_DTO));
		verify(publisherFinder, times(1))
			.findById(anyLong());
	}
	
	@Test
	@DisplayName("Deve deletar editora por 'id' com sucesso e retornar true")
	void deletePublisherById_returnsTrue() {
		when(repo.existsById(anyLong()))
			.thenReturn(true);
		assertTrue(service.deletePublisherById(ROCCO_ID));
		verify(repo, times(1)).deleteById(anyLong());
	}
	
	@Test
	@DisplayName("Deve lançar 'PublisherNotFoundException' ao tentar deletar por 'id'")
	void deletePublisherById_throwsPublisherNotFoundException() {
		when(repo.existsById(anyLong()))
			.thenReturn(false);
		when(publisherExceptionCreator
				.createPublisherNotFoundException(anyLong()))
			.thenReturn(new PublisherNotFoundException(PUBLISHER_NOT_FOUND_MSG));
		assertThrows(
			PublisherNotFoundException.class,
			() -> service.deletePublisherById(ROCCO_ID));
		verify(repo, times(1)).existsById(anyLong());
		verify(publisherExceptionCreator, times(1))
			.createPublisherNotFoundException(anyLong());
	}
}