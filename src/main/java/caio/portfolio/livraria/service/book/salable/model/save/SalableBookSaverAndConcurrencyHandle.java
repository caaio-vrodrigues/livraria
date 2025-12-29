package caio.portfolio.livraria.service.book.salable.model.save;

import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;

public interface SalableBookSaverAndConcurrencyHandle {
	SalableBook saveAndHandleConcurrency(SalableBook book);
}
