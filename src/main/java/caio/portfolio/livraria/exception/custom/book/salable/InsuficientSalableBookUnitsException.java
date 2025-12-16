package caio.portfolio.livraria.exception.custom.book.salable;

public class InsuficientSalableBookUnitsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InsuficientSalableBookUnitsException(String msg) {
		super(msg);
	}
}
