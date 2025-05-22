package com.hkshenoy.jaltantraloopsb.security;

import com.hkshenoy.jaltantraloopsb.config.SecurityConfig;
import com.hkshenoy.jaltantraloopsb.controllers.BranchController;
import com.hkshenoy.jaltantraloopsb.controllers.JaltantraLoopController;
import com.hkshenoy.jaltantraloopsb.controllers.RegisterController;
import com.hkshenoy.jaltantraloopsb.controllers.VerifyController;
import com.hkshenoy.jaltantraloopsb.helper.*;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = BranchController.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JaltantraLoopController.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = RegisterController.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = VerifyController.class)
})
@Import({SpringSecurity.class, SecurityConfig.class})
class SpringSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private XMLUploader xmlUploader;
    @MockBean
    private ExcelUploader excelUploader;
    @MockBean
    private ExcelOutputUploader excelOutputUploader;
    @MockBean
    private EPANetUploader epanUploader;
    @MockBean
    private MapSnapshotUploader mapSnapshotUploader;

    @InjectMocks
    private SpringSecurity springSecurity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginPage() throws Exception {
        // Test that the login page is available
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }


    @Test
    void testSuccessfulLogin() throws Exception {
        // Define test credentials
        String testEmail = "test@test.com";
        String testPassword = "password";

        // Create a mock UserDetails object
        UserDetails mockUserDetails = mock(UserDetails.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Encode the test password
        String encodedPassword = passwordEncoder.encode(testPassword);

        // Mock UserDetails behavior
        when(mockUserDetails.getUsername()).thenReturn(testEmail);
        when(mockUserDetails.getPassword()).thenReturn(encodedPassword);
        when(mockUserDetails.isAccountNonLocked()).thenReturn(true);
        when(mockUserDetails.isAccountNonExpired()).thenReturn(true);
        when(mockUserDetails.isCredentialsNonExpired()).thenReturn(true);
        when(mockUserDetails.isEnabled()).thenReturn(true);

        // Create a mock User object
        User mockUser = mock(User.class);
        when(mockUser.getSessionDetail()).thenReturn(new ArrayList<>());
        when(userRepository.findByEmail(testEmail)).thenReturn(mockUser);

        // Mock UserDetailsService to return the mock UserDetails
        when(userDetailsService.loadUserByUsername(testEmail)).thenReturn(mockUserDetails);

        // Simulate login with correct credentials
        mockMvc.perform(formLogin("/login")  // Ensure you specify the login URL
                        .user(testEmail)
                        .password(testPassword))
                .andExpect(status().is3xxRedirection()) // Ensure this matches your configuration
                .andReturn();

        // Verify that the user repository save method was called
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFailedLogin() throws Exception {
        // Simulate a failed login due to incorrect credentials
        mockMvc.perform(formLogin()
                        .user("wrong@test.com")
                        .password("wrongpassword"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

    @Test
    @WithMockUser(username = "test@test.com", password = "password")
    void testLogout() throws Exception {
        // Simulate a logout
        mockMvc.perform(SecurityMockMvcRequestBuilders.logout("/logout"))
                .andExpect(redirectedUrl("/"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());

        // Verify that the user repository is not called during logout (no changes expected)
        verify(userRepository, times(0)).save(any(User.class));
    }
}

/*
    @Test
    void testSuccessfulLogin() throws Exception {
        // Mock user details
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("test@test.com");
        when(mockUserDetails.getPassword()).thenReturn(new BCryptPasswordEncoder().encode("password")); // Encode the password properly
        when(mockUserDetails.isAccountNonLocked()).thenReturn(true);  // Ensure the account is unlocked
        when(mockUserDetails.isEnabled()).thenReturn(true);  // Ensure the account is enabled
        when(mockUserDetails.isCredentialsNonExpired()).thenReturn(true);  // Ensure the credentials are not expired
        when(mockUserDetails.isAccountNonExpired()).thenReturn(true);  // Ensure the account is not expired

        // Mock repository response
        when(userDetailsService.loadUserByUsername("test@test.com")).thenReturn(mockUserDetails);
        when(userRepository.findByEmail(eq("test@test.com"))).thenReturn(user);

        // Simulate login
        mockMvc.perform(SecurityMockMvcRequestBuilders.formLogin()
                        .user("test@test.com")
                        .password("password"))  // This password must match the encoded one
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(redirectedUrl(""));

        // Verify that the user session is updated
        verify(userRepository, times(1)).save(any(User.class));
    }
*/