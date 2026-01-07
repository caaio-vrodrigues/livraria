package caio.portfolio.livraria.service.author;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.author.dto.CreateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.UpdateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.country.Country;

@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace=Replace.ANY)
class AuthorServiceIntegrationTest {

	@Autowired private AuthorService authorService;
	
	private static final Long PAULO_COELHO_ID = 1L;
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	private static final String PAULO_COLEHO_ALIAS = "O Mago";
	private static final String PAULO_COELHO_UPDATED_ALIAS = "PC";
	private static final String PAULO_COELHO_FULL_NAME = "Paulo Coelho";
	private static final String PAULO_COELHO_UPDATED_FULL_NAME = "Coelho, Paulo.";
	private static final Integer BRAZIL_ID = 1;
	private static final LocalDate PAULO_COELHO_BIRTHDAY = LocalDate.of(1947, 8, 24);
	
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

	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@DisplayName("Deve receber 'CreateAuthorDTO' e retornar 'ResponseAuthorDTO' após processo de criação e salvamento")
	void createAuthor_returnsResponseAuthorDTO() {
		ResponseAuthorDTO pauloCoelhoResponse = authorService
			.createAuthor(CREATE_PAULO_COELHO_DTO);
		assertNotNull(pauloCoelhoResponse);
		assertEquals(
			PAULO_COLEHO_ALIAS, 
			pauloCoelhoResponse.getAlias());
		assertEquals(
			PAULO_COELHO_FULL_NAME, 
			pauloCoelhoResponse.getFullName());
		assertEquals(
			PAULO_COELHO_ID, 
			pauloCoelhoResponse.getId());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve receber 'CreateAuthorDTO' e retornar 'AuthorAlreadyExistsException' após verificar autor já existente")
	void createAuthor_throwsAuthorAlreadyExistsException() {
		assertThrows(
			AuthorAlreadyExistsException.class, 
			() -> authorService.createAuthor(CREATE_PAULO_COELHO_DTO));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve retornar uma lista de 'ResponseAuthorDTO' ao chamar método sem argumentos")
	void getAllAuthors_returnsResponseAuthorDTOList() {
		List<ResponseAuthorDTO> responseAuthorDTOList = authorService
			.getAllResponseAuthorDTOs();
		assertNotNull(responseAuthorDTOList);
		assertEquals(6, responseAuthorDTOList.size());
		assertEquals(
			PAULO_COLEHO_ALIAS, 
			responseAuthorDTOList.get(0).getAlias());
	}
	
	@Test
	@DisplayName("Deve retornar uma lista vazia ao chamar método sem argumentos")
	void getAllAuthors_returnsEmptyList() {
		List<ResponseAuthorDTO> responseAuthorDTOList = authorService
			.getAllResponseAuthorDTOs();
		assertNotNull(responseAuthorDTOList);
		assertEquals(0, responseAuthorDTOList.size());
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve retornar 'ResponseAuthorDTO' ao buscar por 'id'")
	void getAuthorById_returnsResponseAuthorDTO() {
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
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve retornar autor ao buscar por 'id'")
	void getAuthorById_returnsAuthor() {
		Author pauloCoelho = authorService.getAuthorById(PAULO_COELHO_ID);
		assertNotNull(pauloCoelho);
		assertEquals(
			PAULO_COLEHO_ALIAS,
			pauloCoelho.getAlias());
	}
	
	@Test
	@DisplayName("Deve retornar 'AuthorNotFoundException' ao buscar por 'id'")
	void getAuthorById_throwsAuthorNotFoundException() {
		assertThrows(
			AuthorNotFoundException.class, 
			() -> authorService.getAuthorById(PAULO_COELHO_ID));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve retornar 'ResponseAuthorDTO' ao buscar por 'alias'")
	void getAuthorByAlias_returnsResponseAuthorDTO() {
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
	}
	
	@Test
	@DisplayName("Deve retornar 'AuthorNotFoundException' ao buscar por 'alias'")
	void getAuthorByAlias_throwsAuthorNotFoundException() {
		assertThrows(
			AuthorNotFoundException.class, 
			() -> authorService.getResponseAuthorDTOByAlias(PAULO_COLEHO_ALIAS));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve atualizar autor por 'id' e retornar 'ResponseAuthorDTO'")
	void updateAuthor_returnsResponseAuthorDTO() {
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
	}
	
	@Test
	@DisplayName("Deve lançar 'AuthorNotFoundException' ao enviar 'id' não existente")
	void updateAuthor_throwsAuthorNotFoundException() {
		assertThrows(
			AuthorNotFoundException.class, 
			() -> authorService.updateAuthor(
				PAULO_COELHO_ID, 
				UPDATE_PAULO_COELHO));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve retornar 'true' após deletar autor com sucesso")
	void deleteAuthorById_returnsTrue() {
		assertTrue(authorService.deleteAuthorById(PAULO_COELHO_ID));
	}
	
	@Test
	@DisplayName("Deve lançar 'AuthorNotFoundException' após tentar deletar autor")
	void deleteAuthorById_throwsAuthorNotFoundException() {
		assertThrows(
			AuthorNotFoundException.class,
			() -> authorService.deleteAuthorById(PAULO_COELHO_ID));
	}
}