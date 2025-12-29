package caio.portfolio.livraria.service.book.salable.implementation.sell;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.BookSellListDTO;
import caio.portfolio.livraria.service.book.salable.model.find.SalableBookFinder;
import caio.portfolio.livraria.service.book.salable.model.sell.BookSeller;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookSellerImpl implements BookSeller {

	private final SalableBookFinder salableBookFinder;

	@Override
	public BigDecimal sellBook(Long bookId, int units) {
		SalableBook book = salableBookFinder.findById(bookId);
		return BigDecimal.valueOf(units)
			.multiply(book.getPrice());
	}

	@Override
	public BigDecimal sellBooks(BookSellListDTO bookListDTO) {
		return bookListDTO.getSellList()
			.stream()
			.map(bookSellDTO -> sellBook(
				bookSellDTO.getBookId(), 
				bookSellDTO.getUnits()))
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}