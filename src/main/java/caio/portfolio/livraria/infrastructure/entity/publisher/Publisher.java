package caio.portfolio.livraria.infrastructure.entity.publisher;

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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name="publisher")
public class Publisher {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="countryId", nullable=false)
	private Country country;
	
	@Column(name="full_address", nullable=false, unique=true)
	private String fullAddress;
	
	@Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Publisher)) return false;
        Publisher publisher = (Publisher) o;
        return id != null && id.equals(publisher.id);
    }

	@Override
    public int hashCode() {
        return Objects.hash(id);
    }
}