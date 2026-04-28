package com.uditgoel.groomify.security;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Translates auth failures into 401 JSON responses. Writes the response directly
 * (rather than {@code sendError}) to avoid container error-page dispatch and
 * spurious {@code WWW-Authenticate} headers.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
			throws IOException {
		logger.debug("Unauthorized: {}", ex.getMessage());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		String message = ex.getMessage() == null ? "Unauthorized" : ex.getMessage();
		response.getWriter().write("{\"error\":\"unauthorized\",\"message\":\"" + escape(message) + "\"}");
	}

	private static String escape(String s) {
		return s.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}
