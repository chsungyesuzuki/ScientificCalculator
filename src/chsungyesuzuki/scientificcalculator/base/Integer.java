package chsungyesuzuki.scientificcalculator.base;

import chsungyesuzuki.scientificcalculator.exception.ObjectTooLargeException;

import java.util.Arrays;

public class Integer implements RationalNumber {

    private byte[] value;
    private boolean signal;

    public Integer () {
        value = new byte [1];
        signal = true;
    }

    public void setLength (final int length) {
        if (value == null) {
            value = new byte [length];
        }
        else if (value.length < length) {
            byte[] tmp = new byte [length];
            System.arraycopy (value, 0, tmp, 0, value.length);
            value = tmp;
        }
        else if (value.length > length) {
            value = Arrays.copyOf (value, length);
        }
    }

    public Integer (final long a) {
        long copyOfA = a;
        byte length = (byte) Math.ceil(Math.log(a));
        value = new byte [length];
        for (byte i = 0; i != length; i ++) {
            value [i] = (byte) (copyOfA % 10);
            copyOfA /= 10;
        }
        if (a < 0) {
            signal = false;
        }
        else {
            signal = true;
        }
    }

    public Integer (final byte[] value) {
        this.value = value;
        simplify ();
    }

    @Override
    public double toDouble () {
        return toLong ();
    }

    @Override
    public boolean moreThan (RealNumber a) {
        simplify ();
        a.simplify ();
        if (a instanceof Integer) {
            if (((Integer) a).signal != signal) {
                return signal;
            }
            if (((Integer) a).value.length != value.length) {
                return value.length > ((Integer) a).value.length;
            }
            else {
                for (int i = value.length - 1; i >= 0; i --) {
                    if (value [i] > ((Integer) a).value [i]) {
                        return true;
                    }
                    else if (value [i] < ((Integer) a).value [i]) {
                        return false;
                    }
                }
                return false;
            }
        }
        else {
            return a.lessThan (this);
        }
    }

    @Override
    public boolean lessThan (RealNumber a) {
        if (equals (a)) {
            return false;
        }
        else {
            return !moreThan(a);
        }
    }

    public long toLong () throws ObjectTooLargeException {
        if (moreThan (new Integer (Long.MAX_VALUE))) {
            throw new ObjectTooLargeException ("value is bigger than Long.MAX_VALUE");
        }
        long result = 0;
        simplify ();
        for (byte i = 0; i != value.length; i ++) {
            result += value [i] * Math.pow (10, i);
        }
        return result;
    }

    @Override
    public Number add (Number addend) {
        if (!(addend instanceof Integer)) {
            return addend.add (this);
        }
        simplify ();
        addend.simplify ();
        if (signal != ((Integer) addend).signal) {
            Integer subtractor;
            Integer minuend;
            if (signal) {
                subtractor = this;
                minuend = (Integer) addend;
            }
            else {
                subtractor = (Integer) addend;
                minuend = this;
            }
            ((Integer) minuend).signal = true;
            Integer res = (Integer) subtractor.subtract(minuend);
            ((Integer) minuend).signal = false;
            return res;
        }
        int bigger = Math.max (value.length, ((Integer) addend).value.length);
        int smaller = Math.min (value.length, ((Integer)addend).value.length);
        Integer big;
        if (value.length < ((Integer) addend).value.length) {
            big = (Integer) addend;
        }
        else {
            big = this;
        }
        Integer res = new Integer ();
        res.setLength (bigger + 1);
        byte x = 0;
        for (int i = 0; i != bigger; i ++) {
            byte sum;
            if (i < smaller) {
                sum = (byte) (value [i] + ((Integer) addend).value [i] + x);
            }
            else {
                sum = (byte) (big.value [i] + x);
            }
            x = (byte) (sum % 10);
            sum /= 10;
            res.value [i] = sum;
        }
        res.value [bigger] = x;
        if ((!signal) && (!((Integer) addend).signal)) {
            res.signal = false;
        }
        else {
            res.signal = true;
        }
        res.simplify ();
        return res;
    }

    @Override
    public Number subtract (Number minuend) {
        return null;
    }

    @Override
    public Number multiply (Number multiplier) {
        return null;
    }

    @Override
    public Number divide (Number dividend) {
        return null;
    }

    @Override
    public Number getPower (Number exp) {
        return null;
    }

    @Override
    public Number getRoot (Number index) {
        return null;
    }

    @Override
    public boolean equals (Number a) {
        simplify ();
        a.simplify ();
        if ((!(a instanceof Integer)) || (((Integer) a).value.length != value.length) || ((Integer) a).signal != signal) {
            return false;
        }
        for (int i = value.length - 1; i >= 0; i --) {
            if (value [i] != ((Integer) a).value [i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void simplify () {
        if (value.length == 1 && value [0] == 0) {
            signal = true;
            return;
        }
        int i = value.length - 1;
        for (; i >= 0; i --) {
            if (value [i] != 0) {
                break;
            }
        }
        value = Arrays.copyOf (value, i + 1);
    }
}
