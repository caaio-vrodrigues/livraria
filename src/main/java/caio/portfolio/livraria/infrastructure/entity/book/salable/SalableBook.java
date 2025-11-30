package caio.portfolio.livraria.infrastructure.entity.book.salable;

import java.math.BigDecimal;
import java.util.Objects;

import caio.portfolio.livraria.infrastructure.entity.book.Book;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Entity
@Table(name="salable_book")
public class SalableBook extends Book {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="price", nullable=false)
	private BigDecimal price;
	
	@Column(name="units", nullable=false)
	private BigDecimal units;
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof SalableBook)) return false;
		SalableBook book = (SalableBook) o;
		return id != null && id.equals(book.getId());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
