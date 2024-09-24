package com.hkshenoy.jaltantraloopsb.helper;

import com.google.gson.Gson;
import com.hkshenoy.jaltantraloopsb.optimizer.Node;
import com.hkshenoy.jaltantraloopsb.optimizer.Optimizer;
import com.hkshenoy.jaltantraloopsb.optimizer.Pipe;
import com.hkshenoy.jaltantraloopsb.structs.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OptimizationPerformerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;

    @Mock
    private CustomLogger customLogger;

    @InjectMocks
    private OptimizationPerformer optimizationPerformer;

    @Value("${JALTANTRA_VERSION}")
    private String version;

    @Value("${solver.root.dir}")
    private String solverRootDir;

    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testPerformOptimization_success() throws Exception {
        // Mock the Optimizer and necessary fields
        Optimizer opt = mock(Optimizer.class);

        // Mock request parameters
        String jsonGeneral = "{\"name_project\":\"Project1\",\"name_organization\":\"Org1\",\"max_water_speed\":10.0,\"supply_hours\":12.0,\"secondary_supply_hours\":6.0}";
        String jsonNodes = "[{\"nodeID\":1,\"nodeName\":\"Node1\",\"elevation\":100.0,\"demand\":10.0,\"requiredCapacity\":20.0,\"residualPressure\":5.0,\"head\":15.0,\"pressure\":10.0,\"esr\":0}]";
        String jsonPipes = "[{\"pipeID\":1,\"startNode\":1,\"endNode\":2,\"length\":10.0,\"diameter\":5.0,\"roughness\":0.1}]";
        String jsonCommercialPipes = "[{\"pipeID\":1,\"diameter\":5.0,\"cost\":100.0}]";
        String jsonEsrGeneral = "{\"esr_enabled\":true,\"esr_capacity_factor\":1.5,\"esr_general_properties\":{}}";
        String jsonEsrCost = "[{\"esrID\":1,\"cost\":50.0}]";
        String jsonPumpGeneral = "{\"pump_enabled\":true,\"pump_capacity\":100.0,\"pump_head\":10.0}";
        String jsonPumpManual = "[{\"pumpID\":1,\"capacity\":100.0,\"head\":10.0}]";
        String jsonValves = "[{\"valveID\":1,\"diameter\":5.0}]";

        when(request.getParameter("general")).thenReturn(jsonGeneral);
        when(request.getParameter("nodes")).thenReturn(jsonNodes);
        when(request.getParameter("pipes")).thenReturn(jsonPipes);
        when(request.getParameter("commercialPipes")).thenReturn(jsonCommercialPipes);
        when(request.getParameter("esrGeneral")).thenReturn(jsonEsrGeneral);
        when(request.getParameter("esrCost")).thenReturn(jsonEsrCost);
        when(request.getParameter("pumpGeneral")).thenReturn(jsonPumpGeneral);
        when(request.getParameter("pumpManual")).thenReturn(jsonPumpManual);
        when(request.getParameter("valves")).thenReturn(jsonValves);
        when(request.getParameter("version")).thenReturn("1.0");  // Adjust version accordingly
        when(request.getParameter("time")).thenReturn("30");

        // Mock Optimizer methods (ensure it is correctly mocked)
        when(opt.Optimize(anyString(), anyString(), anyString())).thenReturn(true);
        when(opt.getNodes()).thenReturn(new HashMap<>()); // Ensure correct return types

        // Set public fields of optimizer directly if necessary
        opt.resultPipes = new ArrayList<>();
        opt.resultCost = new ArrayList<>();
        opt.resultPumps = new ArrayList<>();

        // Now, perform optimization
        optimizationPerformer.performOptimization(request, response);

        // Verify the success output
        verify(writer).print(contains("\"status\":\"success\""));
    }

    @Test
    public void testPerformOptimization_noNodeData() throws Exception {
        String jsonGeneral = "{\"name_project\":\"Project1\",\"name_organization\":\"Org1\",\"max_water_speed\":10.0,\"supply_hours\":12.0,\"secondary_supply_hours\":6.0\"}";
        when(request.getParameter("general")).thenReturn(jsonGeneral);
        when(request.getParameter("nodes")).thenReturn(null); // No node data

        optimizationPerformer.performOptimization(request, response);

        verify(writer).print(
                "{\"status\":\"success\",\"message\":\"Your browser is running an old JalTantra version.<br> Please save your data and press ctrl+F5 to do a hard refresh and get the latest version.<br> If still facing issues please contact the <a target='_blank' href='https://groups.google.com/forum/#!forum/jaltantra-users/join'>JalTantra Google Group</a>\"}"
        );
    }

    @Test
    public void testPerformOptimization_failure() throws Exception {
        String jsonGeneral = "{\"name_project\":\"Project1\",\"name_organization\":\"Org1\",\"max_water_speed\":10.0,\"supply_hours\":12.0,\"secondary_supply_hours\":6.0\"}";
        when(request.getParameter("general")).thenReturn(jsonGeneral);
        when(request.getParameter("nodes")).thenReturn("[{\"nodeID\":1,\"nodeName\":\"Node1\",\"elevation\":100.0,\"demand\":10.0,\"requiredCapacity\":20.0,\"residualPressure\":5.0,\"head\":15.0,\"pressure\":10.0,\"esr\":0}]");
        when(request.getParameter("pipes")).thenReturn("[{\"pipeID\":1,\"startNode\":1,\"endNode\":2,\"length\":10.0,\"diameter\":5.0,\"roughness\":0.1}]");
        when(request.getParameter("commercialPipes")).thenReturn("[{\"pipeID\":1,\"diameter\":5.0,\"cost\":100.0}]");
        when(request.getParameter("esrGeneral")).thenReturn("{\"esr_enabled\":true,\"esr_capacity_factor\":1.5,\"esr_general_properties\":{}}");
        when(request.getParameter("esrCost")).thenReturn("[{\"esrID\":1,\"cost\":50.0}]");
        when(request.getParameter("pumpGeneral")).thenReturn("{\"pump_enabled\":true,\"pump_capacity\":100.0,\"pump_head\":10.0}");
        when(request.getParameter("pumpManual")).thenReturn("[{\"pumpID\":1,\"capacity\":100.0,\"head\":10.0}]");
        when(request.getParameter("valves")).thenReturn("[{\"valveID\":1,\"diameter\":5.0}]");

        // Simulate failure in optimizer
        Optimizer opt = mock(Optimizer.class);
        when(opt.Optimize(any(String.class), any(String.class), any(String.class))).thenReturn(false);

        optimizationPerformer.performOptimization(request, response);

        verify(writer).print(
                "{\"status\":\"success\",\"message\":\"Your browser is running an old JalTantra version.<br> Please save your data and press ctrl+F5 to do a hard refresh and get the latest version.<br> If still facing issues please contact the <a target='_blank' href='https://groups.google.com/forum/#!forum/jaltantra-users/join'>JalTantra Google Group</a>\"}"
        );
    }

    @Test
    public void testPerformOptimization_exception() throws Exception {
        when(request.getParameter("general")).thenReturn(null); // This will cause an exception

        optimizationPerformer.performOptimization(request, response);

        verify(writer).print(
                "{\"status\":\"success\",\"message\":\"Your browser is running an old JalTantra version.<br> Please save your data and press ctrl+F5 to do a hard refresh and get the latest version.<br> If still facing issues please contact the <a target='_blank' href='https://groups.google.com/forum/#!forum/jaltantra-users/join'>JalTantra Google Group</a>\"}"
        );
    }
}
