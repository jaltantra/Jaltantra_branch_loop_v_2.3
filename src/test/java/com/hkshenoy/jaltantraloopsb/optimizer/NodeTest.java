package com.hkshenoy.jaltantraloopsb.optimizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    private Node node;
    private Node upstreamNode;
    private Node downstreamNode;
    private Pipe pipe;
    private Set<Integer> usedNodeIDs;
    private Set<Integer> usedPipeIDs;
    private Node servedNode;

    @BeforeEach
    void setUp() throws Exception {
        usedNodeIDs = new HashSet<>();
        usedPipeIDs = new HashSet<>();


        node = new Node(50.0, 5.0, 1, 2.0, "TestNode", 1.5, usedNodeIDs);
        upstreamNode = new Node(60.0, 4.0, 2, 1.5, "UpstreamNode", 1.5, usedNodeIDs);
        downstreamNode = new Node(70.0, 3.0, 3, 1.2, "DownstreamNode", 1.5, usedNodeIDs);
        servedNode = new Node(65.0, 4.5, 4, 1.8, "ServedNode", 1.5, usedNodeIDs);

        pipe = new Pipe(100.0, node, downstreamNode, 200.0, 120.0, 1, true, usedPipeIDs);
    }

    @Test
    void testGetNodeID() {
        assertEquals(1, node.getNodeID());
    }

    @Test
    void testGetDemand() {
        assertEquals(7.5, node.getDemand(), 0.01); // Based on the peakFactor (5.0 * 1.5)
    }

    @Test
    void testSetDemand() {
        node.setDemand(10.0);
        assertEquals(10.0, node.getDemand());
    }

    @Test
    void testGetAllowESR() {
        assertTrue(node.getAllowESR());
    }

    @Test
    void testSetAllowESR() {
        node.setAllowESR(false);
        assertFalse(node.getAllowESR());
    }

    @Test
    void testGetNodeName() {
        assertEquals("TestNode", node.getNodeName());
    }

    @Test
    void testGetResidualPressure() {
        assertEquals(2.0, node.getResidualPressure(), 0.01);
    }

    @Test
    void testAddToOutgoingPipes() {
        node.addToOutgoingPipes(pipe);
        List<Pipe> outgoingPipes = node.getOutgoingPipes();
        assertEquals(2, outgoingPipes.size());
        assertEquals(pipe, outgoingPipes.get(0));
    }

    @Test
    void testAddToSourceToNodePipes() {
        node.addToSourceToNodePipes(pipe);
        List<Pipe> sourceToNodePipes = node.getSourceToNodePipes();
        assertEquals(1, sourceToNodePipes.size());
        assertEquals(pipe, sourceToNodePipes.get(0));
    }

    @Test
    void testAddToUpstreamNodes() {
        node.addToUpstreamNodes(upstreamNode);
        Set<Node> upstreamNodes = node.getUpstreamNodes();
        assertEquals(1, upstreamNodes.size());
        assertTrue(upstreamNodes.contains(upstreamNode));
    }

    @Test
    void testAddToDownstreamNodes() {
        node.addToDownstreamNodes(downstreamNode);
        Set<Node> downstreamNodes = node.getDownstreamNodes();
        assertEquals(1, downstreamNodes.size());
        assertTrue(downstreamNodes.contains(downstreamNode));
    }

    @Test
    void testGetElevation() {
        assertEquals(50.0, node.getElevation(), 0.01);
    }

    @Test
    void testSetHead() {
        node.setHead(75.0);
        assertEquals(75.0, node.getHead(), 0.01);
    }

    @Test
    void testGetPressure() {
        node.setHead(75.0);
        assertEquals(25.0, node.getPressure(), 0.01); // 75.0 (head) - 50.0 (elevation) = 25.0
    }

    @Test
    void testGetAndSetESR() {
        assertEquals(1, node.getESR());
        node.setESR(2);
        assertEquals(2, node.getESR());
    }

    @Test
    void testGetAndSetEsrHeight() {
        node.setEsrHeight(10.0);
        assertEquals(10.0, node.getEsrHeight(), 0.01);
    }

    @Test
    void testGetAndSetEsrTotalDemand() {
        node.setEsrTotalDemand(1000.0);
        assertEquals(1000.0, node.getEsrTotalDemand(), 0.01);
    }

    @Test
    void testAddToServedNodes() {
        node.addToServedNodes(servedNode);
        Set<Node> servedNodes = node.getServedNodes();
        assertEquals(1, servedNodes.size());
        assertTrue(servedNodes.contains(servedNode));
    }

    @Test
    void testGetAndSetEsrCost() {
        node.setEsrCost(5000.0);
        assertEquals(5000.0, node.getEsrCost(), 0.01);
    }

    @Test
    void testGetRequiredCapacity() {
        double esrCapacityFactor = 1.2;
        assertEquals(518400.0, node.getRequiredCapacity(esrCapacityFactor), 0.01); // 1.2 * 5.0 * 3600 * 24
    }
}
