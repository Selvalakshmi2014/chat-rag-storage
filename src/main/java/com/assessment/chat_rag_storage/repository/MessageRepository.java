package com.assessment.chat_rag_storage.repository;

import com.assessment.chat_rag_storage.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, Long> {

	@Query("select m from Message m where m.session.id = :session_id and m.session.deletedAt IS NULL")
	Page<Message> findBySessionIdAndSessionDeletedAtIsNull(UUID session_id, Pageable pageable);

}
