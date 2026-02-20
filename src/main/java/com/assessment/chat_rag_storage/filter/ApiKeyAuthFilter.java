package com.assessment.chat_rag_storage.filter;

import com.assessment.chat_rag_storage.config.SecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyAuthFilter extends OncePerRequestFilter {

	private final SecurityProperties props;

	private static final String HEADER = "X-API-KEY";

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getServletPath();

		return path.startsWith("/actuator") || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")
				|| path.startsWith("/swagger-ui.html") || path.startsWith("/swagger-ui/webjars");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestKey = request.getHeader(HEADER);
		String expectedKey = props.getApiKey();
		System.out.println(
				"Incoming request: " + request.getMethod() + " " + request.getRequestURI() + " API Key: " + requestKey);
		if (requestKey == null || !MessageDigest.isEqual(requestKey.getBytes(StandardCharsets.UTF_8),
				expectedKey.getBytes(StandardCharsets.UTF_8))) {
			log.warn("Unauthorized access attempt from IP: {}", request.getRemoteAddr());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("""
					        {
					          "error":"Unauthorized",
					          "message":"Invalid or missing API key"
					        }
					""");
			return;
		}

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("api-user", null,
				List.of(new SimpleGrantedAuthority("ROLE_API")));

		SecurityContextHolder.getContext().setAuthentication(auth);

		filterChain.doFilter(request, response);
	}

}
