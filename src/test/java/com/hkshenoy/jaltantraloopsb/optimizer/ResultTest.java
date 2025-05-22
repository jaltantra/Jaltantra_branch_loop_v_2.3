package com.hkshenoy.jaltantraloopsb.optimizer;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResultTest {

    private Problem mockProblem;
    private Result result;

    @BeforeEach
    void setUp() {
        mockProblem = mock(Problem.class);
        result = new Result(mockProblem);
    }

    @Test
    void testGetPrimalValue() throws Exception {
        // Arrange
        String varName = "x";
        Double expectedValue = 10.0;
        when(mockProblem.getPrimalValue(varName)).thenReturn(expectedValue);

        // Act
        Double actualValue = result.getPrimalValue(varName);

        // Assert
        assertEquals(expectedValue, actualValue);
        verify(mockProblem, times(1)).getPrimalValue(varName);
    }

    @Test
    void testGetPrimalValueVariableNotFound() throws Exception {
        // Arrange
        String varName = "non_existent_var";
        when(mockProblem.getPrimalValue(varName)).thenReturn(null);

        // Act
        Double actualValue = result.getPrimalValue(varName);

        // Assert
        assertNull(actualValue);
        verify(mockProblem, times(1)).getPrimalValue(varName);
    }



    @Test
    void testGetObjectiveValueWhenProblemIsNull() {
        // Arrange
        result = new Result(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> result.getObjectiveValue());
    }
}



