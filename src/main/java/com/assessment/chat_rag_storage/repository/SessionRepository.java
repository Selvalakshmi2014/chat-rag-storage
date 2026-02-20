package com.assessment.chat_rag_storage.repository;

import com.assessment.chat_rag_storage.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

	Optional<Session> findByIdAndDeletedAtIsNull(UUID session_id);

	List<Session> findByDeletedAtIsNull();

}
