package caio.portfolio.livraria.service.publisher.implementation.find;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.publisher.PublisherNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.repository.PublisherRepository;
import caio.portfolio.livraria.service.publisher.model.find.PublisherFinder;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublisherFinderImpl implements PublisherFinder {
	
	private final PublisherRepository repo;
	private final MessageSource publisherMessageSource;
	
	@Override
	public Publisher findByFullAddress(String fullAddress) {
		String msg = publisherMessageSource.getMessage(
			"publisher.not.found.fullAddress", 
			new Object[]{fullAddress},
			LocaleContextHolder.getLocale());
		return repo.findByFullAddress(fullAddress).orElseThrow(() ->
			new PublisherNotFoundException(msg));
	}

	@Override
	public Publisher findById(Long id) {
		String msg = publisherMessageSource.getMessage(
			"publisher.not.found.id", 
			new Object[]{id},
			LocaleContextHolder.getLocale());
		return repo.findById(id).orElseThrow(() -> 
			new PublisherNotFoundException(msg));
	}
}
