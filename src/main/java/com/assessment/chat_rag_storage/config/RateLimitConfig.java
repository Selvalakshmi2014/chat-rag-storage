package com.assessment.chat_rag_storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@ConfigurationProperties(prefix = "api.rate-limit")
@Data
@Component
public class RateLimitConfig {

	private long capacity;

	private long refillTokens;

	private Duration refillDurationMinutes;

}
