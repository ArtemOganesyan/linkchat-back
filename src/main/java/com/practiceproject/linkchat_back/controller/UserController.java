package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.dtos.UserEditDto;
import com.practiceproject.linkchat_back.model.User;
import com.practiceproject.linkchat_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import java.util.Collections;

/**
 * Controller for user management operations.
 * Handles displaying, editing, and deleting users via UI endpoints.
 * Uses Spring Boot, Java, and Thymeleaf for view rendering.
 *
 * Endpoints:
 * - /ui/user: List all users
 * - /ui/edit-user: Edit user form and update
 * - /ui/delete-user: Delete user
 *
 * Dependencies:
 * - UserRepository for data access
 * - UserEditDto for form binding
 */

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Displays the list of users.
     * Handles database access exceptions and provides error messages.
     *
     * @param model Model to pass attributes to the view
     * @return View name for rendering
     */

    @GetMapping("/ui/user")
    public String showUsers(Model model) {

        try {
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("userEditDto", new UserEditDto());
        } catch (DataAccessException e) {
            model.addAttribute("errorMessage", "Database is not available" + e.getMessage());
            model.addAttribute("users", Collections.emptyList());
        }
        return "UsersManagement";
    }

    /**
     * Displays the user edit form for a specific user.
     * Loads user data into the form for editing.
     *
     * @param id    User ID to edit
     * @param model Model to pass attributes to the view
     * @return View name for rendering
     */

    @GetMapping("/ui/edit-user")
    public String showEditUserForm(@RequestParam("id") Long id, Model model) {
        try {
            if (id == null) {
                model.addAttribute("errorMessage", "User ID is required.");
                return "edit-user";
            }
            userRepository.findById(id).ifPresentOrElse(user -> {
                UserEditDto dto = new UserEditDto();
                dto.setId(user.getId());
                dto.setName(user.getName());
                dto.setEmail(user.getEmail());
                dto.setRole(user.getRole());
                dto.setActive(user.isActive());
                model.addAttribute("userEditDto", dto);
            }, () -> model.addAttribute("errorMessage", "User not found."));
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Error loading user: " + e.getMessage());
        } catch (DataAccessException ex) {
            model.addAttribute("errorMessage", "Database error.");
        }
        return "edit-user";
    }

    /**
     * Handles the form submission for editing a user.
     * Validates input and updates the user in the database.
     *
     * @param userEditDto DTO containing user data
     * @param br          BindingResult for validation errors
     * @param model       Model to pass attributes to the view
     * @return View name for rendering
     */
    @PostMapping("/ui/edit-user")
    public String editUser(@ModelAttribute("userEditDto") UserEditDto userEditDto,
                           BindingResult br, Model model) {
        try {
            if (br.hasErrors()) {
                model.addAttribute("userEditDto", userEditDto);
                return "edit-user";
            }
            if (userEditDto == null ||
                    userEditDto.getId() == null ||
                    userEditDto.getName() == null || userEditDto.getName().isBlank() ||
                    userEditDto.getEmail() == null || userEditDto.getEmail().isBlank()) {
                model.addAttribute("errorMessage", "Name and Email are required.");
                model.addAttribute("userEditDto", userEditDto);
                return "edit-user";
            }
            userRepository.findById(userEditDto.getId()).ifPresentOrElse(user -> {
                user.setName(userEditDto.getName());
                user.setEmail(userEditDto.getEmail());
                user.setRole(userEditDto.getRole());
                user.setActive(userEditDto.isActive());
                userRepository.save(user);
            }, () -> {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            });
            model.addAttribute("userEditDto", userEditDto);
            model.addAttribute("successMessage", "User updated successfully!");
            model.addAttribute("redirectAfter", "/ui/user");
            return "edit-user";
        } catch (ResponseStatusException ex) {
            model.addAttribute("errorMessage", "User not found");
            return "edit-user";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating user: " + e.getMessage());
            return "edit-user";
        }
    }

    /**
     * Handles user deletion.
     * Deletes a user by ID and returns a success message.
     *
     * @param id    User ID to delete
     * @param model Model to pass attributes to the view
     * @return View name for rendering
     */
    @PostMapping("/ui/delete-user")
    public String deleteUser(@RequestParam Long id, Model model) {
        try {
            userRepository.deleteById(id);
            model.addAttribute("successMessage", "User was deleted.");
        } catch (EmptyResultDataAccessException ex) {
            model.addAttribute("errorMessage", "User not found.");
        } catch (DataAccessException ex) {
            model.addAttribute("errorMessage", "Error deleting user");
        }
        model.addAttribute("userEditDto", new UserEditDto());
        model.addAttribute("redirectAfter", "/ui/user");
        return "edit-user";
    }

    /**
     * Displays the add user form.
     * Prepares an empty UserEditDto for the form.
     *
     * @param model Model to pass attributes to the view
     * @return View name for rendering
     */
    @GetMapping("/ui/add-user")
    public String showAddUserForm(Model model) {
        model.addAttribute("userEditDto", new UserEditDto());
        return "edit-user";
    }

    /**
     * Handles the form submission for adding a new user.
     * Validates input and saves the new user to the database.
     *
     * @param userEditDto DTO containing user data
     * @param br BindingResult for validation errors
     * @param model Model to pass attributes to the view
     * @return View name for rendering
     */
    @PostMapping("/ui/add-user")
    public String addUser(@ModelAttribute("userEditDto") UserEditDto userEditDto,
                          BindingResult br, Model model) {
        try {
            if (br.hasErrors()) {
                model.addAttribute("userEditDto", userEditDto);
                return "edit-user";
            }
            if (userEditDto.getName() == null || userEditDto.getName().isBlank() ||
                    userEditDto.getEmail() == null || userEditDto.getEmail().isBlank()) {
                model.addAttribute("errorMessage", "Name and Email are required.");
                model.addAttribute("userEditDto", userEditDto);
                return "edit-user";
            }
            User user = new User();
            user.setName(userEditDto.getName());
            user.setEmail(userEditDto.getEmail());
            user.setRole(userEditDto.getRole());
            user.setActive(userEditDto.isActive());
            userRepository.save(user);

            model.addAttribute("successMessage", "User added successfully!");
            model.addAttribute("redirectAfter", "/ui/user");
            return "edit-user";
        } catch (DataAccessException ex) {
            model.addAttribute("errorMessage", "Database error: " + ex.getMessage());
            return "edit-user";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error adding user: " + e.getMessage());
            return "edit-user";
        }
    }
}