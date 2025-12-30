package caio.portfolio.livraria.controller.exception;

import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.AssertionFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.exception.custom.author.ConcurrentAuthorException;
import caio.portfolio.livraria.exception.custom.book.salable.ConcurrentSalableBookException;
import caio.portfolio.livraria.exception.custom.book.salable.InsuficientSalableBookUnitsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.book.salable.SalableBookNotFoundException;
import caio.portfolio.livraria.exception.custom.country.ConcurrentCountryException;
import caio.portfolio.livraria.exception.custom.country.CountryNotFoundException;
import caio.portfolio.livraria.exception.custom.publisher.ConcurrentPublisherException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.publisher.PublisherNotFoundException;
import caio.portfolio.livraria.exception.model.ExceptionHandlerMessageCreator;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionHandlerController {
	
	@Autowired private ExceptionHandlerMessageCreator exceptionHandlerMessageCreator;

	private static final String TIME_STAMP = "timestamp";
	private static final String STATUS = "status";
	private static final String ERROR = "error";
	private static final String MESSAGE = "message";
	private static final String PATH = "path";
	private static final String DETAILS = "details";
	
	@SuppressWarnings("removal")
	private List<String> extractValidationErrors(
		HandlerMethodValidationException e
	){
	    return e.getAllValidationResults().stream()
	        .flatMap(result -> result.getResolvableErrors().stream())
	        .map(error -> error.getDefaultMessage())
	        .toList();
	}
	
	private List<String> extractBindingResults(
		MethodArgumentNotValidException e
	){
		return e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> error.getField()+": "+error.getDefaultMessage())
			.toList();
	}
	
	private Map<String, Object> createErrorBody(
		HttpStatus status, 
		String msg, 
		String path, 
		Object details
	){
		Map<String, Object> body = new LinkedHashMap<>();
		body.put(TIME_STAMP, LocalDateTime.now());
		body.put(STATUS, status.value());
		body.put(ERROR, status.getReasonPhrase());
		body.put(MESSAGE, msg);
		body.put(PATH, path);
		body.put(DETAILS, details);
		return body;
	}
	
	@ExceptionHandler(InsuficientSalableBookUnitsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> handleInsuficientSalableBookUnitsException(
		InsuficientSalableBookUnitsException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.CONFLICT,
	        e.getMessage(),
	        httpRequest.getRequestURI(),
	        exceptionHandlerMessageCreator.insuficientSalableBookUnitsCreateMsg()
	    );
	    return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(SalableBookNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> handleSalableBookNotFoundException(
		SalableBookNotFoundException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.NOT_FOUND,
	        e.getMessage(),
	        httpRequest.getRequestURI(),
	        exceptionHandlerMessageCreator.salableBookNotFoundCreateMsg()
	    );
	    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ConcurrentSalableBookException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> handleConcurrentSalableBookException(
		ConcurrentSalableBookException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.CONFLICT,
	        e.getMessage(),
	        httpRequest.getRequestURI(),
	        exceptionHandlerMessageCreator.concurrentSalableBookCreateMsg()
	    );
	    return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(SalableBookAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> handleSalableBookAlreadyExistsException(
		SalableBookAlreadyExistsException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.CONFLICT,
	        e.getMessage(),
	        httpRequest.getRequestURI(),
	        exceptionHandlerMessageCreator.salableBookAlreadyExistsCreateMsg()
	    );
	    return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(PublisherNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> handlePublisherNotFoundException(
		PublisherNotFoundException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.NOT_FOUND,
	        e.getMessage(),
	        httpRequest.getRequestURI(),
	        exceptionHandlerMessageCreator.publisherNotFoundCreateMsg()
	    );
	    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ConcurrentPublisherException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> handleConcurrentPublisherException(
		ConcurrentPublisherException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.CONFLICT,
	        e.getMessage(),
	        httpRequest.getRequestURI(),
	        exceptionHandlerMessageCreator.concurrentPublisherCreateMsg()
	    );
	    return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(PublisherAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> handlePublisherAlreadyExistsException(
		PublisherAlreadyExistsException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.CONFLICT,
	        e.getMessage(),
	        httpRequest.getRequestURI(),
	        exceptionHandlerMessageCreator.publisherAlreadyExistsCreateMsg()
	    );
	    return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleHttpMessageNotReadableException(
	    HttpMessageNotReadableException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.BAD_REQUEST,
	        e.getMessage(),
	        httpRequest.getRequestURI(),
	        exceptionHandlerMessageCreator.messageNotReadableCreateMsg()
	    );
	    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConcurrentAuthorException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> handleConcurrentAuthorException(
		ConcurrentAuthorException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.CONFLICT,
	        e.getMessage(),
	        httpRequest.getRequestURI(),
	        exceptionHandlerMessageCreator.concurrentAuthorCreateMsg()
	    );
	    return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(AuthorNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> handleAuthorNotFoundException(
		AuthorNotFoundException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.NOT_FOUND,
	        e.getMessage(),
	        httpRequest.getRequestURI(),
	        exceptionHandlerMessageCreator.authorNotFoundCreateMsg()
	    );
	    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(AuthorAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> handleAuthorAlreadyExistsException(
		AuthorAlreadyExistsException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.CONFLICT,
	        e.getMessage(),
	        httpRequest.getRequestURI(),
	        exceptionHandlerMessageCreator.authorAlreadyExistsCreateMsg()
	    );
	    return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ConcurrentCountryException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> handleConcurrentCountryException(
		ConcurrentCountryException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.CONFLICT,
	        e.getMessage(),
	        httpRequest.getRequestURI(),
	        exceptionHandlerMessageCreator.concurrentCountryCreateMsg()
	    );
	    return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(HandlerMethodValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleValidationException(
	    HandlerMethodValidationException e,
	    HttpServletRequest httpRequest
	) {
		Map<String, Object> body = createErrorBody(
			HttpStatus.BAD_REQUEST,
			exceptionHandlerMessageCreator.validationCreateMsg(),
			httpRequest.getRequestURI(),
			extractValidationErrors(e)
	    );
	    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CountryNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> handleCountryNotFoundException(
	    CountryNotFoundException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.NOT_FOUND,
	        e.getMessage(),
	        httpRequest.getRequestURI(),
	        exceptionHandlerMessageCreator.countryNotFoundCreateMsg()
	    );
	    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleIllegalArgumentException(
	    IllegalArgumentException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.BAD_REQUEST,
	        e.getMessage(),
	        httpRequest.getRequestURI(),
	        exceptionHandlerMessageCreator.illegalArgumentCreateMsg()
	    );
	    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleMethodArgumentNotValidException(
	    MethodArgumentNotValidException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.BAD_REQUEST,
	        exceptionHandlerMessageCreator.methodArgumentNotValidCreateMsg(),
	        httpRequest.getRequestURI(),
	        extractBindingResults(e)
	    );
	    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HttpMessageConversionException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleHttpMessageConversionException(
		HttpMessageConversionException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = createErrorBody(
	        HttpStatus.BAD_REQUEST,
	        exceptionHandlerMessageCreator.httpMessageConversionCreateMsg(),
	        httpRequest.getRequestURI(),
	        null
	    );
	    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNoResourceFoundException(
        NoResourceFoundException e,
        HttpServletRequest httpRequest
    ){
        Map<String, Object> body = createErrorBody(
            HttpStatus.NOT_FOUND,
            exceptionHandlerMessageCreator.noResourceFoundCreateMsg(),
            httpRequest.getRequestURI(),
            null
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(AssertionFailure.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAssertionFailure(
        AssertionFailure e,
        HttpServletRequest httpRequest
    ){
        Map<String, Object> body = createErrorBody(
            HttpStatus.INTERNAL_SERVER_ERROR,
            exceptionHandlerMessageCreator.assertionFailureCreateMsg(),
            httpRequest.getRequestURI(),
            null
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}