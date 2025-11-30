package caio.portfolio.livraria.infrastructure.entity.book.salable;

import java.math.BigDecimal;

import caio.portfolio.livraria.infrastructure.entity.book.Book;
import jakarta.persistence.Column;
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
public abstract class SalableBook extends Book {

	@Column(name="price", nullable=false)
	private BigDecimal price;
	
	@Column(name="units", nullable=false)
	private BigDecimal units;
}
