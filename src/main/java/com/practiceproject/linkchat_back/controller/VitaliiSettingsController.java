package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.model.VitaliiSettings;
import com.practiceproject.linkchat_back.services.VitaliiSettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class VitaliiSettingsController {

    private final VitaliiSettingsService service;

    public VitaliiSettingsController(VitaliiSettingsService service) {
        this.service = service;
    }

    @GetMapping("/ui/vitalii-settings")
    public String listSettings(Model model) {
        model.addAttribute("settings", service.findAll());
        model.addAttribute("settingForm", new VitaliiSettings());
        return "vitalii-settings";
    }

    @PostMapping("/ui/vitalii-settings/add")
    public String addSetting(@ModelAttribute VitaliiSettings setting) {
        service.save(setting);
        return "redirect:/ui/vitalii-settings";
    }

    @GetMapping("/ui/vitalii-settings/delete/{id}")
    public String deleteSetting(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/ui/vitalii-settings";
    }

    @PostMapping("/ui/vitalii-settings/update")
    public String updateSetting(@ModelAttribute VitaliiSettings setting) {
        service.save(setting);
        return "redirect:/ui/vitalii-settings";
    }
}
