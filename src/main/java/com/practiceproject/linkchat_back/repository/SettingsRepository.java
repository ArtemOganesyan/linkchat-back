package com.practiceproject.linkchat_back.repository;


import com.practiceproject.linkchat_back.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
    boolean existsBySettingName(String settingName);
}
