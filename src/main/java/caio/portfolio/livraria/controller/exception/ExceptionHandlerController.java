package caio.portfolio.livraria.controller.exception;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionHandlerController {

	private static final String TIME_STAMP = "timestamp";
	private static final String STATUS = "satatus";
	private static final String ERROR = "error";
	private static final String MESSAGE = "message";
	private static final String PATH = "path";
	private static final String DETAILS = "details";
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleIllegalArgumentException(
		IllegalArgumentException e,
		HttpServletRequest httpRequest
	){
		Map<String, Object> body = new LinkedHashMap<>();
		body.put(TIME_STAMP, LocalDate.now());
		body.put(STATUS, HttpStatus.BAD_REQUEST.value());
		body.put(ERROR, HttpStatus.BAD_REQUEST.getReasonPhrase());
		body.put(MESSAGE, e.getMessage());
		body.put(PATH, httpRequest.getRequestURI());
		body.put(DETAILS, "Argumento inválido fornecido na requisição");
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
}
