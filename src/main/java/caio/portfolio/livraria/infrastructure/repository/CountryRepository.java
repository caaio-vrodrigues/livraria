package caio.portfolio.livraria.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import caio.portfolio.livraria.infrastructure.entity.country.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
	Optional<Country> findByIsoAlpha2Code(String isoAlpha2Code);
}
