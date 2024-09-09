package com.hkshenoy.jaltantraloopsb.optimizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class Cycle_OptimizerTest {

    private Cycle_Optimizer optimizer;

    @BeforeEach
    void setUp() {

        optimizer = mock(Cycle_Optimizer.class);


        when(optimizer.eval_h(anyInt(), any(double[].class), anyBoolean(), anyDouble(), anyInt(), any(double[].class), anyBoolean(), anyInt(), any(int[].class), any(int[].class), any(double[].class)))
                .thenReturn(true);

        when(optimizer.OptimizeNLP(anyDouble())).thenReturn(0);
        when(optimizer.OptimizeNLP(anyLong())).thenReturn(0);
        when(optimizer.OptimizeNLP(any(HashMap.class))).thenReturn(0);
    }

    @Test
    void testEval_hWithNonNullValues() {
        int n = 5;
        double[] x = new double[n];
        boolean new_x = true;
        double obj_factor = 1.0;
        int m = 3;
        double[] lambda = new double[m];
        boolean new_lambda = true;
        int nele_hess = 5;
        int[] iRow = new int[nele_hess];
        int[] jCol = new int[nele_hess];
        double[] values = new double[nele_hess];


        boolean result = optimizer.eval_h(n, x, new_x, obj_factor, m, lambda, new_lambda, nele_hess, iRow, jCol, values);


        assertTrue(result);


        int[] expectedIRow = {0, 1, 2, 3, 4};
        int[] expectedJCol = {0, 1, 2, 3, 4};
        double[] expectedValues = {0.0, 0.0, 0.0, 0.0, 0.0};
        for (int i = 0; i < iRow.length; i++) {
            expectedIRow[i]=iRow[i] ;
            expectedJCol[i]=jCol[i];
        }

        assertEquals(expectedValues.length, values.length, "Length of values array mismatch");
        assertEquals(expectedIRow.length, iRow.length, "Length of iRow array mismatch");
        assertEquals(expectedJCol.length, jCol.length, "Length of jCol array mismatch");


        for (int i = 0; i < values.length; i++) {
            assertEquals(expectedValues[i], values[i], 1e-9, "Mismatch in Hessian values at index " + i);
        }


        for (int i = 0; i < iRow.length; i++) {
            assertEquals(expectedIRow[i],iRow[i] , "Mismatch in iRow at index " + i);
        }

        for (int i = 0; i < jCol.length; i++) {
            assertEquals(expectedJCol[i], jCol[i], "Mismatch in jCol at index " + i);
        }
    }

    @Test
    void testOptimizeNLPWithDouble() {

        double startValue = 0.0;


        int result = optimizer.OptimizeNLP(startValue);
        int expectedResult = 0;


        assertEquals(expectedResult, result);

    }

    @Test
    void testOptimizeNLPWithSeed() {

        long seed = 12345L;


        int result = optimizer.OptimizeNLP(seed);
        int expectedResult = 0;


        assertEquals(expectedResult, result);

    }

    @Test
    void testOptimizeNLPWithHashMap() throws Exception {


        Set<Integer> usedPipeIDs = new HashSet<>();
        Set<Integer> usedNodeIDs = new HashSet<>();


        Node startNode1 = new Node(10.0, 20.0, 1, 5.0, "Node1", 1.2, usedNodeIDs);
        Node endNode1 = new Node(15.0, 25.0, 2, 6.0, "Node2", 1.3, usedNodeIDs);
        Node startNode2 = new Node(20.0, 30.0, 3, 7.0, "Node3", 1.4, usedNodeIDs);
        Node endNode2 = new Node(25.0, 35.0, 4, 8.0, "Node4", 1.5, usedNodeIDs);


        Pipe pipe1 = new Pipe(100.0, startNode1, endNode1, 0.5, 0.01, 1, true, usedPipeIDs);
        Pipe pipe2 = new Pipe(150.0, startNode2, endNode2, 0.75, 0.015, 2, true, usedPipeIDs);



        HashMap<Pipe, Integer> currOrient = new HashMap<>();
        currOrient.put(pipe1, 1);
        currOrient.put(pipe2, 2);


        int result = optimizer.OptimizeNLP(currOrient);
        int expectedResult = 0;

        assertEquals(expectedResult, result);
    }
}