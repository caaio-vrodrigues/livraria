package caio.portfolio.livraria.exception.custom.author;

public class AuthorNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AuthorNotFoundException(String msg) {
		super(msg);
	}
}
