package me.superpenguin.mathevaluator;

import org.junit.Test;

import static org.junit.Assert.*;

public class EvaluatorTest {

    @Test
    public void testEvaluation() {
        assertEquals(Evaluator.eval("3+3").intValue(), 6);
    }

}