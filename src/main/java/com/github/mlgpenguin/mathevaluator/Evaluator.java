package com.github.mlgpenguin.mathevaluator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * API Class for the evaluator, written in Java for optimal compabibility.
 *
 */
public class Evaluator {

    public static class InvalidSyntaxException extends RuntimeException {
        public InvalidSyntaxException(String msg) { super(msg); }
        public InvalidSyntaxException() { super(""); }
    }

    private static final Util util = Util.INSTANCE;

    private static final String NUMBER_REGEX = "-?[0-9]+\\.?[0-9]*";

    /**
     *
     * @param expression
     * @throws InvalidSyntaxException if the expression is improperly formatted
     * @return
     */
    public static Value eval(String expression) {
        String exp = util.sanitise(expression);
        if (!util.isValidSyntax(exp)) throw new InvalidSyntaxException();

        exp = calcualtePrefixFunctions(exp);
        exp = calculateBrackets(exp);
        exp = calculate(exp, "\\%");
        exp = calculate(exp, "\\^");
        exp = calculate(exp, "\\*|/");
        exp = calculate(exp, "-|\\+");
        double result;
        try {
            result = Double.parseDouble(exp);
        } catch (NumberFormatException ex) {
            throw new InvalidSyntaxException();
        }
        return new Value(result);
    }

    public static boolean isValidSyntax(String exp) { return util.isValidSyntax(exp); }

    private static String operate(String n1, String operator, String n2){
        double val;
        double a1 = Double.parseDouble(n1);
        double a2 = Double.parseDouble(n2);
        switch (operator) {
            case "+": val = a1+a2; break;
            case "-": val = a1-a2; break;
            case "*": val = a1*a2; break;
            case "/": val = a1/a2; break;
            case "^": val = Math.pow(a1, a2); break;
            case "%": val = a1%a2; break;
            default: val = 0;
        };
        return new Value(val).toString();
    }

    private static String operate(Matcher m){
        return operate(m.group(1), m.group(2), m.group(3));
    }

    private static String calculate(String expression, String operators){
        Pattern p = Pattern.compile("(" + NUMBER_REGEX + ")(" + operators + ")(" + NUMBER_REGEX + ")");
        Matcher m = p.matcher(expression);
        while (m.find()){
            expression = m.replaceFirst(operate(m));
            m = p.matcher(expression);
        }
        return expression;
    }

    private static final Pattern brackets = Pattern.compile("\\(([^()]+)\\)");
    private static String calculateBrackets(String exp){
        Matcher m = brackets.matcher(exp);
        while(m.find()){
            exp = m.replaceFirst(Evaluator.eval(m.group(1)).toString());
            m = brackets.matcher(exp);
        }
        return exp;
    }

    private static final Pattern prefixFunctions = Pattern.compile("(?i)([a-z]+)\\((.+)\\)");
    private static String calcualtePrefixFunctions(String exp) {
        Matcher m = prefixFunctions.matcher(exp);
        while(m.find()){
            exp = m.replaceFirst(String.valueOf(util.parsePrefixFunction(m.group(1), Evaluator.eval(m.group(2)).doubleValue())));
            m = prefixFunctions.matcher(exp);
        }
        return exp;
    }

}
