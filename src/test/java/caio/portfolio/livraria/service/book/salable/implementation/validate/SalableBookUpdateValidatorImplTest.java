package caio.portfolio.livraria.service.book.salable.implementation.validate;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.model.enums.Genre;
import caio.portfolio.livraria.service.author.AuthorService;
import caio.portfolio.livraria.service.book.salable.dto.TitleAndAuthorUpdateDTO;
import caio.portfolio.livraria.service.publisher.PublisherService;

@ExtendWith(MockitoExtension.class)
class SalableBookUpdateValidatorImplTest {

	@InjectMocks SalableBookUpdateValidatorImpl salableBookUpdateValidatorImpl;
	@Mock AuthorService authorService;
	@Mock PublisherService publisherService;
	
	private static final int CURRENT_UNITS = 100;
	private static final int NEW_UNITS = 80;
	private static final Long PAULO_COELHO_ID = 1L;
	private static final Long CAIO_ID = 2L;
	private static final Long NON_EXISTENT_ID = 100L;
	private static final Long ROCCO_ID = 1L;
	private static final Long GLOBAL_BOOKS_ID = 2L;
	private static final Genre ROMANCE = Genre.ROMANCE;
	private static final Genre FICTION = Genre.FICTION;
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	private static final String PAULO_COLEHO_ALIAS = "O Mago";
	private static final String CAIO_ALIAS = "CVR";
	private static final String PAULO_COELHO_FULL_NAME = "Paulo Coelho";
	private static final String CAIO_FULL_NAME = "Caio Vinicius Rodrigues";
	private static final String CURRENT_TITLE = "O Alquimista";
	private static final String NEW_TITLE = "Livro de Caio";
	private static final String ROCCO_NAME = "Rocco";
	private static final String GLOBAL_BOOKS_NAME = "Global Books";
	private static final String USA_NAME = "Estados Unidos";
	private static final String USA_ISO_CODE = "US";
	private static final String ROCCO_FULL_ADDRESS = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	private static final String GLOBAL_BOOKS_FULL_ADDRESS = "123 Main Street, New York, USA";
	private static final String CURRENT_ISBN = "isbn-test1";
	private static final String NEW_ISBN = "isbn-test2";
	private static final Integer BRAZIL_ID = 1;
	private static final Integer USA_ID = 2;
	private static final LocalDate PAULO_COELHO_BIRTHDAY = LocalDate.of(1947, 8, 24);
	private static final LocalDate CAIO_BIRTHDAY = LocalDate.of(1992, 03, 20);
	private static final BigDecimal CURRENT_PRICE = BigDecimal.valueOf(100);
	private static final BigDecimal NEW_PRICE = BigDecimal.valueOf(50);
	
	private static final Country BRAZIL = Country.builder()
		.id(BRAZIL_ID)
		.isoAlpha2Code(BRAZIL_CODE)
		.name(BRAZIL_NAME)
		.build(); 
	
	private static final Country USA = Country.builder()
		.id(USA_ID)
		.name(USA_NAME)
		.isoAlpha2Code(USA_ISO_CODE)
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
	
	@Test
	@DisplayName("Deve validar novos título e autor para retorno de 'TitleAndAuthorUpdateDTO' contendo novos valores")
	void validateTitleAndAuthor_returnsUpdatedTitleAndAuthorUpdateDTO() {
		when(authorService.getAuthorById(CAIO_ID))
			.thenReturn(CAIO_VINICIUS_RODRIGUES);
		TitleAndAuthorUpdateDTO titleAndAuthorUpdateDTO = salableBookUpdateValidatorImpl
			.validateTitleAndAuthor(
				CURRENT_TITLE, 
				NEW_TITLE, 
				PAULO_COELHO, 
				CAIO_ID);
		assertNotNull(titleAndAuthorUpdateDTO);
		assertEquals(
			NEW_TITLE, 
			titleAndAuthorUpdateDTO.getTitle());
		assertEquals(
			CAIO_ID, 
			titleAndAuthorUpdateDTO.getAuthor().getId());
	}
	
