package caio.portfolio.livraria.exception.custom.country;

public class ConcurrentCountryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConcurrentCountryException(String msg) {
		super(msg);
	}
}
