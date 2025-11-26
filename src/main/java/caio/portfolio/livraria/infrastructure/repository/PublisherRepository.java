package caio.portfolio.livraria.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
	Optional<Publisher> findByFullAddress(String fullAddress);
}
