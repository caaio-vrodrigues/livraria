package caio.portfolio.livraria.service.book.salable.model;

import java.util.List;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.model.enums.Genre;

public interface SalableBookFinder {
	SalableBook findById(Long id);
	List<SalableBook> findByAuthor(Author author);
	List<SalableBook> findByPublisher(Publisher publisher);
	List<SalableBook> findByTitle(String title);
	List<SalableBook> findByGenre(Genre genre);
	List<SalableBook> findByIsbn(String isbn);
	boolean existsById(Long bookId);
}