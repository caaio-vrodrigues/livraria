package caio.portfolio.livraria.service.book.salable.implementation.sell;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import caio.portfolio.livraria.exception.custom.book.salable.InsuficientSalableBookUnitsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.BookSellDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.BookSellListDTO;
import caio.portfolio.livraria.infrastructure.entity.country.Country;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.model.enums.Genre;
import caio.portfolio.livraria.service.book.salable.model.create.SalableBookExceptionCreator;
import caio.portfolio.livraria.service.book.salable.model.find.SalableBookFinder;

@ExtendWith(MockitoExtension.class)
class BookSellerImplTest {

	@InjectMocks private BookSellerImpl bookSellerImpl;
	@Mock private SalableBookFinder salableBookFinder;
	@Mock private SalableBookExceptionCreator salableBookExceptionCreator;
	
	private static final int O_ALQUIMISTA_SELL_UNITS = 10;
	private static final int DOM_CASMURRO_SELL_UNITS = 10;
	private static final int ZERO_UNITS = 0;
	private static final int O_ALQUIMISTA_UNITS = 100;
	private static final int DOM_CASMURRO_UNITS = 100;
	private static final Long PAULO_COELHO_ID = 1L;
	private static final Long MACHADO_DE_ASSIS_ID = 2L;
	private static final Long ROCCO_ID = 1L;
	private static final Long O_ALQUIMISTA_ID = 1L;
	private static final Long DOM_CASMURRO_ID = 2L;
	private static final String O_ALQUIMISTA_ISBN = "978-85-325-1010-0";
	private static final String DOM_CASMURRO_ISBN = "978-85-8022-005-0";
	private static final String BRAZIL_NAME = "Brazil";
	private static final String BRAZIL_CODE = "BR";
	private static final String PAULO_COELHO_FULL_NAME = "Paulo Coelho";
	private static final String MACHADO_DE_ASSIS_NAME = "Machado de Assis";
	private static final String O_ALQUIMISTA_TITLE = "O Alquimista";
	private static final String DOM_CASMURRO_TITLE = "Dom Casmurro";
	private static final String PAULO_COLEHO_ALIAS = "O Mago";
	private static final String MACHADO_DE_ASSIS_ALIAS = "machado.ass";
	private static final String ROCCO_NAME = "Rocco";
	private static final String ROCCO_FULL_ADDRESS = "Rua do Passeio, 38, 11º andar, no Passeio Corporate";
	private static final Integer BRAZIL_ID = 1;
	private static final LocalDate PAULO_COELHO_BIRTHDAY = LocalDate.of(1947, 8, 24);
	private static final LocalDate MACHADO_DE_ASSIS_BIRTHDAY = LocalDate.of(1839, 06, 21);
	private static final BigDecimal O_ALQUIMISTA_PRICE = BigDecimal.valueOf(35.50);
	private static final BigDecimal DOM_CASMURRO_PRICE = BigDecimal.valueOf(28.99);
	
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
	
	private static final Author MACHADO_DE_ASSIS = Author.builder()
		.id(MACHADO_DE_ASSIS_ID)
		.alias(MACHADO_DE_ASSIS_ALIAS)
		.fullName(MACHADO_DE_ASSIS_NAME)
		.birthday(MACHADO_DE_ASSIS_BIRTHDAY)
		.country(BRAZIL)
		.build();
	
	private static final Publisher ROCCO_PUBLISHER = Publisher.builder()
		.id(ROCCO_ID)
		.name(ROCCO_NAME)
		.country(BRAZIL)
		.fullAddress(ROCCO_FULL_ADDRESS)
		.build();
	
	private static final BookSellDTO O_ALQUIMISTA_SELL_DTO = BookSellDTO.builder()
		.bookId(O_ALQUIMISTA_ID)	
		.units(O_ALQUIMISTA_SELL_UNITS)
		.build();
	
	private static final BookSellDTO DOM_CASMURRO_SELL_DTO = BookSellDTO.builder()
		.bookId(DOM_CASMURRO_ID)	
		.units(DOM_CASMURRO_SELL_UNITS)
		.build();

	private static final List<BookSellDTO> BOOK_SELL_DTO_LIST_SINGLE_BOOK = List.of(O_ALQUIMISTA_SELL_DTO);
	private static final List<BookSellDTO> BOOK_SELL_DTO_LIST_MANY_BOOKS = List.of(O_ALQUIMISTA_SELL_DTO, DOM_CASMURRO_SELL_DTO);
	private static final String INSUFICIENT_BOOK_UNITS = "Quantidade de livros insuficiente para realizar a venda. Estoque atual: `"+ZERO_UNITS+"`";
	private static final String BOOK_NOT_FOUND_ID_MSG = "Não foi possível encontrar livro com `id`: `"+O_ALQUIMISTA_ID+"`";
	
