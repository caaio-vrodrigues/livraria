package caio.portfolio.livraria.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import caio.portfolio.livraria.infrastructure.entity.publisher.Publisher;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {}
