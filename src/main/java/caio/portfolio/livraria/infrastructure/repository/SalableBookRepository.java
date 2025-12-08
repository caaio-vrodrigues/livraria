package caio.portfolio.livraria.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.model.enums.Genre;

public interface SalableBookRepository extends JpaRepository<SalableBook, Long> {
	Optional<SalableBook> findByTitleAndAuthor(String title, Author author);
	List<SalableBook> findByAuthor(Author author);
	List<SalableBook> findByPublisher(Publisher publisher);
	List<SalableBook> findByTitle(String title);
	List<SalableBook> findByGenre(Genre genre);
	List<SalableBook> findByIsbn(String isbn);
}