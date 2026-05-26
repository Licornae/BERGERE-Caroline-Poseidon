package unitTests;


import com.nnk.springboot.configuration.security.CustomUserDetailsService;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnitCustomUserDetailsTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {

        User user = new User();
        user.setUsername("john");
        user.setPassword("$2a$10$encodedPassword");
        user.setRole("USER");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("john");

        assertThat(result.getUsername()).isEqualTo("john");
        assertThat(result.getPassword()).isEqualTo("$2a$10$encodedPassword");
        assertThat(result.getAuthorities())
                .extracting("authority")
                .contains("ROLE_USER");
    }
}
