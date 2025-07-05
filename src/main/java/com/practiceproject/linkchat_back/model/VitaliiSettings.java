package com.practiceproject.linkchat_back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "vitalii_settings")
public class VitaliiSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "setting_name", nullable = false)
    private String settingName;

    @NotBlank
    @Column(name = "setting_value", nullable = false)
    private String settingValue;

    @NotBlank
    @Column(name = "setting_type", nullable = false)
    private String settingType;

    public VitaliiSettings() {}

    public VitaliiSettings(String settingName, String settingValue, String settingType) {
        this.settingName = settingName;
        this.settingValue = settingValue;
        this.settingType = settingType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    public String getSettingType() {
        return settingType;
    }

    public void setSettingType(String settingType) {
        this.settingType = settingType;
    }
}