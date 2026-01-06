package caio.portfolio.livraria.service.author.implementation.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.ANY)
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthorFinderImplIntegrationTest {

	@Autowired private AuthorFinderImpl authorFinderImpl;
	
	private static final Long PAULO_COELHO_ID = 1L;
	private static final String PAULO_COLEHO_ALIAS = "O Mago";

	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve retornar autor ao buscar por 'id'")
	void findById_returnsAuthor() {
		Author pauloCoelho = authorFinderImpl.findById(PAULO_COELHO_ID);
		assertNotNull(pauloCoelho);
		assertEquals(PAULO_COLEHO_ALIAS, pauloCoelho.getAlias());
	}
	
	@Test
	@DisplayName("Deve lançar 'AuthorNotFoundException' ao buscar por 'id'")
	void findById_throwsAuthorNotFoundException() {
		assertThrows(
			AuthorNotFoundException.class,
			() -> authorFinderImpl.findById(PAULO_COELHO_ID));
	}
	
	@Test
	@Sql("/sql/country/insert_country_list.sql")
	@Sql("/sql/author/insert_author_list.sql")
	@DisplayName("Deve retornar autor ao buscar por 'alias'")
	void findByAlias_returnsAuthor() {
		Author pauloCoelho = authorFinderImpl.findByAlias(PAULO_COLEHO_ALIAS);
		assertNotNull(pauloCoelho);
		assertEquals(PAULO_COLEHO_ALIAS, pauloCoelho.getAlias());
	}
	
	@Test
	@DisplayName("Deve lançar 'AuthorNotFoundException' ao buscar por 'alias'")
	void findByAlias_throwsAuthorNotFoundException() {
		assertThrows(
			AuthorNotFoundException.class,
			() -> authorFinderImpl.findByAlias(PAULO_COLEHO_ALIAS));
	}
}