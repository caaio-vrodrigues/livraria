package caio.portfolio.livraria.exception.custom.book.salable;

public class SalableBookAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SalableBookAlreadyExistsException(String msg) {
		super(msg);
	}
}