	@Test
	@DisplayName("Deve validar título e autor iguais aos já existentes para retorno de 'TitleAndAuthorUpdateDTO' contendo valores sem alteração")
	void validateTitleAndAuthor_returnsCurrentTitleAndAuthorUpdateDTO() {
		TitleAndAuthorUpdateDTO titleAndAuthorUpdateDTO = salableBookUpdateValidatorImpl
			.validateTitleAndAuthor(
				CURRENT_TITLE, 
				CURRENT_TITLE, 
				PAULO_COELHO, 
				PAULO_COELHO_ID);
		assertNotNull(titleAndAuthorUpdateDTO);
		assertEquals(
			CURRENT_TITLE, 
			titleAndAuthorUpdateDTO.getTitle());
		assertEquals(
			PAULO_COELHO.getId(), 
			titleAndAuthorUpdateDTO.getAuthor().getId());
		verify(authorService, never())
			.getAuthorById(anyLong());
	}
	
	@Test
	@DisplayName("Deve retornar 'TitleAndAuthorUpdateDTO' contendo valores sem alteração ao receber argumentos nulos")
	void validateTitleAndAuthor_nullArgument_returnsCurrentTitleAndAuthorUpdateDTO() {
		TitleAndAuthorUpdateDTO titleAndAuthorUpdateDTO = salableBookUpdateValidatorImpl
			.validateTitleAndAuthor(
				CURRENT_TITLE, 
				null, 
				PAULO_COELHO, 
				null);
		assertNotNull(titleAndAuthorUpdateDTO);
		assertEquals(
			CURRENT_TITLE, 
			titleAndAuthorUpdateDTO.getTitle());
		assertEquals(
			PAULO_COELHO.getId(), 
			titleAndAuthorUpdateDTO.getAuthor().getId());
		verify(authorService, never())
			.getAuthorById(anyLong());
	}
	
	@Test
	@DisplayName("Deve tentar validar título e autor para lançar 'AuthorNotFoundException'")
	void validateTitleAndAuthor_throwsAuthorNotFoundException() {
		when(authorService.getAuthorById(NON_EXISTENT_ID))
			.thenThrow(AuthorNotFoundException.class);
		assertThrows(
			AuthorNotFoundException.class, 
			() -> salableBookUpdateValidatorImpl
					.validateTitleAndAuthor(
						CURRENT_TITLE, 
						NEW_TITLE, 
						PAULO_COELHO, 
						NON_EXISTENT_ID));
	}
	
	@Test
	@DisplayName("Deve validar uma editora diferente e retorna-la")
	void validatePublisher_returnsNewPublisher() {
		when(publisherService.getPublisherById(GLOBAL_BOOKS_ID))
			.thenReturn(GLOBAL_BOOKS_PUBLISHER);
		Publisher publisher = salableBookUpdateValidatorImpl
			.validatePublisher(
				ROCCO_PUBLISHER, 
				GLOBAL_BOOKS_ID);
		assertNotNull(publisher);
		assertEquals(
			GLOBAL_BOOKS_ID, 
			publisher.getId());
		assertEquals(
			GLOBAL_BOOKS_NAME, 
			publisher.getName());
	}
	
	@Test
	@DisplayName("Deve validar a mesma editora e retorna-la")
	void validatePublisher_returnsCurrentPublisher() {
		Publisher publisher = salableBookUpdateValidatorImpl
			.validatePublisher(
				ROCCO_PUBLISHER, 
				ROCCO_ID);
		assertNotNull(publisher);
		assertEquals(
			ROCCO_ID, 
			publisher.getId());
		assertEquals(
			ROCCO_NAME, 
			publisher.getName());
		verify(publisherService, never())
			.getPublisherById(anyLong());
	}
	
	@Test
	@DisplayName("Deve retornar editora já existente ao receber argumento nulo")
	void validatePublisher_nullArgument_returnsCurrentPublisher() {
		Publisher publisher = salableBookUpdateValidatorImpl
			.validatePublisher(
				ROCCO_PUBLISHER, 
				null);
		assertNotNull(publisher);
		assertEquals(
			ROCCO_ID, 
			publisher.getId());
		assertEquals(
			ROCCO_NAME, 
			publisher.getName());
		verify(publisherService, never())
			.getPublisherById(anyLong());
	}
	
	@Test
	@DisplayName("Deve tentar validar uma editora e lançar 'PublisherNotFoundException'")
	void validatePublisher_throwsPublisherNotFoundException() {
		when(publisherService.getPublisherById(NON_EXISTENT_ID))
			.thenThrow(PublisherNotFoundException.class);
		assertThrows(
			PublisherNotFoundException.class, 
			() -> salableBookUpdateValidatorImpl
					.validatePublisher(
						ROCCO_PUBLISHER, 
						NON_EXISTENT_ID));
	}
	
