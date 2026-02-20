package com.assessment.chat_rag_storage.dto;

import com.assessment.chat_rag_storage.entity.Session;
import com.assessment.chat_rag_storage.enums.SenderType;
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
public class MessageResponseDto {

	private Long id;

	private SenderType sender;

	private String content;

	private Map<String, Object> context;

	private UUID sessionId;

}
