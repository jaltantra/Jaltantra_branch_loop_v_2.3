package com.hkshenoy.jaltantraloopsb.optimizer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Set;

class edgeGroupTest {

    @Test
    void testConstructorWithSingleNode() {
        int id = 1;
        int node1 = 10;
        edgeGroup edge = new edgeGroup(id, node1);

        assertEquals(id, edge.getID());
        assertEquals(node1, edge.getNode1());
        assertEquals(-1, edge.getNode2());
        assertTrue(edge.getMapping().isEmpty());
    }

    @Test
    void testConstructorWithTwoNodes() {
        int id = 1;
        int node1 = 10;
        int node2 = 20;
        edgeGroup edge = new edgeGroup(id, node1, node2);

        assertEquals(id, edge.getID());
        assertEquals(node1, edge.getNode1());
        assertEquals(node2, edge.getNode2());
        assertTrue(edge.getMapping().isEmpty());
    }

    @Test
    void testOtherNode() {
        int id = 1;
        int node1 = 10;
        int node2 = 20;
        edgeGroup edge = new edgeGroup(id, node1, node2);

        assertEquals(node2, edge.otherNode(node1));
        assertEquals(node1, edge.otherNode(node2));
        assertEquals(-1, edge.otherNode(30));
    }

    @Test
    void testAddEdge() {
        edgeGroup edge1 = new edgeGroup(1, 10, 20);
        edgeGroup edge2 = new edgeGroup(2, 30, 40);

        edge1.addEdge(edge2);
        Set<edgeGroup> mapping = edge1.getMapping();

        assertEquals(1, mapping.size());
        assertTrue(mapping.contains(edge2));
    }

    @Test
    void testAddEndNode() {
        edgeGroup edge = new edgeGroup(1, -1, 20);

        edge.addEndNode(10);

        assertEquals(10, edge.getNode1());
        assertEquals(20, edge.getNode2());
    }

    @Test
    void testGetMappingSize() {
        edgeGroup edge1 = new edgeGroup(1, 10, 20);
        edgeGroup edge2 = new edgeGroup(2, 30, 40);

        edge1.addEdge(edge2);

        assertEquals(1, edge1.getMappingSize());
    }

    @Test
    void testTowards() {
        edgeGroup edge = new edgeGroup(1, 10, 20);

        assertEquals(-1, edge.towards(10));
        assertEquals(1, edge.towards(20));
    }

    @Test
    void testOrientToNode() {
        edgeGroup edge = new edgeGroup(1, 10, 20);

        assertEquals(20, edge.orientToNode(1));
        assertEquals(10, edge.orientToNode(-1));
        assertEquals(-1, edge.orientToNode(0));
    }
}

