package com.practiceproject.linkchat_back.adminLoginPageController;

import com.practiceproject.linkchat_back.model.Settings;
import com.practiceproject.linkchat_back.repository.SettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GegamSettingsController {
    private static final Logger logger = LoggerFactory.getLogger(GegamSettingsController.class);

    private final SettingsRepository settingsRepository;

    public GegamSettingsController(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @GetMapping("/ui/gegam-settings")
    public String showSettingsPage(@RequestParam(value = "id", required = false) Long id, Model model) {
        if (id != null) {
            Settings setting = settingsRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid setting ID: " + id));
            model.addAttribute("setting", setting);
            model.addAttribute("editing", true);
        } else {
            model.addAttribute("setting", new Settings());
            model.addAttribute("editing", false);
        }
        return "gegam-settings";
    }

    @PostMapping("/ui/gegam-settings")
    public String saveSetting(
            @RequestParam("setting_name") String name,
            @RequestParam("setting_type") String type,
            @RequestParam("setting_value") String value,
            Model model
    ) {
        if (!settingsRepository.existsBySettingName(name)) {
            Settings setting = new Settings();
            setting.setSettingName(name);
            setting.setSettingType(type);
            setting.setSettingValue(value);
            settingsRepository.save(setting);
            model.addAttribute("message", "Setting saved successfully!");
        } else {
            model.addAttribute("error", "Setting with that name already exists.");
        }
        return "gegam-settings";
    }

    @GetMapping("/ui/view-settings")
    public String viewSettings(Model model) {
        model.addAttribute("settingsList", settingsRepository.findAll());
        return "view-settings";
    }

    @PostMapping("/ui/update-setting")
    public String updateSetting(
            @RequestParam("id") Long id,
            @RequestParam("setting_name") String name,
            @RequestParam("setting_type") String type,
            @RequestParam("setting_value") String value
    ) {
        Settings setting = settingsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid setting ID: " + id));

        setting.setSettingName(name);
        setting.setSettingType(type);
        setting.setSettingValue(value);
        settingsRepository.save(setting);

        return "redirect:/ui/view-settings";
    }

    @GetMapping("/ui/delete-setting/{id}")
    public String deleteSetting(@PathVariable Long id) {
        settingsRepository.deleteById(id);
        return "redirect:/ui/view-settings";
    }
}
