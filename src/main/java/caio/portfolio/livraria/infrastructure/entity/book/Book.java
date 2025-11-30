package caio.portfolio.livraria.infrastructure.entity.book;

import caio.portfolio.livraria.infrastructure.entity.author.Author;
import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
public abstract class Book {

	@Column(name="title", nullable=false)
	private String title;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="author_id", nullable=false)
	private Author author;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="publisher_id", nullable=false)
	private Publisher publisher;
	
	@Column(name="isbn", nullable=false)
	private String isbn;
}