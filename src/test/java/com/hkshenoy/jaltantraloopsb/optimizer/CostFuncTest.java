package com.hkshenoy.jaltantraloopsb.optimizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CostFuncTest {

    private final CostFunc costFunc = new CostFunc();
    private static final double DELTA = 0.01; // Allowable difference for precision up to 2 decimal places

    @Test
    void testValue() {
        double x = 2.0;
        double[] parameters = {1.0, 2.0, 3.0};

        // Expected value calculation: 1.0 * 2.0^2.0 + 3.0 = 4.0 + 3.0 = 7.0
        double expected = 7.0;
        double actual = costFunc.value(x, parameters);

        assertEquals(expected, actual, DELTA, "The value method did not return the expected result.");
    }

    @Test
    void testGradient() {
        double x = 2.0;
        double[] parameters = {1.0, 2.0, 3.0};

        // Expected gradient calculation:
        // g[0] = 2.0^2.0 = 4.0
        // g[1] = 1.0 * 2.0^2.0 * log(2.0) ≈ 4.0 * 0.693 ≈ 2.77
        // g[2] = 1.0
        double[] expected = {4.0, 2.77, 1.0};
        double[] actual = costFunc.gradient(x, parameters);

        assertEquals(expected[0], actual[0], DELTA, "The first gradient component did not match the expected value.");
        assertEquals(expected[1], actual[1], DELTA, "The second gradient component did not match the expected value.");
        assertEquals(expected[2], actual[2], DELTA, "The third gradient component did not match the expected value.");
    }
}