	@Test
	@DisplayName("Deve validar novo gênero e retorna-lo")
	void validateGenre_returnsNewGenre() {
		Genre fiction = salableBookUpdateValidatorImpl
			.validateGenre(
				FICTION, 
				ROMANCE);
		assertNotNull(fiction);
		assertEquals(
			ROMANCE.getGenreType(), 
			fiction.getGenreType());
	}
	
	@Test
	@DisplayName("Deve validar o mesmo gênero e retorna-lo")
	void validateGenre_returnsCurrentGenre() {
		Genre fiction = salableBookUpdateValidatorImpl
			.validateGenre(
				FICTION, 
				FICTION);
		assertNotNull(fiction);
		assertEquals(
			FICTION.getGenreType(), 
			fiction.getGenreType());
	}
	
	@Test
	@DisplayName("Deve retornar mesmo gênero ao receber argumento nulo")
	void validateGenre_nullArgument_returnsCurrentGenre() {
		Genre fiction = salableBookUpdateValidatorImpl
			.validateGenre(
				FICTION, 
				null);
		assertNotNull(fiction);
		assertEquals(
			FICTION.getGenreType(), 
			fiction.getGenreType());
	}
	
	@Test
	@DisplayName("Deve validar novo isbn e retorna-lo")
	void validateIsbn_returnsNewIsbn() {
		String isbn = salableBookUpdateValidatorImpl
			.validateIsbn(
				CURRENT_ISBN, 
				NEW_ISBN);
		assertNotNull(isbn);
		assertEquals(
			NEW_ISBN, 
			isbn);
	}
	
	@Test
	@DisplayName("Deve validar mesmo isbn e retorna-lo")
	void validateIsbn_returnsCurrentIsbn() {
		String isbn = salableBookUpdateValidatorImpl
			.validateIsbn(
				CURRENT_ISBN, 
				CURRENT_ISBN);
		assertNotNull(isbn);
		assertEquals(
			CURRENT_ISBN, 
			isbn);
	}
	
	@Test
	@DisplayName("Deve retornar mesmo isbn ao receber argumento nulo")
	void validateIsbn_nullArgument_returnsCurrentIsbn() {
		String isbn = salableBookUpdateValidatorImpl
			.validateIsbn(
				CURRENT_ISBN, 
				null);
		assertNotNull(isbn);
		assertEquals(
			CURRENT_ISBN, 
			isbn);
	}
	
	@Test
	@DisplayName("Deve validar novo preço e retorna-lo")
	void validatePrice_returnsNewPrice() {
		BigDecimal price = salableBookUpdateValidatorImpl
			.validatePrice(
				CURRENT_PRICE, 
				NEW_PRICE);
		assertNotNull(price);
		assertEquals(
			NEW_PRICE, 
			price);
	}
	
	@Test
	@DisplayName("Deve validar mesmo preço e retorna-lo")
	void validatePrice_returnsCurrentPrice() {
		BigDecimal price = salableBookUpdateValidatorImpl
			.validatePrice(
				CURRENT_PRICE, 
				CURRENT_PRICE);
		assertNotNull(price);
		assertEquals(
			CURRENT_PRICE, 
			price);
	}
	
	@Test
	@DisplayName("Deve retornar mesmo preço ao receber argumento nulo")
	void validatePrice_nullArgument_returnsCurrentPrice() {
		BigDecimal price = salableBookUpdateValidatorImpl
			.validatePrice(
				CURRENT_PRICE, 
				null);
		assertNotNull(price);
		assertEquals(
			CURRENT_PRICE, 
			price);
	}
	
	@Test
	@DisplayName("Deve validar novas unidades e retorna-las")
	void validateUnits_returnsNewUnits() {
		int units = salableBookUpdateValidatorImpl
			.validateUnits(
				CURRENT_UNITS, 
				NEW_UNITS);
		assertEquals(
			NEW_UNITS, 
			units);
	}
	
	@Test
	@DisplayName("Deve validar mesmas unidades e retorna-las")
	void validateUnits_returnsCurrentUnits() {
		int units = salableBookUpdateValidatorImpl
			.validateUnits(
				CURRENT_UNITS, 
				CURRENT_UNITS);
		assertEquals(
			CURRENT_UNITS, 
			units);
	}
}
