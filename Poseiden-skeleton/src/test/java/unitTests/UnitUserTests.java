package unitTests;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    public void save_shouldReturnSavedUser() {

        User userToSave = new User();
        userToSave.setUsername("newUser");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setUsername("newUser");

        when(userRepository.save(userToSave)).thenReturn(savedUser);

        User actualUser = userService.save(userToSave);

        assertThat(actualUser.getId()).isNotNull();
        assertThat(actualUser.getUsername()).isEqualTo(savedUser.getUsername());
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
