package com.assessment.chat_rag_storage.controller;

import com.assessment.chat_rag_storage.dto.MessageRequestDto;
import com.assessment.chat_rag_storage.dto.MessageResponseDto;
import com.assessment.chat_rag_storage.entity.Message;
import com.assessment.chat_rag_storage.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/messages")
@Tag(name = "Messages", description = "Operations related to messages")
@RequiredArgsConstructor
public class MessageController {

	private final MessageService messageService;

	@PostMapping
	public ResponseEntity<MessageResponseDto> createMessage(@Valid @RequestBody MessageRequestDto messageRequestDto) {
		MessageResponseDto createdMessage = messageService.addMessage(messageRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
	}

	@GetMapping("/{session_id}")
	public ResponseEntity<Page<MessageResponseDto>> getAll(@PathVariable("session_id") UUID sessionId,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "0") int page) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
		Page<MessageResponseDto> messageList = messageService.getAllBySessionId(sessionId, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(messageList);
	}

}
