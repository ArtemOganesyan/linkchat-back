package com.practiceproject.linkchat_back.services;

import com.practiceproject.linkchat_back.model.ChatMessage1;
import com.practiceproject.linkchat_back.repository.ChatMessage1Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatMessage1Service {
    private final ChatMessage1Repository repository;

    public ChatMessage1Service(ChatMessage1Repository repository) {
        this.repository = repository;
    }

    public List<ChatMessage1> findAll() {
        return repository.findAll();
    }

    public void save(ChatMessage1 chatMessage) {
        repository.save(chatMessage);
    }

    public Optional<ChatMessage1> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
