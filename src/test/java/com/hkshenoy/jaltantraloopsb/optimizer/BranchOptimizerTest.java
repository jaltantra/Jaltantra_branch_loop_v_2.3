package com.hkshenoy.jaltantraloopsb.optimizer;
import com.hkshenoy.jaltantraloopsb.optimizer.BranchOptimizer;
import com.hkshenoy.jaltantraloopsb.structs.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;

public class BranchOptimizerTest {

    private BranchOptimizer branchOptimizer;
    private NodeStruct[] nodeStructs;
    private PipeStruct[] pipeStructs;
    private CommercialPipeStruct[] commercialPipeStructs;
    private GeneralStruct generalStruct;
    private EsrGeneralStruct esrGeneralStruct;
    private EsrCostStruct[] esrCostsArray;
    private PumpGeneralStruct pumpGeneralStruct;
    private PumpManualStruct[] pumpManualArray;
    private ValveStruct[] valves;

    @BeforeEach
    public void setUp() throws Exception {


        nodeStructs = new NodeStruct[] {
                new NodeStruct(2, "Node1", 100.0, 50.0, 500.0, 10.0, 100.0, 90.0, 1),
                new NodeStruct(3, "Node2", 120.0, 60.0, 600.0, 15.0, 120.0, 100.0, 2)
        };

        pipeStructs = new PipeStruct[] {
                new PipeStruct(1, 1, 2, 100.0, 10.0, 0.01, 100.0, 2.0, 0.02, 1.0, 1000.0, true, false, true, 10.0, 20.0, 0.5),
                new PipeStruct(2, 2, 3, 200.0, 15.0, 0.015, 150.0, 3.0, 0.03, 1.5, 1500.0, false, false, true, 15.0, 25.0, 0.75)
        };

        commercialPipeStructs = new CommercialPipeStruct[] {
                new CommercialPipeStruct(10.0, 500.0, 100.0, 50000.0, 0.01),
                new CommercialPipeStruct(15.0, 700.0, 200.0, 70000.0, 0.015)
        };

        generalStruct = new GeneralStruct(
                "Test Project", "Test Organization", 10.0, 0.01, 1.0, 3.0, 2.0, 1.0, 5.0, 24.0,
                100.0, 50.0, 1, "Source", 500.0, 1000.0
        );

        esrGeneralStruct = new EsrGeneralStruct(
                true, 12.0, 1.2, 25.0, false, new int[]{}, new int[]{}
        );

        esrCostsArray = new EsrCostStruct[] {
                new EsrCostStruct(1000.0, 2000.0, 10000.0, 5.0),
                new EsrCostStruct(1500.0, 2500.0, 15000.0, 7.5)
        };

        pumpGeneralStruct = new PumpGeneralStruct(
                true, 2.0, 85.0, 5000.0, 0.10, 1.0, 0.05, 0.02, 15, new int[]{}
        );

        pumpManualArray = new PumpManualStruct[] {
                new PumpManualStruct(1, 10.0),
                new PumpManualStruct(2, 20.0)
        };

        valves = new ValveStruct[] {
                new ValveStruct(1, 0.5),
                new ValveStruct(2, 0.75)
        };

        branchOptimizer = new BranchOptimizer(
                nodeStructs, pipeStructs, commercialPipeStructs, generalStruct,
                esrGeneralStruct, esrCostsArray, pumpGeneralStruct, pumpManualArray, valves
        );

    }

    @Test
    @Tag("fast")
    public void testOptimizeWithInvalidNetwork() throws Exception {

        Field nodesField = BranchOptimizer.class.getDeclaredField("nodes");
        nodesField.setAccessible(true);


        HashMap<Integer, Node> emptyNodes = new HashMap<>();
        nodesField.set(branchOptimizer, emptyNodes);


        Exception exception = assertThrows(Exception.class, () -> {
            branchOptimizer.Optimize();
        });

        String expectedMessage = "Input is not valid. Nodes unconnected in the network";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    @Tag("fast")
    public void testValidateNetworkUsingReflection() throws Exception {

        Method validateNetworkMethod = BranchOptimizer.class.getDeclaredMethod("validateNetwork");
        validateNetworkMethod.setAccessible(true);

        int result = (int) validateNetworkMethod.invoke(branchOptimizer);
        assertEquals(1, result);

    }
}

