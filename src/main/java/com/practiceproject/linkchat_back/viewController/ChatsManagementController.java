package com.practiceproject.linkchat_back.viewController;

import com.practiceproject.linkchat_back.dtos.ChatSettingsDto;
import com.practiceproject.linkchat_back.dtos.SimpleEmailRequest;
import com.practiceproject.linkchat_back.dtos.ShareEmail;
import com.practiceproject.linkchat_back.model.Chat;
import com.practiceproject.linkchat_back.model.ChatUser;
import com.practiceproject.linkchat_back.repository.ChatRepository;
import com.practiceproject.linkchat_back.repository.ChatUserRepository;
import com.practiceproject.linkchat_back.services.ChatService;
import com.practiceproject.linkchat_back.services.EmailService;
import com.practiceproject.linkchat_back.viewModels.ChatForm;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/ui")
public class ChatsManagementController {
    private static final Logger logger = LoggerFactory.getLogger(ChatsManagementController.class);

    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final EmailService emailService;
    private final ChatUserRepository chatUserRepository;

    public ChatsManagementController(ChatRepository chatRepository,
                                     ChatService chatService,
                                     EmailService emailService,
                                     ChatUserRepository chatUserRepository) {
        this.chatRepository = chatRepository;
        this.chatService = chatService;
        this.emailService = emailService;
        this.chatUserRepository = chatUserRepository;
    }

    @GetMapping("/chats-management")
    public String showChatsManagement(Model model) {
        try {
            List<Chat> chats = chatRepository.findAll();
            model.addAttribute("chats", chats);
            model.addAttribute("chatSettingsDto", new ChatSettingsDto());
            return "chats-management";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "A system error occurred. Please try again later or contact support.");
            return "maintenance";
        }
    }

    @GetMapping("/new-chat")
    public String showChatForm(Model model) {
        if (!model.containsAttribute("chat")) {
            model.addAttribute("chat", chatService.createDefaultChatForm());
        }
        return "new-chat";
    }

    @PostMapping(value = "/new-chat", params = "generate")
    public String generateLink(@ModelAttribute("chat") ChatForm form, RedirectAttributes redirectAttributes) {
        chatService.generateRandomLink(form);
        redirectAttributes.addFlashAttribute("chat", form);
        logger.info("::Generated new chat link: {}", form.getLink());
        return "redirect:/ui/new-chat";
    }

    @PostMapping(value = "/new-chat", params = "save")
    public String saveNewChat(@Valid @ModelAttribute("chat") ChatForm form, BindingResult result) {
        if (result.hasErrors()) {
            logger.warn("::Validation errors: {}", result.getAllErrors());
            return "new-chat";
        }
        chatService.addInviteEmail(form, null);
        logger.info("::Adding invite emails: {}", form.getInviteEmails());
        chatService.saveChat(form);
        logger.info("::New chat saved with title: {}", form.getTitle());
        return "redirect:/ui/chats-management";
    }

    @PostMapping(value = "/new-chat", params = "save-n-send")
    public String saveAndSendNewChat(
            @Valid @ModelAttribute("chat") ChatForm form,
            BindingResult result,
            @ModelAttribute("emailRequest") SimpleEmailRequest emailRequest
    ) throws MessagingException, IOException {
        if (result.hasErrors()) {
            logger.warn("::Validation error: {}", result.getAllErrors());
            return "new-chat";
        }
        chatService.saveChat(form);
        emailService.sendInviteEmail(emailRequest, form);
        logger.info("::New chat saved and invite email sent to: {}", emailRequest.getTo());
        return "redirect:/ui/chats-management";
    }

    @GetMapping("/chat-settings")
    public String showChatSettings(@RequestParam("id") Long chatId, Model model) {
        try {
            Chat chat = chatRepository.findById(chatId)
                    .orElseThrow(() -> new IllegalArgumentException("Chat not found with ID: " + chatId));

            ChatSettingsDto dto = new ChatSettingsDto();
            dto.setId(chat.getChatId());
            dto.setTitle(chat.getTitle());
            dto.setLink(chat.getLink());
            dto.setType(chat.getType());
            dto.setActive(chat.isActive());

            List<ChatUser> chatUsers = chatUserRepository.findByChat_ChatId(chatId);
            model.addAttribute("chatUsers", chatUsers);

            ShareEmail shareEmail = new ShareEmail();
            shareEmail.setChatId(chatId);
            model.addAttribute("shareEmail", shareEmail);

            model.addAttribute("chatSettingsDto", dto);
            return "chat-settings";
        } catch (Exception ex) {
            logger.error("::Failed to load chat settings", ex);
            model.addAttribute("errorMessage", "A system error occurred. Please try again later or contact support.");
            return "maintenance";
        }
    }


    @PostMapping("/chat-settings/save")
    public String editChatSettings(@Valid @ModelAttribute("chatSettingsDto") ChatSettingsDto chatSettingsDto,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            logger.warn("::Validation errors in chat settings: {}", result.getAllErrors());
            return "chat-settings";
        }
        try {
            chatService.updateChatSettings(chatSettingsDto);
            logger.info("::Chat settings updated for Chat ID: {}", chatSettingsDto.getId());
            return "redirect:/ui/chats-management";
        } catch (Exception ex) {
            logger.error("::Failed to update chat settings", ex);
            model.addAttribute("errorMessage", "A system error occurred while saving settings. Please try again.");
            return "chat-settings";
        }
    }
    @PostMapping("/chat-settings/share")
    public String shareChat(
            @ModelAttribute("shareEmail") ShareEmail shareEmail,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Example: send invite to each email (split by comma)
            String[] emails = shareEmail.getEmail().split(",");
            for (String email : emails) {
                emailService.sendInvite(
                        email.trim(),
                        "User", // You can use the current user's name if available
                        "https://fs-dev.portnov.com/chat/" + shareEmail.getChatId(),
                        24 // TTL hours
                );
            }
            redirectAttributes.addFlashAttribute("notification", "Invites sent!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("notification", "Failed to send invites: " + ex.getMessage());
        }
        // Redirect back to settings page
        return "redirect:/ui/chat-settings?id=" + shareEmail.getChatId();
    }
    @PostMapping("/chat-settings/delete")
    public String deleteChat(@RequestParam("id") Long chatId, Model model) {
        try {
            chatService.deleteChatById(chatId);
            logger.info("::Chat deleted with ID: {}", chatId);
            return "redirect:/ui/chats-management";
        } catch (Exception ex) {
            logger.error("::Failed to delete chat with ID: {}", chatId, ex);
            model.addAttribute("errorMessage", "A system error occurred while deleting the chat.");
            return "chat-settings";
        }
    }
}