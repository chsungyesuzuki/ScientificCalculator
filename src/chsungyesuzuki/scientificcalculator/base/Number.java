package chsungyesuzuki.scientificcalculator.base;

public interface Number {
    public Number add (Number addend);
    public Number subtract (Number minuend);
    public Number multiply (Number multiplier);
    public Number divide (Number dividend);
    public Number getPower (Number exp);
    public Number getRoot (Number index);
    public String toString ();
}
