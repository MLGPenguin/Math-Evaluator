package com.github.mlgpenguin.mathevaluator;

import java.math.BigDecimal;

public class Value {

    private final BigDecimal val;

    public Value(String value) { this.val = new BigDecimal(value); }
    public Value(int value){
        this.val = new BigDecimal(value);
    }
    public Value(double value){
        this.val = new BigDecimal(value);
    }
    public Value(BigDecimal value) { this.val = value; }

    public boolean isInt(){ return val.remainder(BigDecimal.ONE).doubleValue() == 0.0; }
    public int intValue() { return val.intValue(); }
    public int roundToInt() { return (int) Math.round(val.doubleValue()); }
    public double doubleValue(){ return val.doubleValue(); }
    public BigDecimal getBigDecimal() { return val; }

    @Override
    public String toString() {
        if (isInt()) return String.valueOf(intValue());
        else return String.valueOf(doubleValue());
    }

}
