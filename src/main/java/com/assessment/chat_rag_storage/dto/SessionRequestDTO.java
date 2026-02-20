package com.assessment.chat_rag_storage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionRequestDTO {

	@NotBlank(message = "UserId is required")
	private String userId;

	private String sessionName;

}
