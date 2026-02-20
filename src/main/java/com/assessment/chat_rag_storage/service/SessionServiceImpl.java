package com.assessment.chat_rag_storage.service;

import com.assessment.chat_rag_storage.dto.SessionRequestDTO;
import com.assessment.chat_rag_storage.dto.SessionResponseDto;
import com.assessment.chat_rag_storage.entity.Message;
import com.assessment.chat_rag_storage.entity.Session;
import com.assessment.chat_rag_storage.exception.SessionNotFoundException;
import com.assessment.chat_rag_storage.repository.MessageRepository;
import com.assessment.chat_rag_storage.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NotFound;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

	private final SessionRepository sessionRepository;

	public List<SessionResponseDto> getAllSessions() {
		List<Session> sessions = sessionRepository.findByDeletedAtIsNull();
		return sessions.stream().map(this::entityToDtoMapping).collect(Collectors.toList());
	}

	public SessionResponseDto createSession(SessionRequestDTO sessionRequestDTO) {
		String name = sessionRequestDTO.getSessionName();
		if (name == null || name.isBlank()) {
			name = "new chat";
		}
		Session session = Session.builder().sessionName(name).userId(sessionRequestDTO.getUserId()).build();
		Session savedSession = sessionRepository.save(session);
		return entityToDtoMapping(savedSession);
	}

	public SessionResponseDto updateFavorite(UUID sessionId, boolean favorite) {
		Optional<Session> optionalSession = sessionRepository.findByIdAndDeletedAtIsNull(sessionId);
		Session existingSession = optionalSession
			.orElseThrow(() -> new SessionNotFoundException("Session id is not present "));
		existingSession.setFavorite(favorite);
		Session updatedSession = sessionRepository.save(existingSession);
		log.info("Session {} favorite updated → {}", sessionId, favorite);
		return entityToDtoMapping(updatedSession);
	}

	public SessionResponseDto updateSessionName(UUID sessionId, String sessionName) {
		Optional<Session> optionalSession = sessionRepository.findByIdAndDeletedAtIsNull(sessionId);
		Session existingSession = optionalSession
			.orElseThrow(() -> new SessionNotFoundException("Session id is not present"));
		existingSession.setSessionName(sessionName);
		Session updatedSession = sessionRepository.save(existingSession);
		log.info("Session {} name updated → {} ", updatedSession.getId(), sessionName);
		return entityToDtoMapping(updatedSession);
	}

	@Override
	public void deleteSession(UUID sessionId) {
		Optional<Session> optionalSession = sessionRepository.findByIdAndDeletedAtIsNull(sessionId);
		Session existingSession = optionalSession
			.orElseThrow(() -> new SessionNotFoundException("Session id is not present"));
		existingSession.setDeletedAt(LocalDateTime.now());
		Session updatedSession = sessionRepository.save(existingSession);
		log.info("Soft deleted session with id: {}", updatedSession.getId());
	}

	private SessionResponseDto entityToDtoMapping(Session session) {
		return SessionResponseDto.builder()
			.id(session.getId())
			.userId(session.getUserId())
			.sessionName(session.getSessionName())
			.favorite(session.getFavorite())
			.build();
	}

}
