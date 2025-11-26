package caio.portfolio.livraria.exception.custom.publisher;

public class ConcurrentPublisherException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConcurrentPublisherException(String msg) {
		super(msg);
	}
}
