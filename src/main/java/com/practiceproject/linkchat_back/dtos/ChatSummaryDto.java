package com.practiceproject.linkchat_back.dtos;

import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.model.InviteEmailEntry;
import java.util.List;

public class ChatSummaryDto {
    private Long chatId;
    private String title;
    private String type;
    private boolean active;
    private String link;
    private List<String> inviteEmails;

    public ChatSummaryDto() {}

    public ChatSummaryDto(Chat chat) {
        this.chatId = chat.getChatId();
        this.title = chat.getTitle();
        this.type = chat.getType();
        this.active = chat.isActive();
        this.link = chat.getLink();
        this.inviteEmails = chat.getInviteEmails()
                                .stream()
                                .map(InviteEmailEntry::getEmail) // Convert InviteEmailEntry to String
                                .toList();
    }

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public List<String> getInviteEmails() { return inviteEmails; }
    public void setInviteEmails(List<String> inviteEmails) { this.inviteEmails = inviteEmails; }
}