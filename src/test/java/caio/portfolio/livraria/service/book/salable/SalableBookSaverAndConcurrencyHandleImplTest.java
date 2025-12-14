package caio.portfolio.livraria.service.book.salable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import caio.portfolio.livraria.exception.custom.book.salable.ConcurrentSalableBookException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookAlreadyExistsException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.repository.SalableBookRepository;
import caio.portfolio.livraria.model.enums.Genre;

@ExtendWith(MockitoExtension.class)
class SalableBookSaverAndConcurrencyHandleImplTest {
	
	@InjectMocks private SalableBookSaverAndConcurrencyHandleImpl salableBookSaverAndConcurrencyHandleImpl;
	@Mock private SalableBookRepository repo;
	
	private static final int UNITS = 50;
	private static final Long PAULO_COELHO_ID = 1L;
	private static final Long ROCCO_ID = 1L;
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	private static final String PAULO_COLEHO_ALIAS = "O Mago";
	private static final String PAULO_COELHO_FULL_NAME = "Paulo Coelho";
	private static final String ROCCO_NAME = "Rocco";
	private static final String ROCCO_FULL_ADDRESS = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	private static final String O_ALQUIMISTA_ISBN = "abc123";
	private static final String O_ALQUIMISTA_TITLE = "Current Title";
	private static final Integer BRAZIL_ID = 1;
	private static final LocalDate PAULO_COELHO_BIRTHDAY = LocalDate.of(1947, 8, 24);
	private static final BigDecimal O_ALQUIMISTA_PRICE = BigDecimal.valueOf(39.5);
	
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
	
	private static final Publisher ROCCO_PUBLISHER = Publisher.builder()
		.id(ROCCO_ID)
		.name(ROCCO_NAME)
		.country(BRAZIL)
		.fullAddress(ROCCO_FULL_ADDRESS)
		.build();
	
	private static final SalableBook O_ALQUIMISTA = SalableBook.builder()
		.author(PAULO_COELHO)	
		.title(O_ALQUIMISTA_TITLE)
		.genre(Genre.FANTASY)
		.isbn(O_ALQUIMISTA_ISBN)
		.publisher(ROCCO_PUBLISHER)
		.price(O_ALQUIMISTA_PRICE)
		.units(UNITS)
		.build();
	
	@Test
	@DisplayName("Deve salvar novo 'SalableBook' com sucesso tratando possível concorrência")
	void saveAndHandleConcurrency_returnsSalableBook() {
		when(repo.saveAndFlush(any(SalableBook.class)))
			.thenReturn(O_ALQUIMISTA);
		SalableBook book = salableBookSaverAndConcurrencyHandleImpl
			.saveAndHandleConcurrency(O_ALQUIMISTA);
		assertNotNull(book);
		assertEquals(O_ALQUIMISTA.getId(), book.getId());
		assertEquals(O_ALQUIMISTA.getTitle(), book.getTitle());
		assertEquals(O_ALQUIMISTA.getAuthor(), book.getAuthor());
		verify(repo, times(1))
			.saveAndFlush(any(SalableBook.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'ConcurrentSalableBookException' ao tentar salvar em cenário concorrente")
	void saveAndHandleConcurrency_throwsConcurrentSalableBookException() {
		when(repo.saveAndFlush(any(SalableBook.class)))
			.thenThrow(DataIntegrityViolationException.class);
		when(repo.findByTitleAndAuthor(anyString(), any(Author.class)))
			.thenReturn(Optional.empty());
		assertThrows(
			ConcurrentSalableBookException.class, 
			() -> salableBookSaverAndConcurrencyHandleImpl
				.saveAndHandleConcurrency(O_ALQUIMISTA));
		verify(repo, times(1))
			.saveAndFlush(any(SalableBook.class));
		verify(repo, times(1))
			.findByTitleAndAuthor(anyString(), any(Author.class));
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookAlreadyExistsException' ao tentar salvar em cenário concorrente")
	void saveAndHandleConcurrency_throwsSalableBookAlreadyExistsException() {
		when(repo.saveAndFlush(any(SalableBook.class)))
			.thenThrow(DataIntegrityViolationException.class);
		when(repo.findByTitleAndAuthor(anyString(), any(Author.class)))
			.thenReturn(Optional.of(O_ALQUIMISTA));
		assertThrows(
			SalableBookAlreadyExistsException.class, 
			() -> salableBookSaverAndConcurrencyHandleImpl
				.saveAndHandleConcurrency(O_ALQUIMISTA));
		verify(repo, times(1))
			.saveAndFlush(any(SalableBook.class));
		verify(repo, times(1))
			.findByTitleAndAuthor(anyString(), any(Author.class));
	}
}