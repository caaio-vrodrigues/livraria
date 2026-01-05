package caio.portfolio.livraria.service.publisher.implementation.create;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.publisher.ConcurrentPublisherException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherNotFoundException;
import caio.portfolio.livraria.service.publisher.model.create.PublisherExceptionCreator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublisherExceptionCreatorImpl implements PublisherExceptionCreator {
	
	private final MessageSource publisherMessageSource;
	
	@Override
	public ConcurrentPublisherException createConcurrentPublisherException(
		String publisherName
	){
		String msg = publisherMessageSource.getMessage(
			"publisher.concurrent", 
			new Object[]{publisherName},
			LocaleContextHolder.getLocale());
		return new ConcurrentPublisherException(msg);
	}

	@Override
	public PublisherAlreadyExistsException createPublisherAlreadyExistsException(
		String fullAddress
	){
		String msg = publisherMessageSource.getMessage(
			"publisher.already.exists.fullAddress", 
			new Object[]{fullAddress},
			LocaleContextHolder.getLocale());
		return new PublisherAlreadyExistsException(msg);
	}

	@Override
	public PublisherNotFoundException createPublisherNotFoundException(Long id) {
		String msg = publisherMessageSource.getMessage(
			"publisher.not.found.id.throws", 
			new Object[]{id},
			LocaleContextHolder.getLocale());
		return new PublisherNotFoundException(msg);
	}
}