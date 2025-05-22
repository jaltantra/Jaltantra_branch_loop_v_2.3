package com.hkshenoy.jaltantraloopsb.optimizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

class ConstraintTest {

    private Linear linear;
    private Constraint constraint;

    @BeforeEach
    void setUp() {

        linear = new Linear();
        linear.linearVarNames = Arrays.asList("x1", "x2");
        linear.linearVarCoefficients = Arrays.asList(2.0, 3.0);


        constraint = new Constraint("c1", linear, "<=", 5.0);
    }

    @Test
    @Tag("fast")
    void testConstraintInitialization() {

        assertNotNull(constraint);
        assertEquals("c1", constraint.name);
        assertEquals(linear, constraint.linear);
        assertEquals("<=", constraint.sign);
        assertEquals(5.0, constraint.rhs);
    }

    @Test
    @Tag("fast")
    void testToString() {

        String expectedString = "2.0x1 + 3.0x2 <= 5.0\n";


        String actualString = constraint.toString();
        assertEquals(expectedString, actualString);
    }
}

