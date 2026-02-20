package com.assessment.chat_rag_storage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionResponseDto {

	private UUID id;

	private String sessionName;

	private String userId;

	private Boolean favorite;

}
