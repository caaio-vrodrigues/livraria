package caio.portfolio.livraria.service.author;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
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
import caio.portfolio.livraria.infrastructure.entity.author.dto.CreateAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.ResponseAuthorDTO;
import caio.portfolio.livraria.infrastructure.entity.author.dto.UpdateAuthorDTO;

@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace=Replace.ANY)
class AuthorServiceIntegrationTest {

	@Autowired private AuthorService authorService;
	
	private static final Long PAULO_COELHO_ID = 1L;
	private static final String PAULO_COLEHO_ALIAS = "O Mago";
	private static final String PAULO_COELHO_UPDATED_ALIAS = "PC";
	private static final String PAULO_COELHO_FULL_NAME = "Paulo Coelho";
	private static final String PAULO_COELHO_UPDATED_FULL_NAME = "Coelho, Paulo.";
	private static final Integer BRAZIL_ID = 1;
	private static final LocalDate PAULO_COELHO_BIRTHDAY = LocalDate.of(1947, 8, 24);
	
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
	
	@Test
	@Sql("/sql/country/insert_brazil.sql")
	@DisplayName("Deve receber 'CreateAuthorDTO' e retornar 'ResponseAuthorDTO' após processo de criação e salvamento")
	void createAuthor_returnsResponseAuthorDTO() {
		ResponseAuthorDTO pauloCoelhoResponse = authorService.createAuthor(CREATE_PAULO_COELHO_DTO);
		Assertions.assertNotNull(pauloCoelhoResponse);
		Assertions.assertEquals(PAULO_COLEHO_ALIAS, pauloCoelhoResponse.getAlias());
		Assertions.assertEquals(PAULO_COELHO_FULL_NAME, pauloCoelhoResponse.getFullName());
		Assertions.assertEquals(PAULO_COELHO_ID, pauloCoelhoResponse.getId());
	}
	
	@Test
	@Sql("/sql/country/insert_brazil.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve receber 'CreateAuthorDTO' e retornar 'AuthorAlreadyExistsException' após verificar autor já existente")
	void createAuthor_throwsAuthorAlreadyExistsException() {
		Assertions.assertThrows(
			AuthorAlreadyExistsException.class, 
			() -> authorService.createAuthor(CREATE_PAULO_COELHO_DTO));
	}
	
	@Test
	@Sql("/sql/country/insert_brazil.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve retornar uma lista de 'ResponseAuthorDTO' ao chamar método sem argumentos")
	void getAllAuthors_returnsResponseAuthorDTOList() {
		List<ResponseAuthorDTO> responseAuthorDTOList = authorService.getAllAuthors();
		Assertions.assertNotNull(responseAuthorDTOList);
		Assertions.assertEquals(6, responseAuthorDTOList.size());
		Assertions.assertEquals(PAULO_COLEHO_ALIAS, responseAuthorDTOList.get(0).getAlias());
	}
	
	@Test
	@DisplayName("Deve retornar uma lista vazia ao chamar método sem argumentos")
	void getAllAuthors_returnsEmptyList() {
		List<ResponseAuthorDTO> responseAuthorDTOList = authorService.getAllAuthors();
		Assertions.assertNotNull(responseAuthorDTOList);
		Assertions.assertEquals(0, responseAuthorDTOList.size());
	}
	
	@Test
	@Sql("/sql/country/insert_brazil.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve retornar 'ResponseAuthorDTO' ao buscar por 'id'")
	void getAuthorById_returnsResponseAuthorDTO() {
		ResponseAuthorDTO responseAuthorDTO = authorService.getAuthorById(PAULO_COELHO_ID);
		Assertions.assertNotNull(responseAuthorDTO);
		Assertions.assertEquals(PAULO_COLEHO_ALIAS, responseAuthorDTO.getAlias());
		Assertions.assertEquals(PAULO_COELHO_FULL_NAME, responseAuthorDTO.getFullName());
		Assertions.assertEquals(PAULO_COELHO_BIRTHDAY, responseAuthorDTO.getBirthday());
		Assertions.assertEquals(BRAZIL_ID, responseAuthorDTO.getCountryId());
	}
	
	@Test
	@DisplayName("Deve retornar 'AuthorNotFoundException' ao buscar por 'id'")
	void getAuthorById_throwsAuthorNotFoundException() {
		Assertions.assertThrows(
			AuthorNotFoundException.class, 
			() -> authorService.getAuthorById(PAULO_COELHO_ID));
	}
	
	@Test
	@Sql("/sql/country/insert_brazil.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve retornar 'ResponseAuthorDTO' ao buscar por 'alias'")
	void getAuthorByAlias_returnsResponseAuthorDTO() {
		ResponseAuthorDTO author = authorService.getAuthorByAlias(PAULO_COLEHO_ALIAS);
		Assertions.assertNotNull(author);
		Assertions.assertEquals(PAULO_COLEHO_ALIAS, author.getAlias());
		Assertions.assertEquals(PAULO_COELHO_FULL_NAME, author.getFullName());
		Assertions.assertEquals(PAULO_COELHO_BIRTHDAY, author.getBirthday());
		Assertions.assertEquals(BRAZIL_ID, author.getCountryId());
	}
	
	@Test
	@DisplayName("Deve retornar 'AuthorNotFoundException' ao buscar por 'alias'")
	void getAuthorByAlias_throwsAuthorNotFoundException() {
		Assertions.assertThrows(
			AuthorNotFoundException.class, 
			() -> authorService.getAuthorByAlias(PAULO_COLEHO_ALIAS));
	}
	
	@Test
	@Sql("/sql/country/insert_brazil.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve atualizar autor por 'id' e retornar 'ResponseAuthorDTO'")
	void updateAuthor_returnsResponseAuthorDTO() {
		ResponseAuthorDTO responseAuthorDTO = authorService
			.updateAuthor(PAULO_COELHO_ID, UPDATE_PAULO_COELHO);
		Assertions.assertNotNull(responseAuthorDTO);
		Assertions.assertEquals(PAULO_COELHO_ID, responseAuthorDTO.getId());
		Assertions.assertEquals(PAULO_COELHO_UPDATED_ALIAS, responseAuthorDTO.getAlias());
		Assertions.assertEquals(PAULO_COELHO_UPDATED_FULL_NAME, responseAuthorDTO.getFullName());
		Assertions.assertEquals(PAULO_COELHO_BIRTHDAY, responseAuthorDTO.getBirthday());
		Assertions.assertEquals(BRAZIL_ID, responseAuthorDTO.getCountryId());
	}
	
	@Test
	@DisplayName("Deve lançar 'AuthorNotFoundException' ao enviar 'id' não existente")
	void updateAuthor_throwsAuthorNotFoundException() {
		Assertions.assertThrows(
			AuthorNotFoundException.class , 
			() -> authorService.updateAuthor(PAULO_COELHO_ID, UPDATE_PAULO_COELHO));
	}
}