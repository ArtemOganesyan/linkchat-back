package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.model.User;
import com.practiceproject.linkchat_back.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsersController {

    private final CustomUserDetailsService userService;

    @Autowired
    public UsersController(CustomUserDetailsService userService) {
        this.userService = userService;
    }

    @GetMapping("/ui/usersmanagement")
    public String usersManagement(Model model) {
        model.addAttribute("users", userService.findAll());
        return "usersmanagement";
    }

    @PostMapping("/ui/usersmanagement/newuser")
    public String addUser(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/ui/usersmanagement";
    }

    @GetMapping("/ui/usersmanagement/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/ui/usersmanagement";
    }
}