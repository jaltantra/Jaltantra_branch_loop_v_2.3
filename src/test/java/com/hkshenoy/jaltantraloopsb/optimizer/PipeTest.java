package com.hkshenoy.jaltantraloopsb.optimizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PipeTest {

    private Node startNode;
    private Node endNode;
    private Set<Integer> usedPipeIDs;
    private PipeCost pipeCost1;
    private PipeCost pipeCost2;

    @BeforeEach
    public void setUp() throws Exception {
        Set<Integer> usedNodeIDs = new HashSet<>();
        startNode = new Node(100.0, 50.0, 1, 20.0, "StartNode", 1.5, usedNodeIDs);
        endNode = new Node(150.0, 60.0, 2, 25.0, "EndNode", 1.2, usedNodeIDs);
        usedPipeIDs = new HashSet<>();

        // Initialize PipeCost objects
        pipeCost1 = new PipeCost(300.0, 1000.0, 50.0, 0.1);
        pipeCost2 = new PipeCost(350.0, 1200.0, 60.0, 0.2);
    }

    @Test
    public void testPipeConstructor() throws Exception {
        Pipe pipe = new Pipe(500.0, startNode, endNode, 300.0, 100.0, 1, true, usedPipeIDs);
        pipe.setChosenPipeCost(pipeCost1);
        pipe.setChosenPipeCost2(pipeCost2);

        assertEquals(1, pipe.getPipeID());
        assertEquals(500.0, pipe.getLength());
        assertEquals(startNode, pipe.getStartNode());
        assertEquals(endNode, pipe.getEndNode());
        assertEquals(300.0, pipe.getDiameter());
        assertEquals(100.0, pipe.getRoughness());
        assertTrue(pipe.isAllowParallel());
        assertTrue(pipe.existingPipe());
        assertEquals(Pipe.FlowType.PRIMARY, pipe.getFlowchoice());
        assertEquals(pipeCost1, pipe.getChosenPipeCost());
        assertEquals(pipeCost2, pipe.getChosenPipeCost2());
    }

    @Test
    public void testSetFlow() throws Exception {
        Pipe pipe = new Pipe(500.0, startNode, endNode, 300.0, 100.0, 1, true, usedPipeIDs);
        pipe.setFlow(75.0);

        assertEquals(75.0, pipe.getFlow());
    }

    @Test
    public void testSetDiameter2() throws Exception {
        Pipe pipe = new Pipe(500.0, startNode, endNode, 300.0, 100.0, 1, true, usedPipeIDs);
        pipe.setDiameter2(350.0);

        assertEquals(350.0, pipe.getDiameter2());
    }

    @Test
    public void testSetPumpHead() throws Exception {
        Pipe pipe = new Pipe(500.0, startNode, endNode, 300.0, 100.0, 1, true, usedPipeIDs);
        pipe.setPumpHead(15.0);

        assertEquals(15.0, pipe.getPumpHead());
    }

    @Test
    public void testSetAllowPump() throws Exception {
        Pipe pipe = new Pipe(500.0, startNode, endNode, 300.0, 100.0, 1, true, usedPipeIDs);
        pipe.setAllowPump(false);

        assertFalse(pipe.getAllowPump());
    }

    @Test
    public void testSetCustomID_InvalidID_ThrowsException() {
        Exception exception = assertThrows(Exception.class, () -> {
            new Pipe(500.0, startNode, endNode, 300.0, 100.0, -1, true, usedPipeIDs);
        });

        String expectedMessage = "Pipe ID -1 is invalid";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testSetCustomID_DuplicateID_ThrowsException() throws Exception {
        usedPipeIDs.add(1);

        Exception exception = assertThrows(Exception.class, () -> {
            new Pipe(500.0, startNode, endNode, 300.0, 100.0, 1, true, usedPipeIDs);
        });

        String expectedMessage = "Pipe ID 1 already being used";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
