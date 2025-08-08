package com.practiceproject.linkchat_back;

import com.practiceproject.linkchat_back.controller.ChatController;
import com.practiceproject.linkchat_back.dtos.ChatSettingsDtoAPI;
import com.practiceproject.linkchat_back.model.ChatSetting;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.repository.ChatSettingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.MapBindingResult;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChatControllerTests {

    private ChatRepository chatRepository;
    private ChatSettingRepository chatSettingRepository;
    private ChatController controller;


    @BeforeEach
    void setUp() {
        chatRepository = mock(ChatRepository.class);
        chatSettingRepository = mock(ChatSettingRepository.class);
        controller = new ChatController(chatRepository, null, null, chatSettingRepository);
    }

    // Test if controller returns 400 BAD_REQUEST when chatId is invalid (e.g., 0 or negative)
    @Test
    void getSettings_invalidId_returnsBadRequest() {
        ResponseEntity<?> response = controller.getSettings(0L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("chatId must be a positive number", response.getBody());
    }

    // Test if controller returns 404 NOT_FOUND when chat with given ID does not exist
    @Test
    void getSettings_chatNotFound_returnsNotFound() {
        when(chatRepository.existsById(10L)).thenReturn(false);
        ResponseEntity<?> response = controller.getSettings(10L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Chat id 10 not found", response.getBody());
    }

    // Test if controller returns 200 OK and correct DTO when settings exist for a valid chatId
    @Test
    void getSettings_withSettings_returnsDto() {
        when(chatRepository.existsById(3L)).thenReturn(true);

        ChatSetting setting = new ChatSetting();
        setting.setSettingKey("lang");
        setting.setSettingValue("en");

        when(chatSettingRepository.findByChatId(3L)).thenReturn(List.of(setting));

        ResponseEntity<?> response = controller.getSettings(3L);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ChatSettingsDtoAPI dto = assertInstanceOf(ChatSettingsDtoAPI.class, response.getBody());

        assertEquals(1, dto.getSettings().size());
        assertEquals("lang", dto.getSettings().get(0).getSettingKey());
        assertEquals("en", dto.getSettings().get(0).getSettingValue());
    }

    // Test if controller returns 200 OK and message when repository returns an empty list (no settings)
    @Test
    void getSettings_repositoryReturnsNull_returnsMessage() {
        when(chatRepository.existsById(99L)).thenReturn(true);
        when(chatSettingRepository.findByChatId(99L)).thenReturn(Collections.emptyList()); // repository misbehaving

        ResponseEntity<?> response = controller.getSettings(99L);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Response body should still be "No settings for this chat"
        assertInstanceOf(ChatController.ApiResponse.class, response.getBody());
        ChatController.ApiResponse body = (ChatController.ApiResponse) response.getBody();
        assertEquals("No settings for this chat", body.message());
    }

    // Test if controller returns 400 BAD_REQUEST when trying to upsert empty settings list
    @Test
    void upsertSettings_emptySettings_returnsBadRequest() {
        Long chatId = 1L;
        ChatSettingsDtoAPI dto = new ChatSettingsDtoAPI(Collections.emptyList());

        ResponseEntity<?> response = controller.upsertSettings(chatId, dto, new MapBindingResult(Collections.emptyMap(), "dto"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid payload", response.getBody());
    }

}
