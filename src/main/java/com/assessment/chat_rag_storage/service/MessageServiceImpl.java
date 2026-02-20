package com.assessment.chat_rag_storage.service;

import com.assessment.chat_rag_storage.dto.MessageRequestDto;
import com.assessment.chat_rag_storage.dto.MessageResponseDto;
import com.assessment.chat_rag_storage.entity.Message;
import com.assessment.chat_rag_storage.entity.Session;
import com.assessment.chat_rag_storage.exception.SessionNotFoundException;
import com.assessment.chat_rag_storage.repository.MessageRepository;
import com.assessment.chat_rag_storage.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

	private final MessageRepository messageRepository;

	private final SessionRepository sessionRepository;

	public MessageResponseDto addMessage(MessageRequestDto messageRequestDto) {
		Session session = getSessionOrThrow(messageRequestDto.getSessionId(), messageRequestDto.getContent());

		Message message = Message.builder()
			.sender(messageRequestDto.getSender())
			.content(messageRequestDto.getContent())
			.context(messageRequestDto.getContext())
			.session(session)
			.build();

		Message createdMessage = messageRepository.save(message);
		log.info("Message saved for session {}", session.getId());
		return EntityToMessageConverter(createdMessage);
	}

	private Session getSessionOrThrow(UUID sessionId, String messageContent) {
		Session session = sessionRepository.findByIdAndDeletedAtIsNull(sessionId)
			.orElseThrow(() -> new SessionNotFoundException("Session id is not present"));

		String name = session.getSessionName();

		if (name == null || name.isBlank() || name.equalsIgnoreCase("New Chat")) {
			String title = messageContent.length() > 20 ? messageContent.substring(0, 20) : messageContent;

			session.setSessionName(title);
			session = sessionRepository.save(session);
			log.info("name is updated for session {} as {}", session.getId(), title);
		}
		return session;
	}

	public Page<MessageResponseDto> getAllBySessionId(UUID sessionId, Pageable pageable) {
		Page<Message> messagePage = messageRepository.findBySessionIdAndSessionDeletedAtIsNull(sessionId, pageable);
		if (messagePage.isEmpty() && !sessionRepository.existsById(sessionId))
			throw new SessionNotFoundException("Message is empty or Session is not present");
		return messagePage.map(this::EntityToMessageConverter);
	}

	private MessageResponseDto EntityToMessageConverter(Message message) {
		return MessageResponseDto.builder()
			.id(message.getId())
			.sender(message.getSender())
			.content(message.getContent())
			.context(message.getContext())
			.sessionId(message.getSession().getId())
			.build();
	}

}
