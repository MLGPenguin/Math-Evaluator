package com.github.mlgpenguin.mathevaluator;

import org.junit.Test;

import static org.junit.Assert.*;

public class EvaluatorTest {

    public Value eval(String exp) { return Evaluator.eval(exp); }
    
    private static final double DELTA = 0.0001;

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

    @Test
    public void testNegativeExponents() {
        assertEquals(eval("3^-2").doubleValue(), 1/9.0, DELTA);
    }

    @Test
    public void testDoubleNegatives() {
        assertEquals(eval("-3.5--5").doubleValue(), 1.5, DELTA);
        assertEquals(eval("-4^(-3--4)").intValue(), -4);
        assertEquals(eval("50 +- 5").intValue(), 45);
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
        assertInvalidSyntax("50 --- 5");
        assertInvalidSyntax("siin(4)");
    }

    @Test
    public void testPrefixFunctions() {
        assertEquals(eval("PI").doubleValue(), Math.PI, DELTA);
        assertEquals(eval("sin(pi)").intValue(), 0);
        assertEquals(eval("cos(pi)").intValue(), -1);
        assertEquals(eval("radians(180)").doubleValue(), Math.PI, DELTA);
        assertEquals(eval("sin(radians(180))").intValue(), 0);
        assertEquals(eval("sin(2pi)").intValue(), 0);
        assertEqualsInt("degrees(pi)", 180);
        assertEqualsInt("deg(pi)", 180);
        assertEqualsInt("deg(2pi)", 360);
        assertEqualsInt("floor(pi)", 3);
        assertEqualsInt("floor(389.3)", 389);
        assertEqualsInt("ceil(pi)", 4);
        assertEqualsInt("CeIl(389.3)", 390);
        assertEqualsInt("abs(-3)", 3);
    }

    public void assertEqualsDouble(String expression, double outcome) { assertEquals(eval(expression).doubleValue(), outcome, DELTA); }
    public void assertEqualsInt(String exp, int outcome) { assertEquals(eval(exp).intValue(), outcome); }

    private static final double FINE_DELTA = 1E-15;

    @Test
    public void testDoublePrecision() {
        assertTrue(eval("sin(pi/6)").almostEquals(0.5, FINE_DELTA));
        assertTrue(eval("sin(rad(30))").almostEquals(0.5, FINE_DELTA));
        assertTrue(eval("8sin(pi/6)").almostEquals(4.0, FINE_DELTA));
    }

    @Test
    public void testPrefixFunctionsAdvanced() {
        assertEqualsDouble("-2.5^cos(-3^1-1+4sin(pi/2))", -2.5);
        assertEqualsDouble("-2.5^cos(-3^1-1+4sin(pi - radians(90)))", -2.5);
        assertEqualsDouble("-2.5^cos(-3^1-1+4sin(pi - rad(90)))", -2.5);
        assertEqualsDouble("4cos(pi)/4", -1);
        assertEqualsDouble("sqrt(144)", 12);
        assertEqualsDouble("sqrt(12.5^2)", 12.5);
    }

    @Test
    public void testSuffixSanitisationOperators() {
        assertEqualsInt("3!", 6);
        assertEqualsInt("1!", 1);
        assertEqualsInt("0!", 1);
        assertEqualsInt("4!", 24);
        assertEqualsInt("3*4!", 72);
        assertEqualsInt("4!/2", 12);
        assertEqualsInt("10^2!", 100);
    }

    @Test
    public void modulo() {
        assertEqualsInt("6%3", 0);
        assertEqualsInt("6%6", 0);
        assertEqualsInt("6%2", 0);
        assertEqualsInt("6%4", 2);
        assertEqualsInt("6%4", 2);
        assertEqualsDouble("6.5%4", 2.5);
    }

    @Test
    public void round() {
        assertEqualsInt("round(3.5)", 4);
        assertEqualsInt("round(3.2)", 3);
        assertEqualsInt("round(3.6)", 4);
        assertEqualsInt("round(-3.5)", -4);
        assertEqualsInt("round(-3.4)", -3);
    }

    @Test
    public void invalidPrefixFunctionExpressions() {
        assertThrows(Evaluator.InvalidSyntaxException.class, () -> eval("sqrt(-1)"));
        assertThrows(Evaluator.InvalidSyntaxException.class, () -> eval("sqrt(1-2)"));
    }
}