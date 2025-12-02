package caio.portfolio.livraria.exception.custom.book.salable;

public class SalableBookNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SalableBookNotFoundException(String msg) {
		super(msg);
	}
}
