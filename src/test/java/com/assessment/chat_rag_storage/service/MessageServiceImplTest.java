package com.assessment.chat_rag_storage.service;

import com.assessment.chat_rag_storage.dto.MessageRequestDto;
import com.assessment.chat_rag_storage.entity.Message;
import com.assessment.chat_rag_storage.entity.Session;
import com.assessment.chat_rag_storage.enums.SenderType;
import com.assessment.chat_rag_storage.exception.SessionNotFoundException;
import com.assessment.chat_rag_storage.repository.MessageRepository;
import com.assessment.chat_rag_storage.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceImplTest {

	@Mock
	private MessageRepository messageRepository;

	@Mock
	private SessionRepository sessionRepository;

	@InjectMocks
	private MessageServiceImpl messageService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddMessage_createsSessionNameIfEmpty() {
		UUID sessionId = UUID.randomUUID();
		MessageRequestDto request = new MessageRequestDto();
		request.setSessionId(sessionId);
		request.setSender(SenderType.USER);
		request.setContent("Hello, this is a test message");

		Session session = Session.builder().id(sessionId).sessionName(null).build();

		when(sessionRepository.findByIdAndDeletedAtIsNull(sessionId)).thenReturn(Optional.of(session));
		when(sessionRepository.save(session)).thenReturn(session);

		Message savedMessage = Message.builder()
			.id(1L)
			.content(request.getContent())
			.sender(request.getSender())
			.session(session)
			.build();
		when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

		var response = messageService.addMessage(request);

		assertEquals("Hello, this is a test message", response.getContent());
		assertEquals(sessionId, response.getSessionId());
		assertNotNull(session.getSessionName());
		verify(messageRepository).save(any(Message.class));
	}

	@Test
	void testAddMessage_sessionNotFound() {
		UUID sessionId = UUID.randomUUID();
		MessageRequestDto request = new MessageRequestDto();
		request.setSessionId(sessionId);
		request.setContent("Hi");

		when(sessionRepository.findByIdAndDeletedAtIsNull(sessionId)).thenReturn(Optional.empty());

		assertThrows(SessionNotFoundException.class, () -> messageService.addMessage(request));
	}

}
