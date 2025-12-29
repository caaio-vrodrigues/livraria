package caio.portfolio.livraria.service.book.salable.model.create;

import caio.portfolio.livraria.infrastructure.entity.book.salable.SalableBook;
import caio.portfolio.livraria.infrastructure.entity.book.salable.dto.ResponseSalableBookDTO;

public interface ResponseSalableBookDTOCreator {

	ResponseSalableBookDTO toResponseSalableBookDTO(SalableBook book);
}
