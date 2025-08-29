package com.practiceproject.linkchat_back.producerPayloads;

import com.practiceproject.linkchat_back.model.Chat;

public class ChatMessagePayload {
    private Long chatId;
    private String sender;
    private String recipient;
    private String messageType;
    private String messageText;
    private String timestamp;

    public ChatMessagePayload() {}

    public ChatMessagePayload(Long chatId, String sender, String recipient, String messageType, String messageText, String timestamp) {
        this.chatId = chatId;
        this.sender = sender;
        this.recipient = recipient;
        this.messageType = messageType;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
