package com.practiceproject.linkchat_back.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_messages")
public class ChatMessage1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long message_id;

    @Column(name = "recipient", nullable = false)
    private String recipient;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "timestamp", nullable = false)
    private String timestamp;

    public Long getMessage_id() {
        return message_id;
    }
    public void setMessage_id(Long message_id) {
        this.message_id = message_id;
    }
    public String getRecipient() {
        return recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
