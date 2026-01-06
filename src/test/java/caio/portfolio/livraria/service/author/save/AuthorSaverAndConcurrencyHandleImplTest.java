package caio.portfolio.livraria.service.author.save;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
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
import org.springframework.dao.DataIntegrityViolationException;

import caio.portfolio.livraria.exception.custom.author.ConcurrentAuthorException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.repository.AuthorRepository;
import caio.portfolio.livraria.service.author.implementation.save.AuthorSaverAndConcurrencyHandleImpl;
import caio.portfolio.livraria.service.author.model.create.AuthorExceptionCreator;

@ExtendWith(MockitoExtension.class)
class AuthorSaverAndConcurrencyHandleImplTest {

	@InjectMocks private AuthorSaverAndConcurrencyHandleImpl authorSaverAndConcurrencyHandleImpl;
	@Mock private AuthorRepository repo;
	@Mock private AuthorExceptionCreator authorExceptionCreator;
	
	private static final Long PAULO_COELHO_ID = 1L;
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	private static final String PAULO_COLEHO_ALIAS = "O Mago";
	private static final String PAULO_COELHO_FULL_NAME = "Paulo Coelho";
	private static final String CONCURRENT_AUTHOR_MSG = "Falha ao tentar criar autor : `"+PAULO_COELHO_FULL_NAME+"`";
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
	
	@Test
	@DisplayName("Deve salvar e retornar autor com sucesso")
	void saveAndHandleConcurrentyAuthor_returnsAuthor() {
		when(repo.saveAndFlush(any(Author.class)))
			.thenReturn(PAULO_COELHO);
		Author pauloCoelho = authorSaverAndConcurrencyHandleImpl
			.saveAndHandleConcurrentyAuthor(PAULO_COELHO);
		assertNotNull(pauloCoelho);
		assertEquals(
			PAULO_COLEHO_ALIAS, 
			pauloCoelho.getAlias());
		verify(repo, times(1))
			.saveAndFlush(any(Author.class));
	}
	
	@Test
	@DisplayName("Deve buscar e retornar autor já existente após falha de integridade")
	void saveAndHandleConcurrentyAuthor_returnsAuthorAfterFail() {
		when(repo.saveAndFlush(any(Author.class)))
			.thenThrow(new DataIntegrityViolationException(""));
		when(repo.findByAlias(anyString()))
			.thenReturn(Optional.of(PAULO_COELHO));
		Author pauloCoelho = authorSaverAndConcurrencyHandleImpl
			.saveAndHandleConcurrentyAuthor(PAULO_COELHO);
		assertNotNull(pauloCoelho);
		assertEquals(
			PAULO_COLEHO_ALIAS, 
			pauloCoelho.getAlias());
		verify(repo, times(1))
			.saveAndFlush(any(Author.class));
		verify(repo, times(1))
			.findByAlias(anyString());
	}
	
	@Test
	@DisplayName("Deve lançar 'ConcurrentAuthorException' após falha de concorrência")
	void saveAndHandleConcurrentyAuthor_throwsConcurrentAuthorException() {
		when(repo.saveAndFlush(any(Author.class)))
			.thenThrow(new DataIntegrityViolationException(""));
		when(repo.findByAlias(anyString()))
			.thenReturn(Optional.empty());
		when(authorExceptionCreator
				.createConcurrentAuthorException(anyString()))
			.thenReturn(new ConcurrentAuthorException(CONCURRENT_AUTHOR_MSG));
		assertThrows(
			ConcurrentAuthorException.class,
			() -> authorSaverAndConcurrencyHandleImpl
				.saveAndHandleConcurrentyAuthor(PAULO_COELHO));
		verify(repo, times(1))
			.saveAndFlush(any(Author.class));
		verify(repo, times(1))
			.findByAlias(anyString());
		verify(authorExceptionCreator, times(1))
			.createConcurrentAuthorException(anyString());
	}
}