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
	
	@Column(name="fullname", nullable=false)
	private String fullName;
	
	@Column(name="birthday", nullable=false)
	private LocalDate birthday;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="country", nullable=false)
	private Country country;
	
    @Override
    public boolean equals(Object obj) {
    	boolean isSameInstance = this == obj;
        if(isSameInstance) return true;
        boolean isDifferentClassOrNull = obj == null || getClass() != obj.getClass();
        if(isDifferentClassOrNull) return false;
        Author other = (Author) obj;
        boolean isDifferentObjects = other.id == null || id == null;
        if(isDifferentObjects) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : super.hashCode();
    }
}
