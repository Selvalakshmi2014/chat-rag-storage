package com.assessment.chat_rag_storage.service;

import com.assessment.chat_rag_storage.dto.SessionRequestDTO;
import com.assessment.chat_rag_storage.dto.SessionResponseDto;
import com.assessment.chat_rag_storage.entity.Session;

import java.util.List;
import java.util.UUID;

public interface SessionService {

	public List<SessionResponseDto> getAllSessions();

	public SessionResponseDto createSession(SessionRequestDTO sessionRequestDTO);

	public SessionResponseDto updateFavorite(UUID session_id, boolean favorite);

	public SessionResponseDto updateSessionName(UUID session_id, String sessionName);

	public void deleteSession(UUID session_id);

}
