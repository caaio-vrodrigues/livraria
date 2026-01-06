package caio.portfolio.livraria.service.author.find;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import caio.portfolio.livraria.service.author.implementation.find.AuthorFinderImpl;

@ExtendWith(MockitoExtension.class)
class AuthorFinderImplTest {

	@InjectMocks private AuthorFinderImpl authorFinderImpl;
	@Mock private AuthorRepository repo;
	@Mock private MessageSource authorMessageSource;
	
	private static final Long PAULO_COELHO_ID = 1L;
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	private static final String PAULO_COLEHO_ALIAS = "O Mago";
	private static final String PAULO_COELHO_FULL_NAME = "Paulo Coelho";
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
	
	private static final String AUTHOR_NOT_FOUND_ID_MSG = "Não foi possível encontrar um autor com `id`: `"+PAULO_COELHO_ID+"`";
	private static final String AUTHOR_NOT_FOUND_ALIAS_MSG = "Não foi possível encontrar um autor com `alias`: `"+PAULO_COLEHO_ALIAS+"`";
	
	@Test
	@DisplayName("Deve retornar autor ao buscar por 'id'")
	void findById_returnsAuthor() {
		when(authorMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(AUTHOR_NOT_FOUND_ID_MSG);
		when(repo.findById(anyLong()))
			.thenReturn(Optional.of(PAULO_COELHO));
		Author pauloCoelho = authorFinderImpl.findById(PAULO_COELHO_ID);
		assertNotNull(pauloCoelho);
		assertEquals(PAULO_COLEHO_ALIAS, pauloCoelho.getAlias());
		verify(authorMessageSource, times(1))
			.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class));
		verify(repo, times(1))
			.findById(anyLong());
	}
	
	@Test
	@DisplayName("Deve lançar 'AuthorNotFoundException' ao buscar por 'id'")
	void findById_throwsAuthorNotFoundException() {
		when(authorMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(AUTHOR_NOT_FOUND_ID_MSG);
		when(repo.findById(anyLong()))
			.thenReturn(Optional.empty());
		assertThrows(
			AuthorNotFoundException.class,
			() -> authorFinderImpl.findById(PAULO_COELHO_ID));
		verify(authorMessageSource, times(1))
			.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class));
		verify(repo, times(1))
			.findById(anyLong());
	}
	
	@Test
	@DisplayName("Deve retornar autor ao buscar por 'alias'")
	void findByAlias_returnsAuthor() {
		when(authorMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(AUTHOR_NOT_FOUND_ALIAS_MSG);
		when(repo.findByAlias(anyString()))
			.thenReturn(Optional.of(PAULO_COELHO));
		Author pauloCoelho = authorFinderImpl.findByAlias(PAULO_COLEHO_ALIAS);
		assertNotNull(pauloCoelho);
		assertEquals(PAULO_COLEHO_ALIAS, pauloCoelho.getAlias());
		verify(authorMessageSource, times(1))
			.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class));
		verify(repo, times(1))
			.findByAlias(anyString());
	}
	
	@Test
	@DisplayName("Deve lançar 'AuthorNotFoundException' ao buscar por 'alias'")
	void findByAlias_throwsAuthorNotFoundException() {
		when(authorMessageSource.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class)))
			.thenReturn(AUTHOR_NOT_FOUND_ALIAS_MSG);
		when(repo.findByAlias(anyString()))
			.thenReturn(Optional.empty());
		assertThrows(
			AuthorNotFoundException.class,
			() -> authorFinderImpl.findByAlias(PAULO_COLEHO_ALIAS));
		verify(authorMessageSource, times(1))
			.getMessage(
				anyString(), 
				any(Object[].class),
				any(Locale.class));
		verify(repo, times(1))
			.findByAlias(anyString());
	}
}