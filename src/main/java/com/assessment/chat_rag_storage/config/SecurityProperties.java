package com.assessment.chat_rag_storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "security")
@Data
@Component
public class SecurityProperties {

	private String apiKey;

}
