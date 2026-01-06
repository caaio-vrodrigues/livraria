package caio.portfolio.livraria.service.author;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.exception.custom.author.ConcurrentAuthorException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.author.dto.CreateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.UpdateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import caio.portfolio.livraria.service.author.implementation.validate.AuthorUpdateValidatorImpl;
import caio.portfolio.livraria.service.author.model.create.AuthorExceptionCreator;
import caio.portfolio.livraria.service.author.model.create.ResponseAuthorDTOCreator;
import caio.portfolio.livraria.service.author.model.find.AuthorFinder;
import caio.portfolio.livraria.service.author.model.save.AuthorSaverAndConcurrencyHandle;
import caio.portfolio.livraria.service.country.CountryService;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
	
	@InjectMocks private AuthorService authorService;
	@Mock private AuthorUpdateValidatorImpl authorupdateValidator;
	@Mock private CountryService countryService;
	@Mock private AuthorRepository repo;
	@Mock private ResponseAuthorDTOCreator responseAuthorDTOCreator;
	@Mock private AuthorSaverAndConcurrencyHandle saverAndConcurrencyHandle;
	@Mock private AuthorExceptionCreator authorExceptionCreator;
	@Mock private AuthorFinder authorFinder;
	
	private static final Long PAULO_COELHO_ID = 1L;
	private static final Long CAIO_ID = 2L;
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	private static final String PAULO_COLEHO_ALIAS = "O Mago";
	private static final String PAULO_COELHO_UPDATED_ALIAS = "PC";
	private static final String CAIO_ALIAS = "CVR";
	private static final String PAULO_COELHO_FULL_NAME = "Paulo Coelho";
	private static final String PAULO_COELHO_UPDATED_FULL_NAME = "Coelho, Paulo.";
	private static final String CAIO_FULL_NAME = "Caio Vinicius Rodrigues";
	private static final Integer BRAZIL_ID = 1;
	private static final LocalDate PAULO_COELHO_BIRTHDAY = LocalDate.of(1947, 8, 24);
	private static final LocalDate CAIO_BIRTHDAY = LocalDate.of(1992, 03, 20);
	
	private static final String AUTHOR_ALREADY_EXISTS_MSG = "`alias`: `"+PAULO_COLEHO_ALIAS+"` já está sendo utilizado pelo autor: `"+PAULO_COELHO_FULL_NAME+"`";
	private static final String AUTHOR_NOT_FOUND_MSG = "Não foi possível encontrar um autor com `id`: `"+PAULO_COELHO_ID+"`";
	private static final String AUTHOR_NOT_FOUND_MSG_ALIAS = "Não foi possível encontrar um autor com `alias`: `"+PAULO_COLEHO_ALIAS+"`";
	
	private static final Country BRAZIL = Country.builder()
		.id(BRAZIL_ID)
		.isoAlpha2Code(BRAZIL_CODE)
		.name(BRAZIL_NAME)
		.build(); 
	
	private static final Author PAULO_COELHO = Author.builder()
		.id(PAULO_COELHO_ID)
		.alias(PAULO_COLEHO_ALIAS)
		.fullName(PAULO_COELHO_FULL_NAME)
		.birthday(PAULO_COELHO_BIRTHDAY)
		.country(BRAZIL)
		.build();
	
	private static final Author CAIO_VINICIUS_RODRIGUES = Author.builder()
		.id(CAIO_ID)
		.alias(CAIO_ALIAS)
		.fullName(CAIO_FULL_NAME)
		.birthday(CAIO_BIRTHDAY)
		.country(BRAZIL)
		.build();
	
	private static final ResponseAuthorDTO RESPONSE_PAULO_COELHO_DTO = ResponseAuthorDTO
		.builder()
		.id(PAULO_COELHO.getId())
		.alias(PAULO_COELHO.getAlias())
		.fullName(PAULO_COELHO.getFullName())
		.birthday(PAULO_COELHO.getBirthday())
		.countryId(PAULO_COELHO.getCountry().getId())
		.build();
	
	private static final ResponseAuthorDTO RESPONSE_CAIO_VINICIUS_RODRIGUES_DTO = ResponseAuthorDTO
		.builder()
		.id(CAIO_VINICIUS_RODRIGUES.getId())
		.alias(CAIO_VINICIUS_RODRIGUES.getAlias())
		.fullName(CAIO_VINICIUS_RODRIGUES.getFullName())
		.birthday(CAIO_VINICIUS_RODRIGUES.getBirthday())
		.countryId(CAIO_VINICIUS_RODRIGUES.getCountry().getId())
		.build();
	
	private static final CreateAuthorDTO CREATE_PAULO_COELHO_DTO = CreateAuthorDTO
		.builder()
		.alias(PAULO_COLEHO_ALIAS)
		.fullName(PAULO_COELHO_FULL_NAME)
		.birthday(PAULO_COELHO_BIRTHDAY)
		.countryId(BRAZIL_ID)
		.build();
	
	private static final UpdateAuthorDTO UPDATE_PAULO_COELHO = UpdateAuthorDTO
		.builder()
		.alias(PAULO_COELHO_UPDATED_ALIAS)
		.fullName(PAULO_COELHO_UPDATED_FULL_NAME)
		.birthday(PAULO_COELHO_BIRTHDAY)
		.build();
	
	private static final Author UPDATED_PAULO_COELHO = Author.builder()
		.id(PAULO_COELHO.getId())
		.alias(UPDATE_PAULO_COELHO.getAlias())
		.fullName(UPDATE_PAULO_COELHO.getFullName())
		.birthday(UPDATE_PAULO_COELHO.getBirthday())
		.country(PAULO_COELHO.getCountry())
		.build();
	
	private static final ResponseAuthorDTO UPDATE_PAULO_COELHO_DTO = ResponseAuthorDTO
		.builder()
		.id(UPDATED_PAULO_COELHO.getId())
		.alias(UPDATED_PAULO_COELHO.getAlias())
		.fullName(UPDATED_PAULO_COELHO.getFullName())
		.birthday(UPDATED_PAULO_COELHO.getBirthday())
		.countryId(UPDATED_PAULO_COELHO.getCountry().getId())
		.build();
	
	private static final List<Author> AUTHOR_LIST = List.of(
		PAULO_COELHO, 
		CAIO_VINICIUS_RODRIGUES);

	@Test
	@DisplayName("Deve receber 'CreateAuthorDTO' e retornar 'ResponseAuthorDTO' após processo de criação e salvamento")
	void createAuthor_returnsResponseAuthorDTO() {
		when(repo.findByAlias(anyString()))
			.thenReturn(Optional.empty());
		when(countryService.getCountryById(anyInt()))
			.thenReturn(BRAZIL);
		when(saverAndConcurrencyHandle
			.saveAndHandleConcurrentyAuthor(any(Author.class)))
				.thenReturn(PAULO_COELHO);
		when(responseAuthorDTOCreator.toResponseAuthorDTO(any(Author.class)))
			.thenReturn(RESPONSE_PAULO_COELHO_DTO);
		ResponseAuthorDTO pauloCoelhoResponse = authorService
			.createAuthor(CREATE_PAULO_COELHO_DTO);
		assertNotNull(pauloCoelhoResponse);
		assertEquals(
			RESPONSE_PAULO_COELHO_DTO, 
			pauloCoelhoResponse);
		assertEquals(
			PAULO_COLEHO_ALIAS, 
			pauloCoelhoResponse.getAlias());
		assertEquals(
			PAULO_COELHO_FULL_NAME, 
			pauloCoelhoResponse.getFullName());
		assertEquals(
			PAULO_COELHO_ID, 
			pauloCoelhoResponse.getId());
		verify(repo, times(1))
			.findByAlias(anyString());
		verify(countryService, times(1))
			.getCountryById(anyInt());
		verify(saverAndConcurrencyHandle, times(1))
			.saveAndHandleConcurrentyAuthor(any(Author.class));
		verify(responseAuthorDTOCreator, times(1))
			.toResponseAuthorDTO(PAULO_COELHO);
	}
	
	@Test
	@DisplayName("Deve receber 'CreateAuthorDTO' e retornar 'AuthorAlreadyExistsException' após verificar autor já existente")
	void createAuthor_throwsAuthorAlreadyExistsException() {
		when(repo.findByAlias(anyString()))
			.thenReturn(Optional.of(PAULO_COELHO));
		when(authorExceptionCreator
			.createAuthorAlreadyExistsException(
				anyString(), 
				anyString()))
			.thenReturn(new AuthorAlreadyExistsException(AUTHOR_ALREADY_EXISTS_MSG));
		assertThrows(
			AuthorAlreadyExistsException.class, 
			() -> authorService.createAuthor(CREATE_PAULO_COELHO_DTO));
		verify(repo, times(1))
			.findByAlias(anyString());
		verify(authorExceptionCreator, times(1))
			.createAuthorAlreadyExistsException(
				anyString(), 
				anyString());
	}
	
	@Test
    @DisplayName("Deve lançar 'ConcurrentAuthorException' ao tentar salvar autor")
    void createAuthor_throwsConcurrentAuthorException() {
		when(repo.findByAlias(anyString()))
			.thenReturn(Optional.empty());
		when(countryService.getCountryById(anyInt()))
			.thenReturn(BRAZIL);
		when(saverAndConcurrencyHandle
			.saveAndHandleConcurrentyAuthor(any(Author.class)))
				.thenThrow(new ConcurrentAuthorException("Erro de persistência"));
		assertThrows(
			ConcurrentAuthorException.class,
            () -> authorService.createAuthor(CREATE_PAULO_COELHO_DTO)
        );
		verify(repo, times(1)).findByAlias(PAULO_COLEHO_ALIAS);
	    verify(countryService).getCountryById(BRAZIL_ID);
	    verify(saverAndConcurrencyHandle)
	    	.saveAndHandleConcurrentyAuthor(any(Author.class));
	    verify(responseAuthorDTOCreator, never())
	    	.toResponseAuthorDTO(any(Author.class));
	}
	
	@Test
	@DisplayName("Deve retornar uma lista de 'ResponseAuthorDTO' ao chamar método sem argumentos")
	void getAllAuthors_returnsResponseAuthorDTOList() {
		when(repo.findAll()).thenReturn(AUTHOR_LIST);
		when(responseAuthorDTOCreator.toResponseAuthorDTO(any(Author.class)))
			.thenReturn(
				RESPONSE_PAULO_COELHO_DTO, 
				RESPONSE_CAIO_VINICIUS_RODRIGUES_DTO);
		List<ResponseAuthorDTO> responseAuthorDTOList = authorService
			.getAllResponseAuthorDTOs();
		assertNotNull(responseAuthorDTOList);
		assertEquals(2, responseAuthorDTOList.size());
		assertEquals(
			RESPONSE_PAULO_COELHO_DTO, 
			responseAuthorDTOList.get(0));
		assertEquals(
			RESPONSE_CAIO_VINICIUS_RODRIGUES_DTO, 
			responseAuthorDTOList.get(1));
		verify(repo).findAll();
		verify(responseAuthorDTOCreator, times(2))
			.toResponseAuthorDTO(any(Author.class));
	}
	
	@Test
	@DisplayName("Deve retornar uma lista vazia ao chamar método sem argumentos")
	void getAllAuthors_returnsEmptyList() {
		when(repo.findAll()).thenReturn(List.of());
		List<ResponseAuthorDTO> responseAuthorDTOList = authorService
			.getAllResponseAuthorDTOs();
		assertNotNull(responseAuthorDTOList);
		assertEquals(0, responseAuthorDTOList.size());
		verify(repo).findAll();
		verify(responseAuthorDTOCreator, never())
			.toResponseAuthorDTO(any(Author.class));
	}
	
	@Test
	@DisplayName("Deve retornar 'ResponseAuthorDTO' ao buscar por 'id'")
	void getAuthorById_returnsResponseAuthorDTO() {
		when(authorFinder.findById(anyLong()))
			.thenReturn(PAULO_COELHO);
		when(responseAuthorDTOCreator
				.toResponseAuthorDTO(any(Author.class)))
			.thenReturn(RESPONSE_PAULO_COELHO_DTO);
		ResponseAuthorDTO responseAuthorDTO = authorService
			.getResponseAuthorDTOById(PAULO_COELHO_ID);
		assertNotNull(responseAuthorDTO);
		assertEquals(
			PAULO_COLEHO_ALIAS, 
			responseAuthorDTO.getAlias());
		assertEquals(
			PAULO_COELHO_FULL_NAME, 
			responseAuthorDTO.getFullName());
		assertEquals(
			PAULO_COELHO_BIRTHDAY, 
			responseAuthorDTO.getBirthday());
		assertEquals(
			BRAZIL_ID, 
			responseAuthorDTO.getCountryId());
		verify(authorFinder, times(1))
			.findById(anyLong());
		verify(responseAuthorDTOCreator, times(1))
			.toResponseAuthorDTO(any(Author.class));
	}
	
	@Test
	@DisplayName("Deve retornar autor ao buscar por 'id'")
	void getAuthorById_returnsAuthor() {
		when(authorFinder.findById(anyLong()))
			.thenReturn(PAULO_COELHO);
		Author pauloCoelho = authorService.getAuthorById(PAULO_COELHO_ID);
		assertNotNull(pauloCoelho);
		assertEquals(
			PAULO_COLEHO_ALIAS,
			pauloCoelho.getAlias());
		verify(authorFinder, times(1))
			.findById(anyLong());
	}
	
	@Test
	@DisplayName("Deve retornar 'AuthorNotFoundException' ao buscar por 'id'")
	void getAuthorById_throwsAuthorNotFoundException() {
		when(authorFinder.findById(anyLong()))
			.thenThrow(new AuthorNotFoundException(AUTHOR_NOT_FOUND_MSG));
		assertThrows(
			AuthorNotFoundException.class, 
			() -> authorService.getAuthorById(PAULO_COELHO_ID));
		verify(authorFinder, times(1))
			.findById(anyLong());
	}
	
	@Test
	@DisplayName("Deve retornar 'ResponseAuthorDTO' ao buscar por 'alias'")
	void getAuthorByAlias_returnsResponseAuthorDTO() {
		when(authorFinder.findByAlias(anyString()))
			.thenReturn(PAULO_COELHO);
		when(responseAuthorDTOCreator
				.toResponseAuthorDTO(any(Author.class)))
			.thenReturn(RESPONSE_PAULO_COELHO_DTO);
		ResponseAuthorDTO author = authorService
			.getResponseAuthorDTOByAlias(PAULO_COLEHO_ALIAS);
		assertNotNull(author);
		assertEquals(
			PAULO_COLEHO_ALIAS, 
			author.getAlias());
		assertEquals(
			PAULO_COELHO_FULL_NAME, 
			author.getFullName());
		assertEquals(
			PAULO_COELHO_BIRTHDAY, 
			author.getBirthday());
		assertEquals(
			BRAZIL_ID, 
			author.getCountryId());
		verify(authorFinder).findByAlias(anyString());
		verify(responseAuthorDTOCreator)
			.toResponseAuthorDTO(any(Author.class));
	}
	
	@Test
	@DisplayName("Deve retornar 'AuthorNotFoundException' ao buscar por 'alias'")
	void getAuthorByAlias_throwsAuthorNotFoundException() {
		when(authorFinder.findByAlias(anyString()))
			.thenThrow(new AuthorNotFoundException(AUTHOR_NOT_FOUND_MSG_ALIAS));
		assertThrows(
			AuthorNotFoundException.class, 
			() -> authorService.getResponseAuthorDTOByAlias(PAULO_COLEHO_ALIAS));
		verify(authorFinder, times(1))
			.findByAlias(anyString());
	}
	
	@Test
	@DisplayName("Deve atualizar autor por 'id' e retornar 'ResponseAuthorDTO'")
	void updateAuthor_returnsResponseAuthorDTO() {
		when(authorFinder.findById(anyLong()))
			.thenReturn(PAULO_COELHO);
		when(authorupdateValidator
			.validateAlias(
				anyString(), 
				anyString()))
			.thenReturn(PAULO_COELHO_UPDATED_ALIAS);
		when(authorupdateValidator
			.validateFullName(
				anyString(), 
				anyString()))
			.thenReturn(PAULO_COELHO_UPDATED_FULL_NAME);
		when(authorupdateValidator
			.validateBirthday(
				any(LocalDate.class), 
				any(LocalDate.class)))
			.thenReturn(PAULO_COELHO_BIRTHDAY);
		when(authorupdateValidator
				.validateCountry(BRAZIL, null))
			.thenReturn(BRAZIL);
		when(saverAndConcurrencyHandle
				.saveAndHandleConcurrentyAuthor(any(Author.class)))
			.thenReturn(PAULO_COELHO);
		when(responseAuthorDTOCreator
				.toResponseAuthorDTO(any(Author.class)))
			.thenReturn(UPDATE_PAULO_COELHO_DTO);
		ResponseAuthorDTO responseAuthorDTO = authorService
			.updateAuthor(
				PAULO_COELHO_ID, 
				UPDATE_PAULO_COELHO);
		assertNotNull(responseAuthorDTO);
		assertEquals(
			UPDATED_PAULO_COELHO.getId(), 
			responseAuthorDTO.getId());
		assertEquals(
			UPDATED_PAULO_COELHO.getAlias(), 
			responseAuthorDTO.getAlias());
		assertEquals(
			UPDATED_PAULO_COELHO.getFullName(), 
			responseAuthorDTO.getFullName());
		assertEquals(
			UPDATED_PAULO_COELHO.getBirthday(), 
			responseAuthorDTO.getBirthday());
		assertEquals(
			UPDATED_PAULO_COELHO.getCountry().getId(), 
			responseAuthorDTO.getCountryId());
		verify(authorFinder, times(1))
			.findById(anyLong());
		verify(authorupdateValidator, times(1))
			.validateAlias(
				anyString(),
				anyString());
		verify(authorupdateValidator, times(1))
			.validateFullName(
				anyString(), 
				anyString());
		verify(authorupdateValidator, times(1))
			.validateBirthday(
				any(LocalDate.class), 
				any(LocalDate.class));
		verify(authorupdateValidator, times(1))
			.validateCountry(BRAZIL, null);
		verify(responseAuthorDTOCreator, times(1))
			.toResponseAuthorDTO(any(Author.class));
		verify(saverAndConcurrencyHandle, times(1))
			.saveAndHandleConcurrentyAuthor(any(Author.class));
		verify(responseAuthorDTOCreator, times(1))
			.toResponseAuthorDTO(any(Author.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'AuthorNotFoundException' ao enviar 'id' não existente")
	void updateAuthor_throwsAuthorNotFoundException() {
		when(authorFinder.findById(anyLong()))
			.thenThrow(new AuthorNotFoundException(AUTHOR_NOT_FOUND_MSG));
		assertThrows(
			AuthorNotFoundException.class, 
			() -> authorService.updateAuthor(
				PAULO_COELHO_ID, 
				UPDATE_PAULO_COELHO));
		verify(authorFinder, times(1))
			.findById(anyLong());
		verify(authorupdateValidator, never())
			.validateAlias(
				anyString(), 
				anyString());
		verify(authorupdateValidator, never())
			.validateFullName(
				anyString(), 
				anyString());
		verify(authorupdateValidator, never())
			.validateBirthday(
				any(LocalDate.class), 
				any(LocalDate.class));
		verify(authorupdateValidator, never())
			.validateCountry(
				any(Country.class), 
				anyInt());
		verify(repo, never())
			.saveAndFlush(any(Author.class));
		verify(repo, never())
			.findByAlias(anyString());
		verify(responseAuthorDTOCreator, never())
			.toResponseAuthorDTO(any(Author.class));
	}
	
	@Test
	@DisplayName("Deve retornar 'true' após deletar autor com sucesso")
	void deleteAuthorById_returnsTrue() {
		when(repo.existsById(anyLong()))
			.thenReturn(true);
		assertTrue(authorService.deleteAuthorById(PAULO_COELHO_ID));
		verify(repo, times(1))
			.existsById(anyLong());
	}
	
	@Test
	@DisplayName("Deve lançar 'AuthorNotFoundException' após tentar deletar autor")
	void deleteAuthorById_throwsAuthorNotFoundException() {
		when(repo.existsById(anyLong()))
			.thenReturn(false);
		when(authorExceptionCreator
				.createAuthorNotFoundException(anyLong()))
			.thenThrow(new AuthorNotFoundException(AUTHOR_NOT_FOUND_MSG));
		assertThrows(
			AuthorNotFoundException.class,
			() -> authorService.deleteAuthorById(PAULO_COELHO_ID));
		verify(repo, times(1))
			.existsById(anyLong());
		verify(authorExceptionCreator, times(1))
			.createAuthorNotFoundException(anyLong());
	}
}