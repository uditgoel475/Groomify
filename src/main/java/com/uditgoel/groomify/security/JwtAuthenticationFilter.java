package com.uditgoel.groomify.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.uditgoel.groomify.dto.JwtJsonSubjectKey;

/**
 * Extracts and validates the bearer JWT on every request. Signature + expiry only — no Redis.
 * If valid, populates SecurityContext with the user loaded via CustomUserDetailsService.
 *
 * <p>If anything goes wrong (no header, invalid token, downstream exception) the request still
 * proceeds without an authenticated SecurityContext; downstream authorization rules
 * ({@code .authenticated()}, {@code @PreAuthorize}) reject it with 401/403 via
 * {@link JwtAuthenticationEntryPoint}.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	private static final String BEARER_PREFIX = "Bearer ";

	private final JwtTokenProvider tokenProvider;
	private final CustomUserDetailsService customUserDetailsService;

	public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService customUserDetailsService) {
		this.tokenProvider = tokenProvider;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = getJwtFromRequest(request);
			if (StringUtils.hasText(jwt) && tokenProvider.validateAccessToken(jwt)) {
				JwtJsonSubjectKey subjectKey = tokenProvider.getUserIdFromJWT(jwt);
				UserDetails userDetails = customUserDetailsService.loadUserById(subjectKey);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception ex) {
			log.warn("Could not set user authentication in security context: {}", ex.getMessage());
			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
			return authHeader.substring(BEARER_PREFIX.length()).trim();
		}
		return null;
	}
}
