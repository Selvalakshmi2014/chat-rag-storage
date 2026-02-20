package com.assessment.chat_rag_storage.service;

import com.assessment.chat_rag_storage.dto.SessionRequestDTO;
import com.assessment.chat_rag_storage.entity.Session;
import com.assessment.chat_rag_storage.exception.SessionNotFoundException;
import com.assessment.chat_rag_storage.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionServiceImplTest {

	@Mock
	private SessionRepository sessionRepository;

	@InjectMocks
	private SessionServiceImpl sessionService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateSession_withName() {
		SessionRequestDTO request = new SessionRequestDTO();
		request.setSessionName("My Chat");
		request.setUserId("user123");

		Session savedSession = Session.builder().id(UUID.randomUUID()).userId("user123").sessionName("My Chat").build();

		when(sessionRepository.save(any(Session.class))).thenReturn(savedSession);

		var response = sessionService.createSession(request);

		assertEquals("My Chat", response.getSessionName());
		assertEquals("user123", response.getUserId());
		verify(sessionRepository, times(1)).save(any(Session.class));
	}

	@Test
	void testUpdateFavorite_success() {
		UUID sessionId = UUID.randomUUID();
		Session session = Session.builder().id(sessionId).favorite(false).build();

		when(sessionRepository.findByIdAndDeletedAtIsNull(sessionId)).thenReturn(Optional.of(session));
		when(sessionRepository.save(session)).thenReturn(session);

		var response = sessionService.updateFavorite(sessionId, true);

		assertTrue(response.isFavorite());
		verify(sessionRepository).save(session);
	}

	@Test
	void testUpdateFavorite_sessionNotFound() {
		UUID sessionId = UUID.randomUUID();
		when(sessionRepository.findByIdAndDeletedAtIsNull(sessionId)).thenReturn(Optional.empty());

		assertThrows(SessionNotFoundException.class, () -> sessionService.updateFavorite(sessionId, true));
	}

	@Test
	void testDeleteSession_success() {
		UUID sessionId = UUID.randomUUID();
		Session session = Session.builder().id(sessionId).build();
		when(sessionRepository.findByIdAndDeletedAtIsNull(sessionId)).thenReturn(Optional.of(session));
		when(sessionRepository.save(session)).thenReturn(session);

		sessionService.deleteSession(sessionId);

		assertNotNull(session.getDeletedAt());
		verify(sessionRepository).save(session);
	}

}