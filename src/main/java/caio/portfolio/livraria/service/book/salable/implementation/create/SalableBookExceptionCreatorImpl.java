package caio.portfolio.livraria.service.book.salable.implementation.create;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.book.salable.ConcurrentSalableBookException;
import caio.portfolio.livraria.exception.custom.book.salable.InsuficientSalableBookUnitsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;
import caio.portfolio.livraria.service.book.salable.model.create.SalableBookExceptionCreator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SalableBookExceptionCreatorImpl implements SalableBookExceptionCreator {
	
	private final MessageSource salableBookMessageSource;

	@Override
	public SalableBookAlreadyExistsException createSalableBookAlreadyExistsException(
	    String authorName, 
	    String title
	){
	    String msg = salableBookMessageSource.getMessage(
	        "book.already.exists.tiltleAndAuthor",
	        new Object[]{title, authorName},
	        LocaleContextHolder.getLocale());
	    return new SalableBookAlreadyExistsException(msg);
	}

	@Override
	public ConcurrentSalableBookException createConcurrentSalableBookException(
	    String title
	){
	    String msg = salableBookMessageSource.getMessage(
	        "concurrent.book",
	        new Object[]{title},
	        LocaleContextHolder.getLocale());
	    return new ConcurrentSalableBookException(msg);
	}

	@Override
	public SalableBookNotFoundException createSalableBookNotFoundException(
		Long id
	){
		String msg = salableBookMessageSource.getMessage(
	        "book.not.found.id",
	        new Object[]{id},
	        LocaleContextHolder.getLocale());
		return new SalableBookNotFoundException(msg);
	}

	@Override
	public InsuficientSalableBookUnitsException createInsuficientSalableBookUnitsException(
		int units
	){
		String msg = salableBookMessageSource.getMessage(
	        "insuficient.book.units",
	        new Object[]{units},
	        LocaleContextHolder.getLocale());
		return new InsuficientSalableBookUnitsException(msg);
	}
}