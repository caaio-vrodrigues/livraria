package caio.portfolio.livraria.service.book.salable;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;
import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.infrastructure.repository.SalableBookRepository;
import caio.portfolio.livraria.model.enums.Genre;
import caio.portfolio.livraria.service.book.salable.model.SalableBookFinder;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SalableBookFinderImpl implements SalableBookFinder {
	
	private final SalableBookRepository repo;
	private final MessageSource messageSource;
	
	@Override
	public SalableBook findById(Long id) {
		return repo.findById(id).orElseThrow(() -> 
			new SalableBookNotFoundException(messageSource.getMessage(
				"book.not.found.id", 
				new Object[]{id}, 
				LocaleContextHolder.getLocale())));
	}

	@Override
	public List<SalableBook> findByAuthor(Author author) {
		List<SalableBook> bookList = repo.findByAuthor(author);
		if(bookList.isEmpty()) 
			throw new SalableBookNotFoundException(messageSource.getMessage(
				"book.not.found.authorId", 
				new Object[]{author.getId()}, 
				LocaleContextHolder.getLocale()));
		return bookList;
	}

	@Override
	public List<SalableBook> findByPublisher(Publisher publisher) {
		List<SalableBook> bookList = repo.findByPublisher(publisher);
		if(bookList.isEmpty()) 
			throw new SalableBookNotFoundException(messageSource.getMessage(
				"book.not.found.publisherId", 
				new Object[]{publisher.getId()}, 
				LocaleContextHolder.getLocale()));
		return bookList;
	}

	@Override
	public List<SalableBook> findByTitle(String title) {
		List<SalableBook> bookList = repo.findByTitle(title);
		if(bookList.isEmpty())
			throw new SalableBookNotFoundException(messageSource.getMessage(
				"book.not.found.title", 
				new Object[]{title}, 
				LocaleContextHolder.getLocale()));
		return bookList;
	}

	@Override
	public List<SalableBook> findByGenre(Genre genre) {
		List<SalableBook> bookList = repo.findByGenre(genre);
		if(bookList.isEmpty())
			throw new SalableBookNotFoundException(messageSource.getMessage(
				"book.not.found.genre", 
				new Object[]{genre}, 
				LocaleContextHolder.getLocale()));
		return bookList;
	}

	@Override
	public List<SalableBook> findByIsbn(String isbn) {
		List<SalableBook> bookList = repo.findByIsbn(isbn);
		if(bookList.isEmpty())
			throw new SalableBookNotFoundException(messageSource.getMessage(
				"book.not.found.isbn", 
				new Object[]{isbn}, 
				LocaleContextHolder.getLocale()));
		return bookList;
	}

	@Override
	public boolean existsById(Long bookId) {
		if(!repo.existsById(bookId)) 
			throw new SalableBookNotFoundException(messageSource.getMessage(
				"book.not.found.id", 
				new Object[]{bookId}, 
				LocaleContextHolder.getLocale()));
		return true;
	}
}