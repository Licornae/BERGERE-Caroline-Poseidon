package controllerTests;

import com.nnk.springboot.Application;
import com.nnk.springboot.controllers.UserController;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = Application.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    //TESTS LIST
    @Test
    @WithMockUser(username = "testuser")
    public void list_shouldReturnUserListView() throws Exception {

        List<User> mockUsers = Arrays.asList(
                new User(1, "user1", "pass", "User One", "USER"),
                new User(2, "user2", "pass", "User Two", "ADMIN")
        );

        Mockito.when(userService.findAll()).thenReturn(mockUsers);

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", mockUsers));
    }

    //TESTS VALIDATION
    @Test
    @WithMockUser(username = "testuser")
    public void validate_withValidData_shouldSaveAndRedirect() throws Exception {

        User user = new User(1, "user1", "password", "User One", "USER");

        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(post("/user/validate")
                        .param("username", "user1")
                        .param("password", "password")
                        .param("fullname", "User One")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void validate_withInvalidData_shouldReturnAddView() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .param("username", "")
                        .param("password", "password")
                        .param("fullname", "User One")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeHasFieldErrors("user", "username"));
    }

    //TESTS UPDATE
    @Test
    @WithMockUser(username = "testuser")
    public void showUpdateForm_shouldReturnUpdateViewWithUser() throws Exception {

        User mockUser = new User(1, "user1", "pass", "User One", "USER");

        Mockito.when(userService.findById(1)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", mockUser));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void showUpdateForm_userNotFound_shouldRedirect() throws Exception {

        Mockito.when(userService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void updateUser_withValidData_shouldUpdateAndRedirect() throws Exception {

        User updatedUser = new User(1, "updated", "password", "Updated User", "ADMIN");

        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(post("/user/update/1")
                        .param("username", "updated")
                        .param("password", "password")
                        .param("fullname", "Updated User")
                        .param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void updateUser_withInvalidData_shouldReturnUpdateView() throws Exception {

        mockMvc.perform(post("/user/update/1")
                        .param("username", "") // invalide
                        .param("password", "password")
                        .param("fullname", "Updated User")
                        .param("role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeHasFieldErrors("user", "username"));
    }

    //TESTS DELETE
    @Test
    @WithMockUser(username = "testuser")
    public void deleteUser_shouldDeleteAndRedirect() throws Exception {

        Mockito.doNothing().when(userService).deleteById(1);

        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        Mockito.verify(userService, Mockito.times(1)).deleteById(1);
    }

    @Test
    @WithMockUser(username = "testuser")
    public void deleteUser_shouldHandleExceptionAndRedirect() throws Exception {

        Mockito.doThrow(new RuntimeException()).when(userService).deleteById(1);

        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }
}