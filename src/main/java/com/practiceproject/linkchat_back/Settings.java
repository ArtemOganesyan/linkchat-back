package com.practiceproject.linkchat_back.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "chat")
public class Settings {
 private String setting;
 private String value;

 public String getSetting() {
  return setting;
 }

 public void setSetting(String setting) {
  this.setting = setting;
 }

 public String getValue() {
  return value;
 }

 public void setValue(String value) {
  this.value = value;
 }
}