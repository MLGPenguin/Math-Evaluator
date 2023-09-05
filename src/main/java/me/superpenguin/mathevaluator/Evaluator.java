package me.superpenguin.mathevaluator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Evaluator {

    public static Value eval(String expression){
        String exp = expression.replaceAll(" ", "");
        exp = calculateBrackets(exp);
        exp = calculate(exp, "\\^");
        exp = calculate(exp, "\\*|/");
        exp = calculate(exp, "-|\\+");
        return new Value(Double.parseDouble(exp));
    }

    private static String operate(String n1, String operator, String n2){
        double val;
        double a1 = Double.parseDouble(n1);
        double a2 = Double.parseDouble(n2);
        switch (operator) {
            case "+": val = a1+a2; break;
            case "-": val = a1-a2; break;
            case "*": val = a1*a2; break;
            case "/": val = (double)a1/a2; break;
            case "^": val = Math.pow(a1, a2); break;
            default: val = 0;
        };
        return new Value(val).toString();
    }

    private static String operate(Matcher m){
        return operate(m.group(1), m.group(2), m.group(3));
    }

    private static String calculate(String expression, String operators){
        Pattern p = Pattern.compile("([0-9]+\\.?[0-9]*)(" + operators + ")([0-9]+\\.?[0-9]*)");
        Matcher m = p.matcher(expression);
        while (m.find()){
            expression = m.replaceFirst(operate(m));
            m = p.matcher(expression);
        }
        return expression;
    }

    private static String calculateBrackets(String exp){
        Pattern p = Pattern.compile("\\(([^()]+)\\)");
        Matcher m = p.matcher(exp);
        while(m.find()){
            exp = m.replaceFirst(Evaluator.eval(m.group(1)).toString());
            m = p.matcher(exp);
        }
        return exp;
    }


}
