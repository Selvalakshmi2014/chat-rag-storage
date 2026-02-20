package com.assessment.chat_rag_storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@Component
@ConfigurationProperties(prefix = "cors")
@Data
public class CorsProperties {

	private String allowedOrigins;

	private String allowedMethods;

	private Long maxAge;

	public List<String> getAllowedOriginsList() {
		return Arrays.stream(allowedOrigins.split(",")).map(String::trim).collect(Collectors.toList());
	}

	public List<String> getAllowedMethodsList() {
		return Arrays.stream(allowedMethods.split(",")).map(String::trim).collect(Collectors.toList());
	}

}