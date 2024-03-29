package com.github.mlgpenguin.mathevaluator;

public class Value {

    private static final double RECOMMENDED_DELTA = 5E-16;

    private double val;

    public Value(int val){
        this.val = val;
    }

    public Value(double val){
        this.val = val;
    }

    public boolean isInt(){ return val%1 == 0; }
    public int intValue() { return (int)val; }
    public int roundToInt() { return (int) Math.round(val); }
    public double doubleValue(){ return val; }

    /**
     * Checks if the value is equal too, or within the provided delta of this value.
     * @param number The number to compare this value to
     * @param delta The accepted variance between this value and the other number.
     * @return true if this value is within the provided delta of the other number.
     */
    public boolean almostEquals(double number, double delta) {
        return val == number || Math.abs(val-number) < delta;
    }

    /** @see #almostEquals(double, double) */
    public boolean almostEquals(int number, double delta) { return almostEquals(Double.valueOf(number), delta); }

    @Override
    public String toString(){
        if (isInt()) return String.valueOf(intValue());
        else if (isWithinDeltaOfWholeNumber(RECOMMENDED_DELTA)) return String.valueOf(roundToInt());
        else return String.valueOf(doubleValue());
    }

    private boolean isWithinDeltaOfWholeNumber(double delta) {
        return Math.abs(val % 1) < delta || 1 - Math.abs(val % 1) < delta;
    }

}
