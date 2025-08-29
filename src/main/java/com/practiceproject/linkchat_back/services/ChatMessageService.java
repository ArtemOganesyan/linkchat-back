package com.practiceproject.linkchat_back.services;

import com.practiceproject.linkchat_back.dtos.ChatMessageResponse;
import com.practiceproject.linkchat_back.dtos.ImageMessageRequest;
import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.model.ChatMessage;
import com.practiceproject.linkchat_back.producer.ChatMessageProducer;
import com.practiceproject.linkchat_back.producerPayloads.ChatMessagePayload;
import com.practiceproject.linkchat_back.repository.ChatMessageRepository;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageProducer messageProducer;

    public void sendMessage(ChatMessagePayload payload) throws Exception {
        Chat chat = chatRepository.findById(payload.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat not found with id: " + payload.getChatId()));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChat(chat);
        chatMessage.setSender(payload.getSender());
        chatMessage.setRecipient(payload.getRecipient());
        chatMessage.setMessageType(ChatMessage.MessageType.valueOf(payload.getMessageType()));
        chatMessage.setMessageText(payload.getMessageText());
        chatMessage.setTimestamp(LocalDateTime.now().toString());

        chatMessageRepository.save(chatMessage);
    }

    public ChatMessage sendImageMessage(ImageMessageRequest request) {
        ChatMessage message = new ChatMessage();
        message.setSender(request.getSender());
        message.setRecipient(request.getRecipient());
        message.setMessageType(ChatMessage.MessageType.IMAGE);
        message.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // Store the entire base64 string including data URL prefix as text
        message.setImageData(request.getImageBase64());
        message.setImageFilename(request.getFilename());
        message.setImageContentType(request.getContentType());

        if (request.getChatId() != null) {
            Chat chat = chatRepository.findById(request.getChatId()).orElse(null);
            message.setChat(chat);
        }

        return chatMessageRepository.save(message);
    }

    public List<ChatMessageResponse> getMessagesByChatId(Chat chatId) {
        List<ChatMessage> messages = chatMessageRepository.getMessagesByChatId(chatId);
        return messages.stream()
                .map(ChatMessageResponse::new)
                .collect(Collectors.toList());
    }

    public List<ChatMessageResponse> getAllMessages() {
        List<ChatMessage> messages = chatMessageRepository.findAllByOrderByTimestampAsc();
        return messages.stream()
                .map(ChatMessageResponse::new)
                .collect(Collectors.toList());
    }

    public ChatMessage findById(Long id) {
        return chatMessageRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        chatMessageRepository.deleteById(id);
    }

    public long countMessages() {
        return chatMessageRepository.count();
    }
}
