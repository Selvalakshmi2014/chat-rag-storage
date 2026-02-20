package com.assessment.chat_rag_storage;

import com.assessment.chat_rag_storage.filter.ApiKeyAuthFilter;
import com.assessment.chat_rag_storage.filter.RateLimitFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active=test")
class ChatRagStorageApplicationTests {

	@Test
	void contextLoads() {
	}

}
