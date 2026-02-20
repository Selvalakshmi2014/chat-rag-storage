package com.assessment.chat_rag_storage.dto;

import jakarta.validation.constraints.NotBlank;

public record RenameSessionRequest(@NotBlank(message = "Session name required") String sessionName

) {
}
