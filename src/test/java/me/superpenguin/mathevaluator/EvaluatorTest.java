package me.superpenguin.mathevaluator;

import org.junit.Test;

import static org.junit.Assert.*;

public class EvaluatorTest {

    public Value eval(String exp) { return Evaluator.eval(exp); }
    
    private static double DELTA = 0.0001;

    @Test
    public void testSimpleIntExpressionsWithAdditionAndSubtraction() {
        assertEquals(eval("3+3").intValue(), 6);
        assertEquals(eval("3 + 3").intValue(), 6);
        assertEquals(eval("(3+3)").intValue(), 6);
        assertEquals(eval("3 + (3+3)").intValue(), 9);
        assertEquals(eval("3 + (3)").intValue(), 6);
        assertEquals(eval("4 - (3 + 4 - 4) + 4 - 6").intValue(), -1);
    }

    @Test
    public void testSimpleIntMultiplicationAndDivision() {
        assertEquals(eval("3 * 4").intValue(), 12);
        assertEquals(eval(" 3 * 4 / 2").intValue(), 6);
    }

    @Test
    public void testComplexIntSyntaxExpressionsAndOrderOfOperations() {
        assertEquals(eval("3 * 4 + 6 / 2").intValue(), 15);
        assertEquals(eval("3(4+2)/9").intValue(), 2);
    }

    @Test
    public void testDoubleAddAndMinus() {
        assertEquals(eval("3.54 + 54.2").doubleValue(), 57.74, DELTA);
        assertEquals(eval("50.3+4.6-2.8").doubleValue(), 52.1, DELTA);
    }

    @Test
    public void testDoubleTimesAndDivide() {
        assertEquals(eval("3.5*3").doubleValue(), 10.5, DELTA);
        assertEquals(eval("2.5*4.0/5").doubleValue(), 2.0, DELTA);
    }

    @Test
    public void testDoubleAdvancedSyntax() {
        assertEquals(eval("2.5(3.5+2.5) + 3.5").doubleValue(), 18.5, DELTA);
    }

    @Test
    public void testNegativeInts() {
        assertEquals(eval("-3 * 4 + 6 * -8").intValue(), -60);
    }

    @Test
    public void testNegativeDoubles() {
        assertEquals(eval("-2.5(3.5+2.5) + 2.5").doubleValue(), -12.5, DELTA);
    }

    @Test
    public void testExponentInts() {
        assertEquals(eval("10^3").intValue(), 1000);
        assertEquals(eval("(-10)^3").intValue(), -1000);
        assertEquals(eval("6 * 10^3/3").intValue(), 2000);
        assertEquals(eval("10^-2").doubleValue(), 0.01, DELTA);
    }

    @Test
    public void testExponentDoubles() {
        assertEquals(eval("2.5^3").doubleValue(), 15.625, DELTA);
        assertEquals(eval("-3.5*4^2.5+10^-2").doubleValue(), -111.99, DELTA);
    }

    private void assertInvalidSyntax(String syntax) { assertFalse(Evaluator.isValidSyntax(syntax)); }

    @Test
    public void testInvalidSyntax() {
        assertInvalidSyntax("HELLO 3+3");
        assertInvalidSyntax("HELLO 3++3");
        assertInvalidSyntax("43+");
        assertInvalidSyntax("43++4");
        assertInvalidSyntax("(43+4");
        assertInvalidSyntax("43+4)");
        assertInvalidSyntax("--4");
        assertInvalidSyntax("434+/3");
        assertInvalidSyntax("434//3");
    }


}