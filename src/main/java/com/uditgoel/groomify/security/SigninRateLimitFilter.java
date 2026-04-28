package com.uditgoel.groomify.security;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Per-IP fixed-window rate limiter on the auth signin endpoints. Throws 429 once a
 * caller exceeds {@code groomify.signin.max-attempts-per-minute} attempts within a
 * rolling minute. Sits before {@link JwtAuthenticationFilter} so brute-force probes
 * never reach the {@code BCryptPasswordEncoder} (which is the expensive bit).
 *
 * <p><b>Scope:</b> in-memory, per-instance. Adequate for a single-node deployment and
 * for development. Multi-instance deployments need a shared store (Redis bucket or
 * an upstream WAF) — the same caller would otherwise get N×instances attempts.
 */
@Component
public class SigninRateLimitFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(SigninRateLimitFilter.class);

	@Value("${groomify.signin.max-attempts-per-minute:10}")
	private int maxAttemptsPerMinute;

	private final ConcurrentMap<String, Window> windows = new ConcurrentHashMap<>();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		if (isSigninRequest(request)) {
			String key = clientKey(request);
			Window window = windows.compute(key, (k, existing) -> {
				Instant now = Instant.now();
				if (existing == null || existing.expired(now)) {
					return new Window(now);
				}
				existing.increment();
				return existing;
			});
			if (window.attempts > maxAttemptsPerMinute) {
				log.warn("signin rate limit exceeded for {} ({} attempts in window)", key, window.attempts);
				writeTooManyRequests(response, key);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private static boolean isSigninRequest(HttpServletRequest request) {
		return "POST".equals(request.getMethod()) && request.getRequestURI().endsWith("/signin");
	}

	private static String clientKey(HttpServletRequest request) {
		String forwarded = request.getHeader("X-Forwarded-For");
		if (forwarded != null && !forwarded.isBlank()) {
			int comma = forwarded.indexOf(',');
			return (comma > 0 ? forwarded.substring(0, comma) : forwarded).trim();
		}
		return request.getRemoteAddr();
	}

	private static void writeTooManyRequests(HttpServletResponse response, String key) throws IOException {
		response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setHeader("Retry-After", "60");
		response.getWriter().write(
				"{\"error\":\"too_many_requests\",\"message\":\"signin throttled; try again in 60s\"}");
	}

	private static final class Window {
		private static final Duration LENGTH = Duration.ofMinutes(1);
		private final Instant start;
		private int attempts;

		Window(Instant start) {
			this.start = start;
			this.attempts = 1;
		}

		boolean expired(Instant now) {
			return Duration.between(start, now).compareTo(LENGTH) > 0;
		}

		void increment() {
			attempts++;
		}
	}
}
