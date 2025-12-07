package caio.portfolio.livraria.service.book.salable.model;

import java.math.BigDecimal;

public interface BookSeller {
	BigDecimal sellBook(Long bookId, int units);
}
