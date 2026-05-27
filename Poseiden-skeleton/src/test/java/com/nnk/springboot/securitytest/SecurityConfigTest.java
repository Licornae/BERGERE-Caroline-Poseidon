package com.nnk.springboot.securitytest;

import com.nnk.springboot.configuration.SecurityConfig;
import com.nnk.springboot.configuration.security.CustomUserDetailsService;
import com.nnk.springboot.controllers.LoginController;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(
        controllers = LoginController.class,
        properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration"
)
@Import(SecurityConfig.class)
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void loginPage_shouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/app/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void protectedPage_shouldRedirectToLogin_whenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/app/login"));
    }

    @Test
    public void login_shouldSucceed_whenCredentialsAreValid() throws Exception {
        when(customUserDetailsService.loadUserByUsername("valid-user"))
                .thenReturn(User.withUsername("valid-user")
                        .password("encoded-password")
                        .roles("USER")
                        .build());
        when(passwordEncoder.matches("valid-password", "encoded-password")).thenReturn(true);

        mockMvc.perform(formLogin("/app/login")
                        .user("valid-user")
                        .password("valid-password"))
                .andExpect(authenticated().withUsername("valid-user"))
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    public void login_shouldFail_whenCredentialsAreInvalid() throws Exception {
        when(customUserDetailsService.loadUserByUsername("bad-user"))
                .thenThrow(new UsernameNotFoundException("User not found: bad-user"));

        mockMvc.perform(formLogin("/app/login")
                        .user("bad-user")
                        .password("bad-password"))
                .andExpect(unauthenticated());
    }
}
