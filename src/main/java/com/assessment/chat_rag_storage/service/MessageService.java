package com.assessment.chat_rag_storage.service;

import com.assessment.chat_rag_storage.dto.MessageRequestDto;
import com.assessment.chat_rag_storage.dto.MessageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MessageService {

	public MessageResponseDto addMessage(MessageRequestDto messageRequestDto);

	public Page<MessageResponseDto> getAllBySessionId(UUID session_id, Pageable pageable);

}
