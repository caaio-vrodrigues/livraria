package caio.portfolio.livraria.service.book.salable.model.sell;

import java.math.BigDecimal;

import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.BookSellListDTO;

public interface BookSeller {
	BigDecimal sellBook(Long bookId, int units);
	BigDecimal sellBooks(BookSellListDTO bookListDTO);
}
