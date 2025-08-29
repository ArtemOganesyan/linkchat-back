package com.practiceproject.linkchat_back.model;
import com.practiceproject.linkchat_back.dtos.ChatMessageResponse;
import com.practiceproject.linkchat_back.repository.ChatMessageRepository;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.repository.ChatUserRepository;
import com.practiceproject.linkchat_back.model.Chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChatInfo {
    private String title;
    private String link;
    private List<String> users;
    private List<ChatMessageResponse> messages;

    public ChatInfo(String title, String link) {
        this.title = title;
        this.link = link;
        this.users = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public ChatInfo(String link,
                    ChatRepository chatRepository,
                    ChatUserRepository chatUserRepository,
                    ChatMessageRepository messageRepository) {
            try {
                Chat chatId = chatRepository.findByLink(link).orElse(null);
                if (chatId != null) {
                    this.title = chatId.getTitle();
                    this.link = chatId.getLink();

                    if (chatId.getUsers() != null && !chatId.getUsers().isEmpty()) {
                        this.users = Arrays.asList(chatId.getUsers().split("\\s*,\\s*"));
                    } else {
                        this.users = new ArrayList<>();
                    }

                    try {
                        this.messages = messageRepository.getMessagesByChatId(chatId)
                                .stream()
                                .map(ChatMessageResponse::new)
                                .collect(Collectors.toList());
                    } catch (Exception e) {
                        // Fallback to empty list if there's an issue
                        System.err.println("Error fetching messages for chat " + chatId + ": " + e.getMessage());
                        this.messages = new ArrayList<>();
                    }
                } else {
                    this.title = null;
                    this.link = null;
                    this.users = new ArrayList<>();
                    this.messages = new ArrayList<>();
                }
            } catch (Exception e) {
                System.err.println("Error in ChatInfo constructor: " + e.getMessage());
                this.title = null;
                this.link = null;
                this.users = new ArrayList<>();
                this.messages = new ArrayList<>();
            }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<ChatMessageResponse> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessageResponse> messages) {
        this.messages = messages;
    }
}
