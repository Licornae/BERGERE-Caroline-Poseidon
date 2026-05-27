package com.nnk.springboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the application.
 * This class configures session-based authentication using a custom login page
 * and Spring Security auto-configuration for authentication.
 * Public resources such as the login page and static assets are accessible
 * without authentication. All other routes require an authenticated user.
 */
@Configuration
public class SecurityConfig {

    /**
     * Defines the HTTP security filter chain.
     * This configuration allows unauthenticated access to the login page and static
     * resources, requires authentication for every other request, configures the
     * custom login route, and enables logout with session invalidation.
     *
     * @param http the {@link HttpSecurity} object used to configure web security
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if the security filter chain cannot be built
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/app/login",
                                "/app/error",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/user/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/app/login")
                        .loginProcessingUrl("/app/login")
                        .defaultSuccessUrl("/bidList/list", true)
                        .failureUrl("/app/login?error=true")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/app/error")
                )
                .logout(logout -> logout
                        .logoutUrl("/app/logout")
                        .logoutSuccessUrl("/app/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}
