package com.assessment.chat_rag_storage.filter;

import com.assessment.chat_rag_storage.config.RateLimitConfig;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

	private final Bucket bucket;

	private static final long CAPACITY = 10;

	private static final Duration REFILL_DURATION = Duration.ofMinutes(1);

	private static final long TOKEN = 10;

	public RateLimitFilter(RateLimitConfig rateLimitConfig) {
		this.bucket = Bucket.builder()
			.addLimit(limit -> limit.capacity(rateLimitConfig.getCapacity())
				.refillGreedy(rateLimitConfig.getRefillTokens(), rateLimitConfig.getRefillDurationMinutes()))
			.build();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

		if (probe.isConsumed()) {
			response.addHeader("X-Rate-Limit-Limit", String.valueOf(CAPACITY));
			response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
			response.addHeader("X-Rate-Limit-Retry-After", "0");

			filterChain.doFilter(request, response);
		}
		else {
			long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
			response.setStatus(429);
			response.setContentType("application/json");
			response.getWriter().write("""
					{
					  "error":"Too Many Requests",
					  "message":Rate limit exceeded. Retry after %d  seconds"
					}
					""".formatted(waitForRefill));
		}

	}

}
