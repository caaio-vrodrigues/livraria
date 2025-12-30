package caio.portfolio.livraria.exception.implementation;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.model.ExceptionHandlerMessageCreator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExceptionHandlerMessageCreatorImpl implements ExceptionHandlerMessageCreator {
	
	private final MessageSource exceptionHandlerMessageSource;
	
	@Override
	public String assertionFailureCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"assertion.failure",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String noResourceFoundCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"no.resource.found",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String httpMessageConversionCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"http.message.conversion",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String methodArgumentNotValidCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"method.argument.not.valid",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String illegalArgumentCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"illegal.argument",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String countryNotFoundCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"country.not.found",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String validationCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"validation",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String concurrentCountryCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"concurrent.country",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String authorAlreadyExistsCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"author.alreadyExists",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String authorNotFoundCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"author.not.found",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String concurrentAuthorCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"concurrent.author",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String messageNotReadableCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"message.not.readable",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String publisherAlreadyExistsCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"publisher.already.exists",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String concurrentPublisherCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"concurrent.publisher",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String publisherNotFoundCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"publisher.not.found",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String salableBookAlreadyExistsCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"salable.book.already.exists",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String concurrentSalableBookCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"concurrent.salable.book",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String salableBookNotFoundCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"salable.book.not.found",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}

	@Override
	public String insuficientSalableBookUnitsCreateMsg() {
		return exceptionHandlerMessageSource.getMessage(
			"insuficient.salable.book.units",
			new Object[]{},
			LocaleContextHolder.getLocale());
	}
}