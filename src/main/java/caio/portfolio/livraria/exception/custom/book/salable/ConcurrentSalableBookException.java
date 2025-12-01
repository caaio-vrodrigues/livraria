package caio.portfolio.livraria.exception.custom.book.salable;

public class ConcurrentSalableBookException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConcurrentSalableBookException(String msg) {
		super(msg);
	}
}
