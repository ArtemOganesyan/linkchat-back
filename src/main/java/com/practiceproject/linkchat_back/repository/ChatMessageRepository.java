package com.practiceproject.linkchat_back.repository;

import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

//    @Query("SELECT m FROM ChatMessage m WHERE m.chat.chatId = :chatId ORDER BY m.timestamp ASC")
    List<ChatMessage> getMessagesByChatId(@Param("chatId") Chat chatId);

    // Alternative method using native query if JPA query fails
    @Query(value = "SELECT * FROM messages WHERE chat_id = ?1 ORDER BY timestamp ASC", nativeQuery = true)
    List<ChatMessage> getMessagesByChatIdNative(Chat chatId);

    @Query("SELECT m FROM ChatMessage m ORDER BY m.timestamp ASC")
    List<ChatMessage> findAllByOrderByTimestampAsc();

    List<ChatMessage> findBySenderAndRecipientOrderByTimestampAsc(String sender, String recipient);

    List<ChatMessage> findByMessageType(ChatMessage.MessageType messageType);

    @Query("SELECT COUNT(m) FROM ChatMessage m")
    long count();
}
