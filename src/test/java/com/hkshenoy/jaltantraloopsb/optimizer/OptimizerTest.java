package com.hkshenoy.jaltantraloopsb.optimizer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.hkshenoy.jaltantraloopsb.structs.*;
import com.hkshenoy.jaltantraloopsb.helper.CustomLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class OptimizerTest {

    private Optimizer optimizer;
    private NodeStruct[] nodeStructs;
    private PipeStruct[] pipeStructs;
    private CommercialPipeStruct[] commercialPipeStructs;
    private EsrCostStruct[] esrCostsArray;
    private PumpManualStruct[] pumpManualArray;
    private ValveStruct[] valves;
    private GeneralStruct generalStruct;
    private EsrGeneralStruct esrGeneralStruct;
    private PumpGeneralStruct pumpGeneralStruct;
    private CustomLogger mockLogger;

    @BeforeEach
    public void setUp() throws Exception {
        // Mocking CustomLogger
        mockLogger = Mockito.mock(CustomLogger.class);

        // Initialize node structures for optimizer
        nodeStructs = new NodeStruct[] {
                new NodeStruct(2, "Node2", 120.0, 60.0, 600.0, 15.0, 120.0, 100.0, 2),
                new NodeStruct(3, "Node3", 130.0, 70.0, 700.0, 20.0, 130.0, 110.0, 3),
                new NodeStruct(4, "Node4", 140.0, 80.0, 800.0, 25.0, 140.0, 120.0, 4)
        };

        // Initialize pipes for optimizer
        pipeStructs = new PipeStruct[] {
                new PipeStruct(1, 1, 2, 100.0, 10.0, 0.01, 100.0, 2.0, 0.02, 1.0, 1000.0, true, false, true, 10.0, 20.0, 0.5),
                new PipeStruct(2, 2, 3, 150.0, 15.0, 0.015, 150.0, 3.0, 0.03, 1.5, 1500.0, false, false, true, 15.0, 25.0, 0.75),
                new PipeStruct(3, 3, 4, 200.0, 20.0, 0.02, 200.0, 4.0, 0.04, 2.0, 2000.0, true, true, false, 20.0, 30.0, 0.6)
        };

        commercialPipeStructs = new CommercialPipeStruct[] {
                new CommercialPipeStruct(10.0, 500.0, 100.0, 50000.0, 0.01),
                new CommercialPipeStruct(15.0, 700.0, 200.0, 70000.0, 0.015)
        };

        esrCostsArray = new EsrCostStruct[] {
                new EsrCostStruct(1000.0, 2000.0, 10000.0, 5.0),
                new EsrCostStruct(1500.0, 2500.0, 15000.0, 7.5)
        };

        pumpManualArray = new PumpManualStruct[] {
                new PumpManualStruct(1, 10.0),
                new PumpManualStruct(2, 20.0)
        };

        valves = new ValveStruct[] {
                new ValveStruct(1, 0.5),
                new ValveStruct(2, 0.75)
        };

        generalStruct = new GeneralStruct(
                "Test Project", "Test Organization", 10.0, 0.01, 1.0, 3.0, 2.0, 1.0, 5.0, 24.0,
                100.0, 50.0, 1, "Source", 500.0, 1000.0
        );

        esrGeneralStruct = new EsrGeneralStruct(
                true, 12.0, 1.2, 25.0, false, new int[]{}, new int[]{}
        );

        pumpGeneralStruct = new PumpGeneralStruct(
                true, 2.0, 85.0, 5000.0, 0.10, 1.0, 0.05, 0.02, 15, new int[]{}
        );

        optimizer = new Optimizer(
                "solverDir", nodeStructs, pipeStructs, commercialPipeStructs, generalStruct,
                esrGeneralStruct, esrCostsArray, pumpGeneralStruct, pumpManualArray, valves
        );

        // Injecting the mock logger into the optimizer (if necessary)
        Field loggerField = Optimizer.class.getDeclaredField("customLogger");
        loggerField.setAccessible(true);
        loggerField.set(optimizer, mockLogger);
    }


    @Test
    public void testConstructorInitialization() {
        assertNotNull(optimizer);
        assertEquals(generalStruct, optimizer.getGeneralProperties());
        assertEquals(esrGeneralStruct, optimizer.getEsrGeneralProperties());
        assertEquals(pumpGeneralStruct, optimizer.getPumpGeneralProperties());
    }

    @Test
    public void testValidateNetworkValid() throws Exception {
        // Test with a valid acyclic network
        Method validateNetworkMethod = Optimizer.class.getDeclaredMethod("validateNetwork");
        validateNetworkMethod.setAccessible(true);
        int result = (int) validateNetworkMethod.invoke(optimizer);
        assertEquals(2, result);
    }

    @Test
    public void testValidateNetworkWithCyclicGraphUsingReflection() throws Exception {
        // Create a cyclic network
        NodeStruct node1 = new NodeStruct(7, "Node1", 100.0, 50.0, 500.0, 10.0, 100.0, 90.0, 1);
        NodeStruct node2 = new NodeStruct(8, "Node2", 120.0, 60.0, 600.0, 15.0, 120.0, 100.0, 2);
        NodeStruct node3 = new NodeStruct(9, "Node3", 130.0, 70.0, 700.0, 20.0, 130.0, 110.0, 3);

        nodeStructs = new NodeStruct[]{node1, node2, node3};

        pipeStructs = new PipeStruct[]{
                new PipeStruct(1, 1, 7, 100.0, 10.0, 0.01, 100.0, 2.0, 0.02, 1.0, 1000.0, true, false, true, 10.0, 20.0, 0.5),
                new PipeStruct(2, 1, 8, 150.0, 15.0, 0.015, 150.0, 3.0, 0.03, 1.5, 1500.0, false, false, true, 15.0, 25.0, 0.75),
                new PipeStruct(3, 1, 9, 200.0, 20.0, 0.02, 200.0, 4.0, 0.04, 2.0, 2000.0, true, true, false, 20.0, 30.0, 0.6)
        };

        optimizer = new Optimizer(
                "solverDir", nodeStructs, pipeStructs, commercialPipeStructs, generalStruct,
                esrGeneralStruct, esrCostsArray, pumpGeneralStruct, pumpManualArray, valves
        );

        Method validateNetworkMethod = Optimizer.class.getDeclaredMethod("validateNetwork");
        validateNetworkMethod.setAccessible(true);
        int result = (int) validateNetworkMethod.invoke(optimizer);
        assertEquals(2, result);  // Expected 2: Cyclic graph
    }

    @Test
    public void testValidateNetworkWithDuplicateLinksUsingReflection() throws Exception {
        // Create a network with duplicate links
        NodeStruct node1 = new NodeStruct(10, "Node1", 100.0, 50.0, 500.0, 10.0, 100.0, 90.0, 1);
        NodeStruct node2 = new NodeStruct(11, "Node2", 120.0, 60.0, 600.0, 15.0, 120.0, 100.0, 2);

        nodeStructs = new NodeStruct[]{node1, node2};

        pipeStructs = new PipeStruct[]{
                new PipeStruct(1, 1, 10, 100.0, 10.0, 0.01, 100.0, 2.0, 0.02, 1.0, 1000.0, true, false, true, 10.0, 20.0, 0.5),
                new PipeStruct(3, 1, 10, 100.0, 10.0, 0.01, 100.0, 2.0, 0.02, 1.0, 1000.0, true, false, true, 10.0, 20.0, 0.5),
                new PipeStruct(2, 1, 11, 150.0, 15.0, 0.015, 150.0, 3.0, 0.03, 1.5, 1500.0, false, false, true, 15.0, 25.0, 0.75)
        };

        optimizer = new Optimizer(
                "solverDir", nodeStructs, pipeStructs, commercialPipeStructs, generalStruct,
                esrGeneralStruct, esrCostsArray, pumpGeneralStruct, pumpManualArray, valves
        );

        Method validateNetworkMethod = Optimizer.class.getDeclaredMethod("validateNetwork");
        validateNetworkMethod.setAccessible(true);
        int result = (int) validateNetworkMethod.invoke(optimizer);
        assertEquals(5, result);  // Expected 5: Duplicate links found
    }

    @Test
    public void testOptimizeWithInvalidNetwork() throws Exception {
        // Simulate an invalid network by clearing nodeStructs
        NodeStruct node1 = new NodeStruct(10, "Node1", 100.0, 50.0, 500.0, 10.0, 100.0, 90.0, 1);
        NodeStruct node2 = new NodeStruct(11, "Node2", 120.0, 60.0, 600.0, 15.0, 120.0, 100.0, 2);

        nodeStructs = new NodeStruct[]{node1, node2};

        pipeStructs = new PipeStruct[]{
                new PipeStruct(1, 1, 10, 100.0, 10.0, 0.01, 100.0, 2.0, 0.02, 1.0, 1000.0, true, false, true, 10.0, 20.0, 0.5)
        };
        optimizer = new Optimizer(
                "solverDir", nodeStructs, pipeStructs, commercialPipeStructs, generalStruct,
                esrGeneralStruct, esrCostsArray, pumpGeneralStruct, pumpManualArray, valves
        );
        Method validateNetworkMethod = Optimizer.class.getDeclaredMethod("validateNetwork");
        validateNetworkMethod.setAccessible(true);
        int result = (int) validateNetworkMethod.invoke(optimizer);
        assertEquals(3, result);
    }

    @Test
    public void testNullGeneralProperties() {
        Exception exception = assertThrows(Exception.class, () -> {
            new Optimizer(
                    "solverDir", new NodeStruct[]{}, new PipeStruct[]{},
                    new CommercialPipeStruct[]{}, null, esrGeneralStruct,
                    new EsrCostStruct[]{}, pumpGeneralStruct,
                    new PumpManualStruct[]{}, new ValveStruct[]{}
            );
        });

        String expectedMessage = "GeneralStruct cannot be null";
        String actualMessage = exception.getMessage();

        assertFalse(actualMessage.contains(expectedMessage));
    }
}
