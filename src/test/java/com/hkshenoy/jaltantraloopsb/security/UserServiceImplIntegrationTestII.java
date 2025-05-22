package com.hkshenoy.jaltantraloopsb.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional // This will rollback transactions after each test
public class UserServiceImplIntegrationTestII{

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("testuser@example.com");
        userDto.setCountry("Test Country");
        userDto.setOrganization("Test Org");
        userDto.setState("Test State");
        userDto.setDesignation("Tester");
        userDto.setPassword("Password123");
    }

    @Test
    public void testSaveUser() throws UnsupportedEncodingException {
        // Act
        userService.saveUser(userDto, "http://localhost:8080");

        // Assert
        User savedUser = userRepository.findByEmail(userDto.getEmail());
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo(userDto.getName());
        assertThat(passwordEncoder.matches(userDto.getPassword(), savedUser.getPassword())).isTrue();
        assertThat(savedUser.isEnabled()).isTrue();
    }

    @Test
    public void testFindUserByEmail() throws UnsupportedEncodingException {
        // Arrange
        userService.saveUser(userDto, "http://localhost:8080");

        // Act
        User foundUser = userService.findUserByEmail(userDto.getEmail());

        // Assert
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(userDto.getEmail());
    }



}
