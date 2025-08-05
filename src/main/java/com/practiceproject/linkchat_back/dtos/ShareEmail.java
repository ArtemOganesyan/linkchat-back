package com.practiceproject.linkchat_back.dtos;

public class ShareEmail {
    private Long chatId;
    private String email;

    public ShareEmail() {}

    public ShareEmail(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}