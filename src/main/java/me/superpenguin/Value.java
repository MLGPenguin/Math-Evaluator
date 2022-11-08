package me.superpenguin;

public class Value {

    private double val;

    public Value(int val){
        this.val = val;
    }

    public Value(double val){
        this.val = val;
    }

    public boolean isInt(){ return val%1 == 0; }
    public int intValue() { return (int)val; }
    public double doubleValue(){ return val; }

    @Override
    public String toString(){
        if (isInt()) return String.valueOf(intValue());
        else return String.valueOf(doubleValue());
    }

}
