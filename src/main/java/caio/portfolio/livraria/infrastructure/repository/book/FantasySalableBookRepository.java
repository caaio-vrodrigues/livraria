package caio.portfolio.livraria.infrastructure.repository.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import caio.portfolio.livraria.infrastructure.entity.book.salable.fantasy.FantasySalableBook;

@Repository
public interface FantasySalableBookRepository extends JpaRepository<FantasySalableBook, Long> {}