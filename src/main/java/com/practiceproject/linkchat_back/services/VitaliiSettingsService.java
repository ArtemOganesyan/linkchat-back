package com.practiceproject.linkchat_back.services;

import com.practiceproject.linkchat_back.model.VitaliiSettings;
import com.practiceproject.linkchat_back.repository.VitaliiSettingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VitaliiSettingsService {

    private final VitaliiSettingRepository repository;

    public VitaliiSettingsService(VitaliiSettingRepository repository) {
        this.repository = repository;
    }

    public List<VitaliiSettings> findAll() {
        return repository.findAll();
    }

    public Optional<VitaliiSettings> findById(Long id) {
        return repository.findById(id);
    }

    public VitaliiSettings save(VitaliiSettings setting) {
        return repository.save(setting);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}