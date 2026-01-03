package caio.portfolio.livraria.service.book.salable.model.create;

import caio.portfolio.livraria.exception.custom.book.salable.ConcurrentSalableBookException;
import caio.portfolio.livraria.exception.custom.book.salable.InsuficientSalableBookUnitsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;

public interface SalableBookExceptionCreator {
	SalableBookAlreadyExistsException createSalableBookAlreadyExistsException(
		String authorName, 
		String title);
	
	ConcurrentSalableBookException createConcurrentSalableBookException(String title);
	SalableBookNotFoundException createSalableBookNotFoundException(Long id);
	InsuficientSalableBookUnitsException createInsuficientSalableBookUnitsException(int units);
}