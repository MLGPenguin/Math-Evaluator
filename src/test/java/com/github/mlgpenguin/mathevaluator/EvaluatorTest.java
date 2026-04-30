package com.github.mlgpenguin.mathevaluator;

import com.github.mlgpenguin.mathevaluator.tokeniser.Tokeniser;
import org.junit.Test;

import static java.lang.Math.sqrt;
import static org.junit.Assert.*;

public class EvaluatorTest {

    public Value eval(String exp) {
        return Evaluator.eval(exp);
    }
    
    private static final double DELTA = 5E-15;

    @Test
    public void testSimpleIntExpressionsWithAdditionAndSubtraction() {
        assertEquals(6, eval("3+3").intValue());
        assertEquals(6, eval("3 + 3").intValue());
        assertEquals(6, eval("(3+3)").intValue());
        assertEquals(9, eval("3 + (3+3)").intValue());
        assertEquals(6, eval("3 + (3)").intValue());
        assertEquals(-1, eval("4 - (3 + 4 - 4) + 4 - 6").intValue());
    }

    @Test
    public void testSimpleIntMultiplicationAndDivision() {
        assertEquals(12, eval("3 * 4").intValue());
        assertEquals(6, eval(" 3 * 4 / 2").intValue());
        assertEqualsDouble("1/3", 1/3.0);
        assertEqualsDouble("1/2", 0.5);
    }

    @Test
    public void testComplexIntSyntaxExpressionsAndOrderOfOperations() {
        assertEquals(15, eval("3 * 4 + 6 / 2").intValue());
        assertEquals(2, eval("3(4+2)/9").intValue());
    }

    @Test
    public void testDoubleAddAndMinus() {
        assertEquals(57.74, eval("3.54 + 54.2").doubleValue(), DELTA);
        assertEquals(52.1, eval("50.3+4.6-2.8").doubleValue(), DELTA);
    }

    @Test
    public void testDoubleTimesAndDivide() {
        assertEquals(10.5, eval("3.5*3").doubleValue(), DELTA);
        assertEquals(2.0, eval("2.5*4.0/5").doubleValue(), DELTA);
    }

    @Test
    public void testDoubleAdvancedSyntax() {
        assertEquals(18.5, eval("2.5(3.5+2.5) + 3.5").doubleValue(), DELTA);
    }

    @Test
    public void testNegativeInts() {
        assertEquals(-60, eval("-3 * 4 + 6 * -8").intValue());
    }

    @Test
    public void testNegativeDoubles() {
        assertEquals(-12.5, eval("-2.5(3.5+2.5) + 2.5").doubleValue(), DELTA);
    }

    @Test
    public void testExponentInts() {
        assertEquals(1000, eval("10^3").intValue());
        assertEquals(-1000, eval("(-10)^3").intValue());
        assertEquals(2000, eval("6 * 10^3/3").intValue());
        assertEquals(0.01, eval("10^-2").doubleValue(), DELTA);
    }

    @Test
    public void testExponentDoubles() {
        assertEquals(15.625, eval("2.5^3").doubleValue(), DELTA);
        assertEquals(-111.99, eval("-3.5*4^2.5+10^-2").doubleValue(), DELTA);
    }

    @Test
    public void testNegativeExponents() {
        assertEquals(1/9.0, eval("3^-2").doubleValue(), DELTA);
    }

    @Test
    public void testDoubleNegatives() {
        assertEquals(1.5, eval("-3.5--5").doubleValue(), DELTA);
        assertEquals(-4, eval("-4^(-3--4)").intValue());
        assertEquals(45, eval("50 +- 5").intValue());
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
        assertEqualsDouble("PI", Math.PI);
        assertEqualsInt("sin(pi)", 0);
        assertEqualsInt("cos(pi)", -1);
        assertEqualsDouble("radians(180)", Math.PI);
        assertEqualsInt("sin(radians(180))", 0);
        assertEqualsInt("sin(2pi)", 0);
        assertEqualsInt("degrees(pi)", 180);
        assertEqualsInt("deg(pi)", 180);
        assertEqualsInt("deg(2pi)", 360);
        assertEqualsInt("floor(pi)", 3);
        assertEqualsInt("floor(389.3)", 389);
        assertEqualsInt("ceil(pi)", 4);
        assertEqualsInt("CeIl(389.3)", 390);
        assertEqualsInt("abs(-3)", 3);
        assertEqualsDouble("sin(pi/4)", sqrt(2)/2.0);
    }

