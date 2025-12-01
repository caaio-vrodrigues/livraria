package caio.portfolio.livraria.service.book.salable;

import org.springframework.stereotype.Service;

import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.CreateSalableBookDTO;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.ResponseSalableBookDTO;
import caio.portfolio.livraria.infrastructure.repository.SalableBookRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalableBookService {

	private final SalableBookRepository repo;

	public ResponseSalableBookDTO createSalableBook(CreateSalableBookDTO dto) {
		// TODO 
		return null;
	}
}