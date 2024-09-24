package com.hkshenoy.jaltantraloopsb.optimizer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.ortools.linearsolver.MPSolver.ResultStatus;

class ProblemTest {
    private Problem problem;

    @BeforeEach
    void setUp() {
        problem = new Problem();
    }

    @Test
    void testSetVarTypeBoolean() {
        assertDoesNotThrow(() -> problem.setVarType("booleanVar", Boolean.class));
    }

    @Test
    void testSetVarTypeDouble() {
        assertDoesNotThrow(() -> problem.setVarType("doubleVar", Double.class));
    }

    @Test
    void testSetVarLowerBound() {
        assertDoesNotThrow(() -> {
            problem.setVarType("testVar", Double.class);
            problem.setVarLowerBound("testVar", 10.0);
        });
    }

    @Test
    void testSetVarUpperBound() {
        assertDoesNotThrow(() -> {
            problem.setVarType("testVar", Double.class);
            problem.setVarUpperBound("testVar", 100);
        });
    }

    @Test
    void testSetObjective() {
        assertDoesNotThrow(() -> {
            Linear linear = new Linear();
            linear.add(1.0, "testVar1");
            linear.add(2.0, "testVar2");
            problem.setVarType("testVar1", Double.class);
            problem.setVarType("testVar2", Double.class);
            problem.setObjective(linear, true);
        });
    }

    @Test
    void testAddConstraint() {
        assertDoesNotThrow(() -> {
            Linear linear = new Linear();
            linear.add(1.0, "testVar1");
            linear.add(2.0, "testVar2");
            problem.setVarType("testVar1", Double.class);
            problem.setVarType("testVar2", Double.class);
            Constraint constraint = new Constraint("testConstraint", linear, "<=", 10.0);
            problem.add(constraint);
        });
    }


    @Test
    void testGetConstraintsCount() {
        assertEquals(0, problem.getConstraintsCount());
        assertDoesNotThrow(() -> {
            Linear linear = new Linear();
            linear.add(1.0, "testVar1");
            linear.add(2.0, "testVar2");
            problem.setVarType("testVar1", Double.class);
            problem.setVarType("testVar2", Double.class);
            Constraint constraint = new Constraint("testConstraint", linear, "<=", 10.0);
            problem.add(constraint);
            assertEquals(1, problem.getConstraintsCount());
        });
    }

    @Test
    void testSetTimeLimit() {
        assertDoesNotThrow(() -> problem.setTimeLimit(1000));
    }

    @Test
    void testGetTimeTaken() {
        assertEquals(0, problem.getTimeTaken(), 0.001);
    }



}
