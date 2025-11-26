package caio.portfolio.livraria.service.author;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.exception.custom.author.ConcurrentAuthorException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.author.dto.CreateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.UpdateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import caio.portfolio.livraria.service.author.model.ResponseAuthorDTOCreator;
import caio.portfolio.livraria.service.country.CountryService;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
	
	@InjectMocks private AuthorService authorService;
	@Mock private AuthorUpdateValidatorImpl authorupdateValidator;
	@Mock private CountryService countryService;
	@Mock private AuthorRepository repo;
	@Mock private ResponseAuthorDTOCreator responseAuthorDTOCreator;
	
	
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
	
	private static final CreateAuthorDTO CREATE_PAULO_COELHO_DTO = CreateAuthorDTO.builder()
		.alias(PAULO_COLEHO_ALIAS)
		.fullName(PAULO_COELHO_FULL_NAME)
		.birthday(PAULO_COELHO_BIRTHDAY)
		.countryId(BRAZIL_ID)
		.build();
	
	private static final UpdateAuthorDTO UPDATE_PAULO_COELHO = UpdateAuthorDTO.builder()
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
	
	private static final ResponseAuthorDTO UPDATE_PAULO_COELHO_DTO = ResponseAuthorDTO.builder()
		.id(UPDATED_PAULO_COELHO.getId())
		.alias(UPDATED_PAULO_COELHO.getAlias())
		.fullName(UPDATED_PAULO_COELHO.getFullName())
		.birthday(UPDATED_PAULO_COELHO.getBirthday())
		.countryId(UPDATED_PAULO_COELHO.getCountry().getId())
		.build();
	
	private static final List<Author> AUTHOR_LIST = List.of(PAULO_COELHO, CAIO_VINICIUS_RODRIGUES);

	@Test
	@DisplayName("Deve receber 'CreateAuthorDTO' e retornar 'ResponseAuthorDTO' após processo de criação e salvamento")
	void createAuthor_returnsResponseAuthorDTO() {
		Mockito.when(repo.findByAlias(PAULO_COLEHO_ALIAS)).thenReturn(Optional.empty());
		Mockito.when(countryService.getCountryById(BRAZIL_ID)).thenReturn(BRAZIL);
		Mockito.when(repo.saveAndFlush(Mockito.any(Author.class))).thenReturn(PAULO_COELHO);
		Mockito.when(responseAuthorDTOCreator.toResponseAuthorDTO(PAULO_COELHO))
			.thenReturn(RESPONSE_PAULO_COELHO_DTO);
		ResponseAuthorDTO pauloCoelhoResponse = authorService.createAuthor(CREATE_PAULO_COELHO_DTO);
		Assertions.assertNotNull(pauloCoelhoResponse);
		Assertions.assertEquals(RESPONSE_PAULO_COELHO_DTO, pauloCoelhoResponse);
		Assertions.assertEquals(PAULO_COLEHO_ALIAS, pauloCoelhoResponse.getAlias());
		Assertions.assertEquals(PAULO_COELHO_FULL_NAME, pauloCoelhoResponse.getFullName());
		Assertions.assertEquals(PAULO_COELHO_ID, pauloCoelhoResponse.getId());
		Mockito.verify(repo).findByAlias(PAULO_COLEHO_ALIAS);
		Mockito.verify(countryService).getCountryById(BRAZIL_ID);
		Mockito.verify(repo).saveAndFlush(Mockito.any(Author.class));
		Mockito.verify(responseAuthorDTOCreator).toResponseAuthorDTO(PAULO_COELHO);
	}
	
	@Test
	@DisplayName("Deve receber 'CreateAuthorDTO' e retornar 'AuthorAlreadyExistsException' após verificar autor já existente")
	void createAuthor_throwsAuthorAlreadyExistsException() {
		Mockito.when(repo.findByAlias(PAULO_COLEHO_ALIAS)).thenReturn(Optional.of(PAULO_COELHO));
		Assertions.assertThrows(
			AuthorAlreadyExistsException.class, 
			() -> authorService.createAuthor(CREATE_PAULO_COELHO_DTO));
	}
	
	@Test
    @DisplayName("Deve lançar 'ConcurrentAuthorException' ao tentar salvar autor")
    void createAuthor_throwsConcurrentAuthorException() {
		Mockito.when(repo.findByAlias(PAULO_COLEHO_ALIAS)).thenReturn(Optional.empty());
		Mockito.when(countryService.getCountryById(BRAZIL_ID)).thenReturn(BRAZIL);
		Mockito.when(repo.saveAndFlush(Mockito.any(Author.class)))
        	.thenThrow(new DataIntegrityViolationException("Erro de persistência"));
		Assertions.assertThrows(
			ConcurrentAuthorException.class,
            () -> authorService.createAuthor(CREATE_PAULO_COELHO_DTO)
        );
		Mockito.verify(repo, Mockito.times(2)).findByAlias(PAULO_COLEHO_ALIAS);
	    Mockito.verify(countryService).getCountryById(BRAZIL_ID);
	    Mockito.verify(repo).saveAndFlush(Mockito.any(Author.class));
	    Mockito.verify(responseAuthorDTOCreator, Mockito.never())
	    	.toResponseAuthorDTO(Mockito.any(Author.class));
	}
	
	@Test
	@DisplayName("Deve retornar uma lista de 'ResponseAuthorDTO' ao chamar método sem argumentos")
	void getAllAuthors_returnsResponseAuthorDTOList() {
		Mockito.when(repo.findAll()).thenReturn(AUTHOR_LIST);
		Mockito.when(responseAuthorDTOCreator.toResponseAuthorDTO(Mockito.any(Author.class)))
			.thenReturn(RESPONSE_PAULO_COELHO_DTO, RESPONSE_CAIO_VINICIUS_RODRIGUES_DTO);
		List<ResponseAuthorDTO> responseAuthorDTOList = authorService.getAllAuthors();
		Assertions.assertNotNull(responseAuthorDTOList);
		Assertions.assertEquals(2, responseAuthorDTOList.size());
		Assertions.assertEquals(RESPONSE_PAULO_COELHO_DTO, responseAuthorDTOList.get(0));
		Assertions.assertEquals(RESPONSE_CAIO_VINICIUS_RODRIGUES_DTO, responseAuthorDTOList.get(1));
		Mockito.verify(repo).findAll();
		Mockito.verify(responseAuthorDTOCreator, Mockito.times(2))
			.toResponseAuthorDTO(Mockito.any(Author.class));
	}
	
	@Test
	@DisplayName("Deve retornar uma lista vazia ao chamar método sem argumentos")
	void getAllAuthors_returnsEmptyList() {
		Mockito.when(repo.findAll()).thenReturn(List.of());
		List<ResponseAuthorDTO> responseAuthorDTOList = authorService.getAllAuthors();
		Assertions.assertNotNull(responseAuthorDTOList);
		Assertions.assertEquals(0, responseAuthorDTOList.size());
		Mockito.verify(repo).findAll();
		Mockito.verify(responseAuthorDTOCreator, Mockito.never())
			.toResponseAuthorDTO(Mockito.any(Author.class));
	}
	
	@Test
	@DisplayName("Deve retornar 'ResponseAuthorDTO' ao buscar por 'id'")
	void getAuthorById_returnsResponseAuthorDTO() {
		Mockito.when(repo.findById(PAULO_COELHO_ID)).thenReturn(Optional.of(PAULO_COELHO));
		Mockito.when(responseAuthorDTOCreator.toResponseAuthorDTO(PAULO_COELHO))
			.thenReturn(RESPONSE_PAULO_COELHO_DTO);
		ResponseAuthorDTO responseAuthorDTO = authorService.getAuthorById(PAULO_COELHO_ID);
		Assertions.assertNotNull(responseAuthorDTO);
		Assertions.assertEquals(PAULO_COLEHO_ALIAS, responseAuthorDTO.getAlias());
		Assertions.assertEquals(PAULO_COELHO_FULL_NAME, responseAuthorDTO.getFullName());
		Assertions.assertEquals(PAULO_COELHO_BIRTHDAY, responseAuthorDTO.getBirthday());
		Assertions.assertEquals(BRAZIL_ID, responseAuthorDTO.getCountryId());
		Mockito.verify(repo).findById(PAULO_COELHO_ID);
		Mockito.verify(responseAuthorDTOCreator).toResponseAuthorDTO(PAULO_COELHO);
	}
	
	@Test
	@DisplayName("Deve retornar 'AuthorNotFoundException' ao buscar por 'id'")
	void getAuthorById_throwsAuthorNotFoundException() {
		Mockito.when(repo.findById(PAULO_COELHO_ID)).thenReturn(Optional.empty());
		Assertions.assertThrows(
			AuthorNotFoundException.class, 
			() -> authorService.getAuthorById(PAULO_COELHO_ID));
		Mockito.verify(repo).findById(PAULO_COELHO_ID);
		Mockito.verify(responseAuthorDTOCreator, Mockito.never())
			.toResponseAuthorDTO(Mockito.any(Author.class));
	}
	
	@Test
	@DisplayName("Deve retornar 'ResponseAuthorDTO' ao buscar por 'alias'")
	void getAuthorByAlias_returnsResponseAuthorDTO() {
		Mockito.when(repo.findByAlias(PAULO_COLEHO_ALIAS)).thenReturn(Optional.of(PAULO_COELHO));
		Mockito.when(responseAuthorDTOCreator.toResponseAuthorDTO(PAULO_COELHO))
			.thenReturn(RESPONSE_PAULO_COELHO_DTO);
		ResponseAuthorDTO author = authorService.getAuthorByAlias(PAULO_COLEHO_ALIAS);
		Assertions.assertNotNull(author);
		Assertions.assertEquals(PAULO_COLEHO_ALIAS, author.getAlias());
		Assertions.assertEquals(PAULO_COELHO_FULL_NAME, author.getFullName());
		Assertions.assertEquals(PAULO_COELHO_BIRTHDAY, author.getBirthday());
		Assertions.assertEquals(BRAZIL_ID, author.getCountryId());
		Mockito.verify(repo).findByAlias(PAULO_COLEHO_ALIAS);
		Mockito.verify(responseAuthorDTOCreator).toResponseAuthorDTO(PAULO_COELHO);
	}
	
	@Test
	@DisplayName("Deve retornar 'AuthorNotFoundException' ao buscar por 'alias'")
	void getAuthorByAlias_throwsAuthorNotFoundException() {
		Mockito.when(repo.findByAlias(PAULO_COLEHO_ALIAS)).thenReturn(Optional.empty());
		Assertions.assertThrows(
			AuthorNotFoundException.class, 
			() -> authorService.getAuthorByAlias(PAULO_COLEHO_ALIAS));
		Mockito.verify(repo).findByAlias(Mockito.anyString());
		Mockito.verify(responseAuthorDTOCreator, Mockito.never())
			.toResponseAuthorDTO(Mockito.any(Author.class));
	}
	
	@Test
	@DisplayName("Deve atualizar autor por 'id' e retornar 'ResponseAuthorDTO'")
	void updateAuthor_returnsResponseAuthorDTO() {
		Mockito.when(repo.findById(PAULO_COELHO_ID))
			.thenReturn(Optional.of(PAULO_COELHO));
		Mockito.when(authorupdateValidator
			.validateAlias(PAULO_COLEHO_ALIAS, PAULO_COELHO_UPDATED_ALIAS))
				.thenReturn(PAULO_COELHO_UPDATED_ALIAS);
		Mockito.when(authorupdateValidator
			.validateFullName(PAULO_COELHO_FULL_NAME, PAULO_COELHO_UPDATED_FULL_NAME))
				.thenReturn(PAULO_COELHO_UPDATED_FULL_NAME);
		Mockito.when(authorupdateValidator
			.validateBirthday(PAULO_COELHO_BIRTHDAY, PAULO_COELHO_BIRTHDAY))
				.thenReturn(PAULO_COELHO_BIRTHDAY);
		Mockito.when(authorupdateValidator
			.validateCountry(BRAZIL, null))
				.thenReturn(BRAZIL);
		Mockito.when(repo.saveAndFlush(UPDATED_PAULO_COELHO))
			.thenReturn(UPDATED_PAULO_COELHO);
		Mockito.when(responseAuthorDTOCreator.toResponseAuthorDTO(UPDATED_PAULO_COELHO))
			.thenReturn(UPDATE_PAULO_COELHO_DTO);
		ResponseAuthorDTO responseAuthorDTO = authorService.updateAuthor(PAULO_COELHO_ID, UPDATE_PAULO_COELHO);
		Assertions.assertNotNull(responseAuthorDTO);
		Assertions.assertEquals(UPDATED_PAULO_COELHO.getId(), responseAuthorDTO.getId());
		Assertions.assertEquals(UPDATED_PAULO_COELHO.getAlias(), responseAuthorDTO.getAlias());
		Assertions.assertEquals(UPDATED_PAULO_COELHO.getFullName(), responseAuthorDTO.getFullName());
		Assertions.assertEquals(UPDATED_PAULO_COELHO.getBirthday(), responseAuthorDTO.getBirthday());
		Assertions.assertEquals(UPDATED_PAULO_COELHO.getCountry().getId(), responseAuthorDTO.getCountryId());
		Mockito.verify(repo).findById(PAULO_COELHO_ID);
		Mockito.verify(authorupdateValidator).validateAlias(Mockito.anyString(), Mockito.anyString());
		Mockito.verify(authorupdateValidator).validateFullName(Mockito.anyString(), Mockito.anyString());
		Mockito.verify(authorupdateValidator).validateBirthday(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class));
		Mockito.verify(repo).saveAndFlush(Mockito.any(Author.class));
		Mockito.verify(responseAuthorDTOCreator).toResponseAuthorDTO(Mockito.any(Author.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'AuthorNotFoundException' ao enviar 'id' não existente")
	void updateAuthor_throwsAuthorNotFoundException() {
		Mockito.when(repo.findById(PAULO_COELHO_ID)).thenReturn(Optional.empty());
		Assertions.assertThrows(
			AuthorNotFoundException.class , 
			() -> authorService.updateAuthor(PAULO_COELHO_ID, UPDATE_PAULO_COELHO));
		Mockito.verify(repo).findById(Mockito.anyLong());
		Mockito.verify(authorupdateValidator, Mockito.never())
			.validateAlias(Mockito.anyString(), Mockito.anyString());
		Mockito.verify(authorupdateValidator, Mockito.never())
			.validateFullName(Mockito.anyString(), Mockito.anyString());
		Mockito.verify(authorupdateValidator, Mockito.never())
			.validateBirthday(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class));
		Mockito.verify(authorupdateValidator, Mockito.never())
			.validateCountry(Mockito.any(Country.class), Mockito.anyInt());
		Mockito.verify(repo, Mockito.never()).saveAndFlush(Mockito.any(Author.class));
		Mockito.verify(repo, Mockito.never()).findByAlias(Mockito.anyString());
		Mockito.verify(responseAuthorDTOCreator, Mockito.never())
			.toResponseAuthorDTO(Mockito.any(Author.class));
	}
}
