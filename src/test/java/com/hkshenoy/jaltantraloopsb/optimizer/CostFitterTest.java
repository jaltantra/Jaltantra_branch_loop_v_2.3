package com.hkshenoy.jaltantraloopsb.optimizer;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CostFitterTest {

    @Test
    public void testCostFitter() {

        List<WeightedObservedPoint> points = new ArrayList<>();
        points.add(new WeightedObservedPoint(1.0, 1.0, 2.0));
        points.add(new WeightedObservedPoint(1.0, 2.0, 4.5));
        points.add(new WeightedObservedPoint(1.0, 3.0, 7.5));


        CostFitter costFitter = new CostFitter();


        LeastSquaresProblem problem = costFitter.getProblem(points);


        Evaluation evaluation = problem.evaluate(problem.getStart());


        assertEquals(points.size(), evaluation.getResiduals().getDimension());


        RealVector start = evaluation.getPoint();
        assertEquals(3, start.getDimension());
        assertEquals(1.0, start.getEntry(0));
        assertEquals(1.0, start.getEntry(1));
        assertEquals(1.0, start.getEntry(2));


        double[] residuals = evaluation.getResiduals().toArray();
        assertEquals(3, residuals.length);


    }
}

