package caio.portfolio.livraria.controller.exception;

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

import caio.portfolio.livraria.exception.custom.CountryNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionHandlerController {

	private static final String TIME_STAMP = "timestamp";
	private static final String STATUS = "status";
	private static final String ERROR = "error";
	private static final String MESSAGE = "message";
	private static final String PATH = "path";
	private static final String DETAILS = "details";
	
	@ExceptionHandler(CountryNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> handleCountryNotFoundException(
	    CountryNotFoundException e,
	    HttpServletRequest httpRequest
	){
	    Map<String, Object> body = new LinkedHashMap<>();
	    body.put(TIME_STAMP, LocalDateTime.now());
	    body.put(STATUS, HttpStatus.NOT_FOUND.value());
	    body.put(ERROR, HttpStatus.NOT_FOUND.getReasonPhrase());
	    body.put(MESSAGE, e.getMessage());
	    body.put(PATH, httpRequest.getRequestURI());
	    body.put(DETAILS, "O país solicitado não foi encontrado no sistema");
	    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleIllegalArgumentException(
		IllegalArgumentException e,
		HttpServletRequest httpRequest
	){
		Map<String, Object> body = new LinkedHashMap<>();
		body.put(TIME_STAMP, LocalDateTime.now());
		body.put(STATUS, HttpStatus.BAD_REQUEST.value());
		body.put(ERROR, HttpStatus.BAD_REQUEST.getReasonPhrase());
		body.put(MESSAGE, e.getMessage());
		body.put(PATH, httpRequest.getRequestURI());
		body.put(DETAILS, "Argumento inválido fornecido na requisição");
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e,
        HttpServletRequest httpRequest
    ){
        Map<String, Object> body = new LinkedHashMap<>();
        List<String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() +": "+ error.getDefaultMessage())
            .toList();
        body.put(TIME_STAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ERROR, HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put(MESSAGE, "Erro de validação nos campos da requisição");
        body.put(PATH, httpRequest.getRequestURI());
        body.put(DETAILS, errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}