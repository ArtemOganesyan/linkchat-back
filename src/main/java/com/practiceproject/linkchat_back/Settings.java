package com.practiceproject.linkchat_back;

import jakarta.persistence.*;

@Entity
@Table(name = "settings")
public class Settings {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Column(name = "settingname", nullable = false, unique = true)
 private String settingName;

 @Column(name = "settingvalue", nullable = false)
 private String settingValue;

 // Constructors
 public Settings() {
 }

 public Settings(String settingName, String settingValue) {
  this.settingName = settingName;
  this.settingValue = settingValue;
 }

 // Getters and Setters
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

}
