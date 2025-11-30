package caio.portfolio.livraria.infrastructure.entity.book.salable.romance;

import java.util.Objects;

import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.model.enums.Genre;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name="romance_salable_book")
public class RomanceSalableBook extends SalableBook {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name="genre", nullable=false)
	private Genre genre;
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof RomanceSalableBook)) return false;
		RomanceSalableBook book = (RomanceSalableBook) o;
		return id != null && id.equals(book.getId());
	}
	
	@Override
    public int hashCode() {
        return Objects.hash(id);
    }
}