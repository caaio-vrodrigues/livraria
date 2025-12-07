package caio.portfolio.livraria.service.book.salable;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.repository.SalableBookRepository;
import caio.portfolio.livraria.service.book.salable.model.BookSeller;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookSellerImpl implements BookSeller {
	
	private final SalableBookRepository repo;

	@Override
	public BigDecimal sellBook(Long bookId, int units) {
		SalableBook book = repo.findById(bookId).orElseThrow(() -> 
			new SalableBookNotFoundException("Não foi possível encontrar livro com 'id': '"+bookId+"'"));
		book.decreaseUnits(units);
		return BigDecimal.valueOf(units).multiply(book.getPrice());
	}
}