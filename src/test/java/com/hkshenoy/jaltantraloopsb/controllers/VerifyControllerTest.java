package com.hkshenoy.jaltantraloopsb.controllers;

import com.hkshenoy.jaltantraloopsb.security.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VerifyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private VerifyController verifyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(verifyController).build();
    }

    @Test
    void verifyUser_Success() throws Exception {
        String code = "validCode";

        when(userService.verify(code)).thenReturn(true);

        mockMvc.perform(get("/verify").param("code", code))
                .andExpect(status().isOk())
                .andExpect(view().name("verify_success"));

        verify(userService, times(1)).verify(code);
    }

    @Test
    void verifyUser_Fail() throws Exception {
        String code = "invalidCode";

        when(userService.verify(code)).thenReturn(false);

        mockMvc.perform(get("/verify").param("code", code))
                .andExpect(status().isOk())
                .andExpect(view().name("verify_fail"));

        verify(userService, times(1)).verify(code);
    }
}

