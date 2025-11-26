package caio.portfolio.livraria.controller.exception;

import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.LocalDateTime;
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

import caio.portfolio.livraria.exception.custom.author.AuthorAlreadyExistsException;
import caio.portfolio.livraria.exception.custom.author.AuthorNotFoundException;
import caio.portfolio.livraria.exception.custom.author.ConcurrentAuthorException;
import caio.portfolio.livraria.exception.custom.country.ConcurrentCountryException;
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
	        "Corpo da requisição ilegível ou malformado. Verifique a sintaxe."
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
	        "Não foi possível criar um novo autor por violação de concorrência"
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
	        "Não foi possível encontrar um autor para o argumento fornecido"
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
	        "Violação de unicidade ao tentar criar novo autor"
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
	        "Falha ao tentar salvar país devido a concorrência durante persistência de dados"
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