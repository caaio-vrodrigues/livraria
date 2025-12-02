package caio.portfolio.livraria.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import caio.portfolio.livraria.model.enums.Genre;

public interface SalableBookRepository extends JpaRepository<SalableBook, Long> {
	Optional<SalableBook> findByTitleAndAuthorAndPublisher(
		String title, 
		Author author, 
		Publisher publisher);
	
	Optional<List<SalableBook>> findByAuthor(Author author);
	Optional<List<SalableBook>> findByPublisher(Publisher publisher);
	Optional<SalableBook> findByTitle(String title);
	Optional<List<SalableBook>> findByGenre(Genre genre);
	Optional<List<SalableBook>> findByIsbn(String isbn);
}