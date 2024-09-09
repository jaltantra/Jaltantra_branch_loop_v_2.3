package com.hkshenoy.jaltantraloopsb.optimizer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EsrCostTest {

    private EsrCost esrCost;

    @BeforeEach
    public void setUp() {

        esrCost = new EsrCost(20, 30, 100, 10);
    }

    @Test
    public void testGetMinCapacity() {
        assertEquals(20, esrCost.getMinCapacity(), "Min Capacity should be 20");
    }

    @Test
    public void testGetMaxCapacity() {
        assertEquals(30, esrCost.getMaxCapacity(), "Max Capacity should be 30");
    }

    @Test
    public void testGetBaseCost() {
        assertEquals(100, esrCost.getBaseCost(), "Base Cost should be 100");
    }

}
