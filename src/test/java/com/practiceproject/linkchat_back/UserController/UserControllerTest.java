package com.practiceproject.linkchat_back.controller;

import com.practiceproject.linkchat_back.controller.UserController;
import com.practiceproject.linkchat_back.dtos.UserEditDto;
import com.practiceproject.linkchat_back.model.User;
import com.practiceproject.linkchat_back.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Test for a UserController and editUser for:
//      updating users,
//      handling errors,
//      and validation.
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void editUser_ShouldUpdateNameEmailRoleActive() {
        // Arrange
        UserEditDto dto = new UserEditDto();
        dto.setId(1L);
        dto.setName("New Name");
        dto.setEmail("new@email.com");
        dto.setRole("ADMIN");
        dto.setActive(true);

        User user = new User();
        user.setId(1L);
        user.setName("Old Name");
        user.setEmail("old@email.com");
        user.setRole("USER");
        user.setActive(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(false);
        Model model = new ConcurrentModel();

        // Act
        String view = controller.editUser(dto, br, model);

        // Assert
        assertEquals("edit-user", view);
        assertEquals("User updated successfully!", model.getAttribute("successMessage"));
        verify(userRepository).save(argThat(u ->
                u.getName().equals("New Name") &&
                        u.getEmail().equals("new@email.com") &&
                        u.getRole().equals("ADMIN") &&
                        u.isActive()
        ));
    }

    @Test
    void editUser_ShouldHandleNonExistentId() {
        // Arrange
        UserEditDto dto = new UserEditDto();
        dto.setId(99L);
        dto.setName("Name");
        dto.setEmail("email@email.com");
        dto.setRole("USER");
        dto.setActive(true);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(false);
        Model model = new ConcurrentModel();

        // Act
        String view = controller.editUser(dto, br, model);

        // Assert
        assertEquals("edit-user", view);
        assertEquals("User not found", model.getAttribute("errorMessage"));
    }

    @Test
    void editUser_ShouldReturnFormView_WhenValidationErrors() {
        // Arrange
        UserEditDto dto = new UserEditDto();
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(true);
        Model model = new ConcurrentModel();

        // Act
        String view = controller.editUser(dto, br, model);

        // Assert
        assertEquals("edit-user", view);
        assertEquals(dto, model.getAttribute("userEditDto"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void editUser_ShouldHandleInactiveStatus() {
        // Arrange
        UserEditDto dto = new UserEditDto();
        dto.setId(2L);
        dto.setName("Inactive User");
        dto.setEmail("inactive@email.com");
        dto.setRole("USER");
        dto.setActive(false);

        User user = new User();
        user.setId(2L);
        user.setName("Active User");
        user.setEmail("active@email.com");
        user.setRole("USER");
        user.setActive(true);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(false);
        Model model = new ConcurrentModel();

        // Act
        String view = controller.editUser(dto, br, model);

        // Assert
        assertEquals("edit-user", view);
        verify(userRepository).save(argThat(u -> !u.isActive()));
    }
}