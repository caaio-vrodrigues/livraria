package caio.portfolio.livraria.infrastructure.entity.author;

import java.time.LocalDate;
import java.util.Objects;

import caio.portfolio.livraria.infrastructure.entity.country.Country;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name="author")
public class Author {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="alias", nullable=false, unique=true)
	private String alias;
	
	@Column(name="fullname", nullable=false)
	private String fullName;
	
	@Column(name="birthday", nullable=false)
	private LocalDate birthday;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="country_id", nullable=false)
	private Country country;
	
	@Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Author)) return false;
        Author author = (Author) o;
        return id != null && id.equals(author.id);
    }

	@Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
