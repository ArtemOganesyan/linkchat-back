package com.practiceproject.linkchat_back.repository;

import com.practiceproject.linkchat_back.model.User;
import com.practiceproject.linkchat_back.model.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    @Query("select cu.user from ChatUser cu where cu.chat.chatId = :chatId")
    List<User> getChatUsers(@Param("chatId") long chatId);

    List<ChatUser> findByChat_ChatId(Long chatId);
}