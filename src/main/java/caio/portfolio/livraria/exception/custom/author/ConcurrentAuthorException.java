package caio.portfolio.livraria.exception.custom.author;

public class ConcurrentAuthorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConcurrentAuthorException(String msg) {
		super(msg);
	}
}
