package com.assessment.chat_rag_storage.dto;

import com.assessment.chat_rag_storage.entity.Session;
import com.assessment.chat_rag_storage.enums.SenderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequestDto {

	@NotNull(message = "Sender is required")
	private SenderType sender;

	@NotBlank(message = "Content is required")
	private String content;

	private Map<String, Object> context;

	@NotNull(message = "Session ID is required")
	private UUID sessionId;

}
