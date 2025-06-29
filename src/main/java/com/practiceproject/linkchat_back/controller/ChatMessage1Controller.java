package com.practiceproject.linkchat_back.controller;
import com.practiceproject.linkchat_back.model.ChatMessage1;
import com.practiceproject.linkchat_back.services.ChatMessage1Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ChatMessage1Controller {

    private final ChatMessage1Service service;

    public ChatMessage1Controller(ChatMessage1Service service) {
        this.service = service;
    }

    @GetMapping("/ui/chatMessages1")
    public String chatMessages1(Model model) {
        model.addAttribute("chatMessages", service.findAll());
        model.addAttribute("chatMessageForm", new ChatMessage1());
        return "chatMessages1";
    }

    @PostMapping("/ui/chatMessages1/add")
    public String addChatMessage(@ModelAttribute ChatMessage1 chatMessage) {
        service.save(chatMessage);
        return "redirect:/ui/chatMessages1";
    }

    @GetMapping("/ui/chatMessages1/delete/{id}")
    public String deleteChatMessage(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/ui/chatMessages1";
    }

    @PostMapping("/ui/chatMessages1/update")
    public String updateChatMessage(@ModelAttribute ChatMessage1 chatMessage) {
        service.save(chatMessage);
        return "redirect:/ui/chatMessages1";
    }


}
