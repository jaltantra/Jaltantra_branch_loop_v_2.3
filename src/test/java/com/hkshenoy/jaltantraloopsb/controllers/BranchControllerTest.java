package com.hkshenoy.jaltantraloopsb.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hkshenoy.jaltantraloopsb.helper.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BranchControllerTest {

    @Mock
    private UserDetails user;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private UserHistoryTracker userHistoryTracker;

    @Mock
    private XMLUploader xmlUploader;

    @Mock
    private ExcelUploader excelUploader;

    @Mock
    private ExcelOutputUploader excelOutputUploader;

    @Mock
    private EPANetUploader epaNetUploader;

    @Mock
    private MapSnapshotUploader mapSnapshotUploader;

    @InjectMocks
    private BranchController branchController;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }




    @Test
    public void testDoPost_SaveInputExcel() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("saveInputExcel");

        branchController.doPost(user, request, response);

        verify(excelUploader).uploadExcelInputFile(request, response);
        verifyNoMoreInteractions(xmlUploader, excelUploader, excelOutputUploader, epaNetUploader, mapSnapshotUploader);
    }

    @Test
    public void testDoPost_SaveOutputExcel() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("saveOutputExcel");

        branchController.doPost(user, request, response);

        verify(excelOutputUploader).uploadExcelOutputFile(request, response);
        verifyNoMoreInteractions(xmlUploader, excelUploader, excelOutputUploader, epaNetUploader, mapSnapshotUploader);
    }

    @Test
    public void testDoPost_VersionCheck_OldVersion() throws ServletException, IOException, NoSuchFieldException, IllegalAccessException {
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("version")).thenReturn("1.0");
        when(request.getParameter("general")).thenReturn("{\"name_project\": \"TestProject\"}");
        when(request.getParameter("nodes")).thenReturn("[{}]");
        when(request.getParameter("pipes")).thenReturn("[{}]");
        when(request.getParameter("commercialPipes")).thenReturn("[{}]");
        when(request.getParameter("esrGeneral")).thenReturn("{}");
        when(request.getParameter("pumpGeneral")).thenReturn("{}");
        when(request.getParameter("esrCost")).thenReturn("[{}]");
        when(request.getParameter("pumpManual")).thenReturn("[{}]");
        when(request.getParameter("valves")).thenReturn("[{}]");
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        Field versionField = BranchController.class.getDeclaredField("version");
        versionField.setAccessible(true);
        versionField.set(branchController, "0.9"); // Setting a lower version to trigger the clientOld condition

        branchController.doPost(user, request, response);

        verify(writer).print(contains("\"status\":\"error\",\"message\":\"Your browser is running an old JalTantra version.<br> Please save your data and press ctrl+F5 to do a hard refresh and get the latest version.<br> If still facing issues please contact the <a target='_blank' href='https://groups.google.com/forum/#!forum/jaltantra-users/join'>JalTantra Google Group</a>\""));
    }

}
