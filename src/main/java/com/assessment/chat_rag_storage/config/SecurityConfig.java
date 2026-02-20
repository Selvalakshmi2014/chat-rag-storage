package com.assessment.chat_rag_storage.config;

import com.assessment.chat_rag_storage.filter.ApiKeyAuthFilter;
import com.assessment.chat_rag_storage.filter.RateLimitFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final ApiKeyAuthFilter apiKeyAuthFilter;

	private final RateLimitFilter rateLimitFilter;

	private final CorsProperties corsProperties;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(request -> {
			var corsConfig = new org.springframework.web.cors.CorsConfiguration();
			corsConfig.setAllowedOrigins(corsProperties.getAllowedOriginsList());
			corsConfig.setAllowedMethods(corsProperties.getAllowedMethodsList());
			corsConfig.setAllowedHeaders(List.of("*"));
			corsConfig.setAllowCredentials(true);
			corsConfig.setMaxAge(corsProperties.getMaxAge());
			return corsConfig;
		}))
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
			.addFilterBefore(apiKeyAuthFilter,
					org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(rateLimitFilter, ApiKeyAuthFilter.class);

		return http.build();
	}

	@Bean
	public org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
			.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/webjars/**",
					"/actuator/**");
	}

}
