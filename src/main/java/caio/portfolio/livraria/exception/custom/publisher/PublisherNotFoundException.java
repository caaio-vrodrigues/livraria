package caio.portfolio.livraria.exception.custom.publisher;

public class PublisherNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PublisherNotFoundException(String msg) {
		super(msg);
	}
}