package com.hkshenoy.jaltantraloopsb.optimizer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinearTest {
    private Linear linear;

    @BeforeEach
    void setUp() {
        linear = new Linear();
    }

    @Test
    void testAdd() {

        linear.add(2.5, "x1");
        assertEquals(1, linear.linearVarNames.size());
        assertEquals(1, linear.linearVarCoefficients.size());
        assertEquals("x1", linear.linearVarNames.get(0));
        assertEquals(2.5, linear.linearVarCoefficients.get(0));


        linear.add(3.0, "x2");
        assertEquals(2, linear.linearVarNames.size());
        assertEquals(2, linear.linearVarCoefficients.size());
        assertEquals("x2", linear.linearVarNames.get(1));
        assertEquals(3.0, linear.linearVarCoefficients.get(1));
    }

    @Test
    void testAddMultiple() {

        linear.add(1.0, "y1");
        linear.add(4.5, "y2");
        linear.add(-2.3, "y3");

        assertEquals(3, linear.linearVarNames.size());
        assertEquals(3, linear.linearVarCoefficients.size());

        assertEquals("y1", linear.linearVarNames.get(0));
        assertEquals(1.0, linear.linearVarCoefficients.get(0));

        assertEquals("y2", linear.linearVarNames.get(1));
        assertEquals(4.5, linear.linearVarCoefficients.get(1));

        assertEquals("y3", linear.linearVarNames.get(2));
        assertEquals(-2.3, linear.linearVarCoefficients.get(2));
    }

    @Test
    void testEmptyLinear() {

        assertTrue(linear.linearVarNames.isEmpty());
        assertTrue(linear.linearVarCoefficients.isEmpty());
    }
}

