package caio.portfolio.livraria.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;

public interface SalableBookRepository extends JpaRepository<SalableBook, Long> {}