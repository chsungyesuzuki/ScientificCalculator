package chsungyesuzuki.scientificcalculator.base;

public interface RealNumber extends Number {
    public double toDouble ();
    public boolean moreThan (RealNumber a);
    public boolean lessThan (RealNumber a);
}
