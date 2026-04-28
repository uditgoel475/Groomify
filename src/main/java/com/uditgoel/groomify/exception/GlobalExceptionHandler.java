package com.uditgoel.groomify.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Maps common runtime exceptions to clean JSON HTTP responses. Without this,
 * Spring lets {@link IllegalArgumentException} propagate as an HTTP 500 with a
 * full stack trace in logs, which is noisy and misleading — they're 400-class
 * client errors.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex,
			HttpServletRequest request) {
		return body(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex,
			HttpServletRequest request) {
		// IllegalStateException usually means broken server config (Redis down, etc.) —
		// a 503 reflects "this request couldn't proceed because of server state".
		logger.warn("IllegalState during {} {}: {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
		return body(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), request);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex,
			HttpServletRequest request) {
		// Most-specific cause is the DB constraint name; surface as 409 since it's a
		// uniqueness/check violation rather than a server-internal failure.
		String message = ex.getMostSpecificCause().getMessage();
		return body(HttpStatus.CONFLICT, message, request);
	}

	private static ResponseEntity<Map<String, Object>> body(HttpStatus status, String message,
			HttpServletRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", Instant.now().toString());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		body.put("path", request.getRequestURI());
		return ResponseEntity.status(status).body(body);
	}
}
