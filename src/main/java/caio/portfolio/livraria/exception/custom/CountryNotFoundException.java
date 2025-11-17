package caio.portfolio.livraria.exception.custom;

public class CountryNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CountryNotFoundException(String msg) {
		super(msg);
	}
}
