package caio.portfolio.livraria.exception.custom.author;

public class AuthorAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AuthorAlreadyExistsException(String msg) {
		super(msg);
	}
}
