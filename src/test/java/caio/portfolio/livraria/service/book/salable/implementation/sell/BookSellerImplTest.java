package caio.portfolio.livraria.service.book.salable.implementation.sell;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import caio.portfolio.livraria.infrastructure.repository.SalableBookRepository;
import caio.portfolio.livraria.model.enums.Genre;

@ExtendWith(MockitoExtension.class)
class BookSellerImplTest {

	@InjectMocks private BookSellerImpl bookSellerImpl;
	@Mock private SalableBookRepository repo;
	
	private static final int UNITS = 50;
	private static final int SELL_UNITS = 10;
	private static final int ZERO_UNITS = 0;
	private static final Long O_ALQUIMISTA_ID = 1L;
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
		.id(O_ALQUIMISTA_ID)
		.author(PAULO_COELHO)	
		.title(O_ALQUIMISTA_TITLE)
		.genre(Genre.FANTASY)
		.isbn(O_ALQUIMISTA_ISBN)
		.publisher(ROCCO_PUBLISHER)
		.price(O_ALQUIMISTA_PRICE)
		.units(UNITS)
		.build();
	
	private static final SalableBook O_ALQUIMISTA_0_UNITS = SalableBook.builder()
		.id(O_ALQUIMISTA_ID)
		.author(PAULO_COELHO)	
		.title(O_ALQUIMISTA_TITLE)
		.genre(Genre.FANTASY)
		.isbn(O_ALQUIMISTA_ISBN)
		.publisher(ROCCO_PUBLISHER)
		.price(O_ALQUIMISTA_PRICE)
		.units(ZERO_UNITS)
		.build();
	
	private static final BookSellDTO BOOK_SELL_DTO = BookSellDTO.builder()
		.bookId(O_ALQUIMISTA_ID)	
		.units(SELL_UNITS)
		.build();
	
	private static final List<BookSellDTO> BOOK_SELL_DTO_LIST = List.of(BOOK_SELL_DTO);
	
	private static final BookSellListDTO BOOK_SELL_LIST_DTO = BookSellListDTO
		.builder()
		.sellList(BOOK_SELL_DTO_LIST)
		.build();
	
	@Test
	@DisplayName("Deve realizar venda de livro com sucesso e retornar total à pagar")
	void sellBook_returnsBigDecimal() {
		when(repo.findById(anyLong()))
			.thenReturn(Optional.of(O_ALQUIMISTA));
		BigDecimal totalToPay = bookSellerImpl.sellBook(O_ALQUIMISTA.getId(), 2);
		assertNotNull(totalToPay);
		assertEquals(
			O_ALQUIMISTA.getPrice().multiply(BigDecimal.valueOf(2)), 
			totalToPay);
		verify(repo, times(1)).findById(anyLong());
	}
	
	@Test
	@DisplayName("Deve lançar 'SalableBookNotFoundException' ao tenatr realizar venda de livro não existente")
	void sellBook_throwsSalableBookNotFoundException() {
		when(repo.findById(anyLong()))
			.thenReturn(Optional.empty());
		assertThrows(
			SalableBookNotFoundException.class,
			() -> bookSellerImpl.sellBook(O_ALQUIMISTA_ID, UNITS));
		verify(repo, times(1)).findById(anyLong());
	}
	
	@Test
	@DisplayName("Deve realizar venda de múltiplos livros com sucesso e retornar total à pagar")
	void sellBooks_returnsBigDecimal() {
		when(repo.findById(anyLong()))
			.thenReturn(Optional.of(O_ALQUIMISTA));
		BigDecimal sell = bookSellerImpl.sellBooks(BOOK_SELL_LIST_DTO);
		assertNotNull(sell);
		assertEquals(
			O_ALQUIMISTA_PRICE.multiply(BigDecimal.valueOf(SELL_UNITS)), 
			sell);
		verify(repo, times(1)).findById(anyLong());
	}
	
	@Test
	@DisplayName("Deve lançar 'InsuficientSalableBookUnitsException' ao tentar realizar venda com unidades insuficientes")
	void sellBooks_throwsInsuficientSalableBookUnitsException() {
		when(repo.findById(anyLong()))
			.thenReturn(Optional.of(O_ALQUIMISTA_0_UNITS));
		assertThrows(
			InsuficientSalableBookUnitsException.class,
			() -> bookSellerImpl.sellBooks(BOOK_SELL_LIST_DTO));
		verify(repo, times(1)).findById(anyLong());
	}
}