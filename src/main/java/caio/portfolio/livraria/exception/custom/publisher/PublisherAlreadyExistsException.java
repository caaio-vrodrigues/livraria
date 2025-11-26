package caio.portfolio.livraria.exception.custom.publisher;

public class PublisherAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PublisherAlreadyExistsException(String msg) {
		super(msg);
	}
}
