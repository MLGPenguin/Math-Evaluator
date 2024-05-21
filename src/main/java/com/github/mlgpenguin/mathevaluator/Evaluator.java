package com.github.mlgpenguin.mathevaluator;

import com.github.mlgpenguin.mathevaluator.tokeniser.Tokeniser;

/**
 * API Class for the evaluator, written in Java for optimal compabibility.
 */
public class Evaluator {

    private static final Util util = Util.INSTANCE;

    /**
     * @param expression the expression to evaluate
     * @throws Util.InvalidSyntaxException if the expression is improperly formatted
     * @return a value representing the output of the simplified expression
     */
    public static Value eval(String expression) {
        return Tokeniser.INSTANCE.evaluateValue(expression);
    }

    public static boolean isValidSyntax(String exp) { return util.isValidSyntax(exp); }

}