    public void assertEqualsDouble(String expression, double outcome) { assertEquals(outcome, eval(expression).doubleValue(), DELTA); }
    public void assertEqualsInt(String exp, int outcome) { assertEquals(outcome, eval(exp).intValue()); }

    @Test
    public void testDoublePrecision() {
        assertEqualsDouble("sin(pi/6)", 0.5);
        assertEqualsDouble("sin(rad(30))", 0.5);
        assertEqualsDouble("8sin(pi/6)", 4.0);
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
        assertThrows(Tokeniser.InvalidParserOperationException.class, () -> eval("sqrt(-1)"));
        assertThrows(Tokeniser.InvalidParserOperationException.class, () -> eval("sqrt(1-2)"));
    }

    @Test
    public void testModulos() {
        assertEqualsInt("10 % 3", 1);
        assertTrue(Evaluator.isValidSyntax("10% 3"));
    }

    @Test
    public void testLogs() {
        assertEqualsInt("log(10)", 1);
        assertEqualsInt("log(10^5)", 5);
        assertEqualsInt("log(10^9)", 9);
        assertEqualsInt("log(10^10)", 10);
        assertEqualsInt("log(0.1)", -1);
    }

    @Test
    public void testRandomThingsThatMayOrMayNotHaveOnceNotWorked() {
        assertEqualsInt("4 + ((0.1 + 0.2) - 0.3)", 4);
        assertEqualsInt("10/0", 0);
        assertEqualsInt("(8 ^ 2 - 7 ^ 2 -1) /2", 7);
        assertEqualsInt("(log(10^9) + sqrt(49))/2", 8);
        assertEqualsInt("log(10^9)+2cos(rad(0))", 11);
        assertEqualsInt("4.1-0.1", 4);
        assertEqualsInt("15.99999999999999999999999999999999999", 15);
        assertEqualsInt(" (1*1024/1024*2)^2/2", 2);
    }


    @Test
    public void testLongNumbers() {
        String foursixty = "561248351738538220655914513632724385477768371506049452782218533766935660365502422536370186651132086788786128526588826876394356980722973079218342051150553588919118013277167747992002923433569785920874507764202274006301467669494706512638280337884760465719948886793786982577035435612483517385382206559145136327243854777683715060494527822185337669356603655024225363701866511320867887861285265888268763943569807229730792183420511505535889191180132771677479920029234335697859";
        String longnum = "208745077642022740063014676694947065126382803378847604657199488867937869825770354356124835173.85382206559145136327243854777683715060494527822185337669356603655024225363701866511320867887861285265888268763943569807229730792183420511505535889191180132771677479920029234335697859418904484883280646965656152388845338444914516933489593642730494085118899235411019216857173329818808872266121865387041336498852087450776420227400630146766949470651263828033788476046571994888679378698257703543561248351738538220655914513632724385477768371506049452782218533766935660365502422536370186651132086788786128526588826876394356980722973079218342051150553588919118013277167747992002923433569785941890448488328064696565615238884533844491451693348959364273049408511889923541101921685717332981880887226612186538704133649885";
        assertEqualsDouble(foursixty + "*2", Double.parseDouble(foursixty) * 2);
        assertThrows(Util.InvalidSyntaxException.class, () -> eval(longnum +"*3"));
    }

    @Test
    public void testToString() {
        assertEquals("1", eval("1").toString());
        assertEquals("4", eval("4.1-0.1").toString());
        assertEquals("5.5", eval("5 + 0.5").toString());
    }

    @Test
    public void testBasicBinaryFunctions(){
        assertEqualsInt("1 | 2", 3);
        assertEqualsInt("1 | 2 * 3", 9);
        assertEqualsInt("1 & 3", 1);
        assertEqualsInt("1 & 2", 0);
        assertEqualsInt("1 & 3 & 2", 0);
        assertEqualsInt("1 & 3 * 4", 4);
    }

    @Test
    public void testFractionalParts() {
        assertEqualsDouble("9*(1+(1/3))", 12);
    }

    @Test
    public void testShiftOperators() {
        assertEqualsInt("8>>1/4", 1);
        assertEqualsInt("8 >> 2", 2);
        assertEqualsInt("1 << 3", 8);
        assertEqualsInt("2 << 2", 8);
        assertEqualsInt("(2 + 2) << 1", 8);
        assertEqualsInt("16 >> 2 + 1", 5); // >> has higher precedence than +, so: (16>>2)+1 = 4+1 = 5
    }
}