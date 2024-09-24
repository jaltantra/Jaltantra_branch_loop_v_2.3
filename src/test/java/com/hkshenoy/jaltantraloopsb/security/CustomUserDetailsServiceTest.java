package com.hkshenoy.jaltantraloopsb.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void testLoadUserByUsername_UserExistsAndEnabled() {
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setEnabled(true);

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("john.doe@example.com");

        assertEquals("john.doe@example.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername("nonexistent@example.com")
        );
    }

    @Test
    void testLoadUserByUsername_UserNotEnabled() {
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setEnabled(false);

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername("john.doe@example.com")
        );
    }
}

