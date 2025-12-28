package caio.portfolio.livraria.service.book.salable.model;

import java.util.List;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.model.enums.Genre;

public interface SalableBookFinder {
	public SalableBook findById(Long id);
	public List<SalableBook> findByAuthor(Author author);
	public List<SalableBook> findByPublisher(Publisher publisher);
	public List<SalableBook> findByTitle(String title);
	public List<SalableBook> findByGenre(Genre genre);
	public List<SalableBook> findByIsbn(String isbn);
	public boolean existsById(Long bookId);
}