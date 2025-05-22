package com.hkshenoy.jaltantraloopsb.security;

import com.sendgrid.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testSaveUser() throws UnsupportedEncodingException {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setCountry("Country");
        userDto.setOrganization("Organization");
        userDto.setState("State");
        userDto.setDesignation("Designation");
        userDto.setPassword("password123");

        // Mock PasswordEncoder.encode
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Mock UserRepository.save
        when(userRepository.save(any(User.class))).thenReturn(new User());

        userService.saveUser(userDto, "http://localhost:8080");

        // Verify that userRepository.save was called
        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    void testFindUserByEmail() {
        User user = new User();
        user.setEmail("anksushmitra17@gmail.com");

        // Mock UserRepository.findByEmail
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        User result = userService.findUserByEmail("anksushmitra17@gmail.com");

        assertNotNull(result);
        assertEquals("anksushmitra17@gmail.com", result.getEmail());
    }

    @Test
    void testFindAllUsers() {
        User user = new User();
        user.setEmail("john.doe@example.com");

        List<User> users = Collections.singletonList(user);

        // Mock UserRepository.findAll
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> userDtos = userService.findAllUsers();

        assertNotNull(userDtos);
        assertEquals(1, userDtos.size());
        assertEquals("john.doe@example.com", userDtos.get(0).getEmail());
    }

    @Test
    void testVerify() {
        User user = new User();
        user.setEnabled(false);
        user.setVerificationCode("validCode");

        // Mock UserRepository.findByVerificationCode
        when(userRepository.findByVerificationCode(anyString())).thenReturn(user);

        // Mock UserRepository.save
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean result = userService.verify("validCode");

        assertEquals(true, result);
        assertEquals(null, user.getVerificationCode());
        assertEquals(true, user.isEnabled());
    }
}

