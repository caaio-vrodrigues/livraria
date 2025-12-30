package caio.portfolio.livraria.service.publisher;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.publisher.ConcurrentPublisherException;
import caio.portfolio.livraria.service.publisher.model.PublisherExceptionCreator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublisherExceptionCreatorImpl implements PublisherExceptionCreator {
	
	private final MessageSource publisherMessageSource;
	
	@Override
	public ConcurrentPublisherException createConcurrentPublisherException(String publisherName) {
		String msg = publisherMessageSource.getMessage(
			"concurrent", 
			new Object[]{publisherName},
			LocaleContextHolder.getLocale());
		return new ConcurrentPublisherException(msg);
	}
}