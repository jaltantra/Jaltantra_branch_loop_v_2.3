package com.hkshenoy.jaltantraloopsb.optimizer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PipeCostTest {

    @Test
    public void testConstructorAndGetters() {
        double diameter = 100.0;
        double cost = 1500.0;
        double maxPressure = 20.0;
        double roughness = 0.5;

        PipeCost pipeCost = new PipeCost(diameter, cost, maxPressure, roughness);

        assertEquals(diameter, pipeCost.getDiameter(), "Diameter should match.");
        assertEquals(cost, pipeCost.getCost(), "Cost should match.");
        assertEquals(maxPressure, pipeCost.getMaxPressure(), "Max Pressure should match.");
        assertEquals(roughness, pipeCost.getRoughness(), "Roughness should match.");
    }

    @Test
    public void testCompareTo() {
        PipeCost pipeCost1 = new PipeCost(100.0, 1500.0, 20.0, 0.5);
        PipeCost pipeCost2 = new PipeCost(100.0, 1500.0, 15.0, 0.5);
        PipeCost pipeCost3 = new PipeCost(150.0, 2000.0, 20.0, 0.5);

        // Test comparison based on maxPressure
        assertTrue(pipeCost1.compareTo(pipeCost2) > 0, "pipeCost1 should be greater than pipeCost2.");
        assertTrue(pipeCost2.compareTo(pipeCost1) < 0, "pipeCost2 should be less than pipeCost1.");

        // Test comparison based on diameter when maxPressure is the same
        assertTrue(pipeCost1.compareTo(pipeCost3) < 0, "pipeCost1 should be less than pipeCost3.");
        assertTrue(pipeCost3.compareTo(pipeCost1) > 0, "pipeCost3 should be greater than pipeCost1.");

        // Test equality when both maxPressure and diameter are the same
        PipeCost pipeCost4 = new PipeCost(100.0, 1500.0, 20.0, 0.5);
        assertEquals(0, pipeCost1.compareTo(pipeCost4), "pipeCost1 should be equal to pipeCost4.");
    }
}
