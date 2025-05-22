package com.hkshenoy.jaltantraloopsb.optimizer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UtilTest {

    @Test
    void testHWheadLossWithLength() {
        double length = 1000; // in meters
        double flow = 50; // in liters per second
        double HWConstant = 130;
        double diameter = 300; // in millimeters

        double result = Util.HWheadLoss(length, flow, HWConstant, diameter);
        double expected = 10.68 * length * Math.pow((Math.abs(flow) * 0.001) / HWConstant, 1.852) / Math.pow((diameter / 1000), 4.87);

        assertEquals(expected, result, 1e-6);
    }

    @Test
    void testHWheadLossWithoutLength() {
        double flow = 50; // in liters per second
        double HWConstant = 130;
        double diameter = 300; // in millimeters

        double result = Util.HWheadLoss(flow, HWConstant, diameter);
        double expected = 10.68 * Math.pow(Math.abs((flow * 0.001)) / HWConstant, 1.852) / Math.pow((diameter / 1000), 4.87);

        assertEquals(expected, result, 1e-6);
    }

    @Test
    void testRound() {
        assertEquals(123.46, Util.round(123.4567, 2));
        assertEquals(123.45, Util.round(123.4547, 2));
        assertEquals(-123.46, Util.round(-123.4567, 2));
        assertEquals(0, Util.round(0, 2));
    }

    @Test
    void testWaterSpeed() {
        double flow = 50; // in liters per second
        double diameter = 300; // in millimeters

        double result = Util.waterSpeed(flow, diameter);
        double expected = (flow * 4000) / (Math.PI * diameter * diameter);

        assertEquals(expected, result, 1e-6);
    }

    @Test
    void testPresentValueFactorDifferentRates() {
        double discountrate = 10; // in percent
        double inflationrate = 5; // in percent
        int time = 10; // in years

        double result = Util.presentValueFactor(discountrate, inflationrate, time);
        double rate = (1 + inflationrate / 100) / (1 + discountrate / 100);
        double expected = (Math.pow(rate, time) - 1) / (rate - 1);

        assertEquals(expected, result, 1e-6);
    }

    @Test
    void testPresentValueFactorSameRates() {
        double discountrate = 5; // in percent
        double inflationrate = 5; // in percent
        int time = 10; // in years

        double result = Util.presentValueFactor(discountrate, inflationrate, time);

        assertEquals(time, result, 1e-6);
    }

    @Test
    void testPresentValueFactorZeroYears() {
        double discountrate = 5; // in percent
        double inflationrate = 5; // in percent
        int time = 0; // in years

        double result = Util.presentValueFactor(discountrate, inflationrate, time);

        assertEquals(0, result, 1e-6);
    }
}

