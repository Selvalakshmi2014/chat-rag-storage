package com.assessment.chat_rag_storage.controller;

import com.assessment.chat_rag_storage.dto.RenameSessionRequest;
import com.assessment.chat_rag_storage.dto.SessionRequestDTO;
import com.assessment.chat_rag_storage.dto.SessionResponseDto;
import com.assessment.chat_rag_storage.entity.Session;
import com.assessment.chat_rag_storage.service.SessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@Tag(name = "Sessions", description = "Operations related to  chat sessions")
@RequiredArgsConstructor
public class SessionController {

	private final SessionService service;

	@GetMapping
	public ResponseEntity<List<SessionResponseDto>> getAllSessions() {
		return ResponseEntity.ok(service.getAllSessions());
	}

	@PostMapping
	public ResponseEntity<SessionResponseDto> createSession(@Valid @RequestBody SessionRequestDTO sessionRequestDTO) {
		System.out.println("session Req" + sessionRequestDTO);
		SessionResponseDto savedSession = service.createSession(sessionRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedSession);
	}

	@PatchMapping("/{session_id}/favorite")
	public ResponseEntity<SessionResponseDto> updateFavorite(@PathVariable("session_id") UUID session_id,
			@RequestParam boolean favorite) {
		SessionResponseDto updatedSession = service.updateFavorite(session_id, favorite);
		return ResponseEntity.status(HttpStatus.OK).body(updatedSession);
	}

	@PatchMapping("/{session_id}/rename")
	public ResponseEntity<SessionResponseDto> renameSession(@PathVariable("session_id") UUID session_id,
			@Valid @RequestBody RenameSessionRequest req) {
		return ResponseEntity.ok(service.updateSessionName(session_id, req.sessionName()));
	}

	@DeleteMapping("/{session_id}")
	public ResponseEntity<Void> deleteSession(@PathVariable("session_id") UUID session_id) {
		service.deleteSession(session_id);
		return ResponseEntity.noContent().build();
	}

}
