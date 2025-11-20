package caio.portfolio.livraria.controller.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import caio.portfolio.livraria.exception.custom.country.CountryNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionHandlerController {

	private static final String TIME_STAMP = "timestamp";
	private static final String STATUS = "status";
	private static final String ERROR = "error";
	private static final String MESSAGE = "message";
	private static final String PATH = "path";
	private static final String DETAILS = "details";
	
	@SuppressWarnings("removal")
	private List<String> extractValidationErrors(HandlerMethodValidationException e) {
	    return e.getAllValidationResults().stream()
	        .flatMap(result -> result.getResolvableErrors().stream())
	        .map(error -> error.getDefaultMessage())
	        .toList();
	}
	
	private List<String> extractBindingResults(MethodArgumentNotValidException e){
		return e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> error.getField()+": "+error.getDefaultMessage())
			.toList();
	}
	
	private Map<String, Object> createErrorBody(HttpStatus status, String msg, String path, Object details){
		Map<String, Object> body = new LinkedHashMap<>();
		body.put(TIME_STAMP, LocalDateTime.now());
		body.put(STATUS, status.value());
		body.put(ERROR, status.getReasonPhrase());
		body.put(MESSAGE, msg);
		body.put(PATH, path);
		body.put(DETAILS, details);
		return body;
	}
	
	@ExceptionHandler(HandlerMethodValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleValidationException(
	    HandlerMethodValidationException e,
	    HttpServletRequest httpRequest
	) {
		Map<String, Object> body = createErrorBody(
			HttpStatus.BAD_REQUEST,
			"Erro de validação nos parâmetros",
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
	        "O país solicitado não foi encontrado no sistema"
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
	        "Argumento inválido fornecido na requisição"
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
	        "Erro de validação nos campos da requisição",
	        httpRequest.getRequestURI(),
	        extractBindingResults(e)
	    );
	    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
}