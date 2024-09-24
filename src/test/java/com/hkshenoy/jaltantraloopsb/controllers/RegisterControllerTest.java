package com.hkshenoy.jaltantraloopsb.controllers;

import com.hkshenoy.jaltantraloopsb.helper.CustomLogger;
import com.hkshenoy.jaltantraloopsb.security.User;
import com.hkshenoy.jaltantraloopsb.security.UserDto;
import com.hkshenoy.jaltantraloopsb.security.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import com.hkshenoy.jaltantraloopsb.config.TestSecurityConfig;


@WebMvcTest(RegisterController.class)
@Import(TestSecurityConfig.class) // Import the custom test configuration
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomLogger customLogger;

    @MockBean
    private UserService userService;

    @MockBean
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }

    @Test
    public void testRegistration_Success() throws Exception {

        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setName("John Doe");
        userDto.setCountry("CountryName");
        userDto.setState("StateName");
        userDto.setOrganization("OrganizationName");
        userDto.setDesignation("DesignationName");
        userDto.setPassword("password123");

        Mockito.when(userService.findUserByEmail(userDto.getEmail())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/register/save")
                        .flashAttr("user", userDto))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register?success"));
    }


    @Test
    public void testRegistration_Failure() throws Exception {

        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");

        User existingUser = new User();
        existingUser.setEmail("test@example.com");
        Mockito.when(userService.findUserByEmail(userDto.getEmail())).thenReturn(existingUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/register/save")
                        .flashAttr("user", userDto))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Expect HTTP 200 OK status
                .andExpect(MockMvcResultMatchers.view().name("/register")) // Expect the correct view name with leading slash
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("user", "email")) // Check for field errors in email
                .andExpect(MockMvcResultMatchers.model().attributeExists("user")); // Ensure 'user' attribute is present in the model
    }

}