	private SalableBook oAlquimista;
	private SalableBook domCasmurro;
	private SalableBook oAlquimistaZeroUnits;
	private BookSellListDTO bookSellListDTOSingleBook;
	private BookSellListDTO bookSellListDTOManyBooks;
	
	@BeforeEach
	void setup() {
		oAlquimista = SalableBook.builder()
			.id(O_ALQUIMISTA_ID)
			.author(PAULO_COELHO)	
			.title(O_ALQUIMISTA_TITLE)
			.genre(Genre.FICTION)
			.isbn(O_ALQUIMISTA_ISBN)
			.publisher(ROCCO_PUBLISHER)
			.price(O_ALQUIMISTA_PRICE)
			.units(O_ALQUIMISTA_UNITS)
			.build();
		
		oAlquimistaZeroUnits = SalableBook.builder()
			.id(O_ALQUIMISTA_ID)
			.author(PAULO_COELHO)	
			.title(O_ALQUIMISTA_TITLE)
			.genre(Genre.FICTION)
			.isbn(O_ALQUIMISTA_ISBN)
			.publisher(ROCCO_PUBLISHER)
			.price(O_ALQUIMISTA_PRICE)
			.units(ZERO_UNITS)
			.build();
		
		domCasmurro = SalableBook.builder()
			.id(DOM_CASMURRO_ID)
			.author(MACHADO_DE_ASSIS)
			.title(DOM_CASMURRO_TITLE)
			.genre(Genre.FICTION)
			.isbn(DOM_CASMURRO_ISBN)
			.publisher(ROCCO_PUBLISHER)
			.price(DOM_CASMURRO_PRICE)
			.units(DOM_CASMURRO_UNITS)
			.build();
		
		bookSellListDTOSingleBook = BookSellListDTO
			.builder()
			.sellList(BOOK_SELL_DTO_LIST_SINGLE_BOOK)
			.build();
		
		bookSellListDTOManyBooks = BookSellListDTO
			.builder()
			.sellList(BOOK_SELL_DTO_LIST_MANY_BOOKS)
			.build();
	}
	
	@Test
	@DisplayName("Deve realizar venda de livro com sucesso e retornar total à pagar")
	void sellBooks_returnsBigDecimal() {
		when(salableBookFinder.findById(anyLong()))
			.thenReturn(oAlquimista);
		BigDecimal sell = bookSellerImpl.sellBooks(bookSellListDTOSingleBook);
		assertNotNull(sell);
		assertEquals(
			O_ALQUIMISTA_PRICE.multiply(BigDecimal.valueOf(O_ALQUIMISTA_SELL_UNITS)), 
			sell);
		verify(salableBookFinder, times(1))
			.findById(anyLong());
	}
	
	@Test
	@DisplayName("Deve realizar venda de múltiplos livros com sucesso e retornar total à pagar")
	void sellBooks_manyBooks_returnsBigDecimal() {
		when(salableBookFinder.findById(anyLong()))
			.thenReturn(oAlquimista, domCasmurro);
		BigDecimal sell = bookSellerImpl.sellBooks(bookSellListDTOManyBooks);
		assertNotNull(sell);
		assertEquals(
			O_ALQUIMISTA_PRICE
				.multiply(BigDecimal.valueOf(O_ALQUIMISTA_SELL_UNITS))
				.add(DOM_CASMURRO_PRICE
					.multiply(BigDecimal.valueOf(DOM_CASMURRO_SELL_UNITS))),
			sell);
		verify(salableBookFinder, times(2))
			.findById(anyLong());
	}
	
	@Test
	@DisplayName("Deve lançar 'InsuficientSalableBookUnitsException' ao tentar realizar venda de unidades além do disponível em estoque")
	void sellBooks_throwsInsuficientSalableBookUnitsException() {
		when(salableBookFinder.findById(anyLong()))
			.thenReturn(oAlquimistaZeroUnits);
		when(salableBookExceptionCreator
				.createInsuficientSalableBookUnitsException(anyInt()))
			.thenReturn(new InsuficientSalableBookUnitsException(INSUFICIENT_BOOK_UNITS));
		assertThrows(
			InsuficientSalableBookUnitsException.class,
			() -> bookSellerImpl.sellBooks(bookSellListDTOSingleBook));
		verify(salableBookFinder, times(1))
			.findById(anyLong());
		verify(salableBookFinder, times(1))
			.findById(anyLong());
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao tentar vender livro inexistente")
	void sellBooks_throwsSalableBookNotFoundException() {
		when(salableBookFinder.findById(anyLong()))
			.thenThrow(new SalableBookNotFoundException(BOOK_NOT_FOUND_ID_MSG));
		assertThrows(
			SalableBookNotFoundException.class,
			() -> bookSellerImpl.sellBooks(bookSellListDTOSingleBook));
		verify(salableBookFinder, times(1))
			.findById(anyLong());
	}
}