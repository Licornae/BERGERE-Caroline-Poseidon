package unitTests;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnitUserTests {

    @Mock
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void findAll_shouldReturnAllUsers() {

        User user1 = new User();
        user1.setId(1);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");

        List<User> userList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("user1");
        assertThat(result.get(1).getUsername()).isEqualTo("user2");
    }

    @Test
    public void findById_shouldReturnCorrespondingUser() {

        User user = new User();
        user.setId(1);
        user.setUsername("user1");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("user1");
    }

    @Test
    public void findById_shouldReturnExceptionWhenUserDoesNotExist() {

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found for id: 999");
    }

    @Test
    public void save_shouldHashPasswordBeforeSavingUser() {

        User user = new User();
        user.setUsername("user");
        user.setPassword("Password1!");
        user.setFullname("New User");
        user.setRole("USER");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.save(user);

        assertThat(savedUser.getPassword()).isNotEqualTo("Password1!");
        assertThat(passwordEncoder.matches("Password1!", savedUser.getPassword())).isTrue();

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void save_shouldReturnSavedUser() {

        User userToSave = new User();
        userToSave.setUsername("newUser");
        userToSave.setPassword("Password1!");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setUsername("newUser");
        savedUser.setPassword("Password1!");

        when(userRepository.save(userToSave)).thenReturn(savedUser);

        User actualUser = userService.save(userToSave);

        assertThat(actualUser.getId()).isNotNull();
        assertThat(actualUser.getUsername()).isEqualTo(savedUser.getUsername());
    }

    @Test
    public void update_shouldHashNewPassword_whenPasswordFieldIsNotBlank() {

        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("john");
        existingUser.setPassword("$2a$10$existingEncodedPassword");
        existingUser.setFullname("John Doe");
        existingUser.setRole("USER");

        User updatedData = new User();
        updatedData.setUsername("john");
        updatedData.setPassword("NewPassword1!");
        updatedData.setFullname("John Doe");
        updatedData.setRole("USER");

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.update(1, updatedData);

        assertThat(result.getPassword()).isNotEqualTo("NewPassword1!");
        assertThat(passwordEncoder.matches("NewPassword1!", result.getPassword())).isTrue();

        verify(userRepository).findById(1);
        verify(userRepository).save(existingUser);
    }

    @Test
    public void update_shouldKeepExistingPassword_whenPasswordFieldIsBlank() {

        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("john");
        existingUser.setPassword("$2a$10$existingEncodedPassword");
        existingUser.setFullname("John Doe");
        existingUser.setRole("USER");

        User updatedData = new User();
        updatedData.setUsername("john-updated");
        updatedData.setPassword("");
        updatedData.setFullname("John Updated");
        updatedData.setRole("ADMIN");

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));


        User result = userService.update(1, updatedData);

        assertThat(result.getPassword()).isEqualTo("$2a$10$existingEncodedPassword");
        assertThat(result.getUsername()).isEqualTo("john-updated");
        assertThat(result.getFullname()).isEqualTo("John Updated");
        assertThat(result.getRole()).isEqualTo("ADMIN");

        verify(userRepository).findById(1);
        verify(userRepository).save(existingUser);
    }

    @Test
    public void deleteById_shouldDeleteUser() {

        int userId = 1;

        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
