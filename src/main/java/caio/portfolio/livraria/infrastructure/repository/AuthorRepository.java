package caio.portfolio.livraria.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import caio.portfolio.livraria.infrastructure.entity.author.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {}
