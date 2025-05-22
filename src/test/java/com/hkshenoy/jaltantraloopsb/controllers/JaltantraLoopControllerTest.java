package com.hkshenoy.jaltantraloopsb.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hkshenoy.jaltantraloopsb.helper.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class JaltantraLoopControllerTest {

    @Mock
    private OptimizationPerformer optimizationPerformer;

    @Mock
    private NonOptimizationRelatedActionPerformer nonOptimizationRelatedActionPerformer;

    @Mock
    private CustomLogger customLogger;

    @Mock
    private UserHistoryTracker userHistoryTracker;

    @InjectMocks
    private JaltantraLoopController jaltantraLoopController;

    @Mock
    private UserDetails userDetails;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoPost_ActionNotNull() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("someAction");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("")));

        jaltantraLoopController.doPost(userDetails, request, response);

        verify(nonOptimizationRelatedActionPerformer).performNonOptimizationRelatedAction(request, response, "someAction");
        verifyNoMoreInteractions(optimizationPerformer);
    }

    @Test
    @Tag("fast")
    public void testDoPost_ActionNull() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("time")).thenReturn("10");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("")));

        jaltantraLoopController.doPost(userDetails, request, response);

        verify(optimizationPerformer).performOptimization(request, response);
        verifyNoMoreInteractions(nonOptimizationRelatedActionPerformer);
    }

    @Test
    @Tag("fast")
    public void testDoPost_UserNotNull() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("time")).thenReturn("10");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("")));

        jaltantraLoopController.doPost(userDetails, request, response);

        verify(userHistoryTracker).saveUserRequest(userDetails, request);
    }

    @Test
    @Tag("fast")
    public void testDoPost_LogRequestParameters() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("time")).thenReturn("10");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("")));

        jaltantraLoopController.doPost(userDetails, request, response);

        verify(customLogger).logd("doPost() called");
        verify(customLogger).logd("doPost() action=null");
    }

}
