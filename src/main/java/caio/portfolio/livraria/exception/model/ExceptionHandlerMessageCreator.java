package caio.portfolio.livraria.exception.model;

public interface ExceptionHandlerMessageCreator {
	String assertionFailureCreateMsg();
	String noResourceFoundCreateMsg();
	String httpMessageConversionCreateMsg();
	String methodArgumentNotValidCreateMsg();
	String illegalArgumentCreateMsg();
	String countryNotFoundCreateMsg();
	String validationCreateMsg();
	String concurrentCountryCreateMsg();
	String authorAlreadyExistsCreateMsg();
	String authorNotFoundCreateMsg();
	String concurrentAuthorCreateMsg();
	String messageNotReadableCreateMsg();
	String publisherAlreadyExistsCreateMsg();
	String concurrentPublisherCreateMsg();
	String publisherNotFoundCreateMsg();
	String salableBookAlreadyExistsCreateMsg();
	String concurrentSalableBookCreateMsg();
	String salableBookNotFoundCreateMsg();
	String insuficientSalableBookUnitsCreateMsg();
	String missingParameterCreateMsg(String parameterName);
}