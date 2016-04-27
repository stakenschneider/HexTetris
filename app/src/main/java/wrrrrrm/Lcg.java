package wrrrrrm;

import java.math.BigInteger;


public class Lcg {

    private final BigInteger a = BigInteger.valueOf(1103515245);  // Multiplier
    private final BigInteger b = BigInteger.valueOf(12345);  // Increment
    private final BigInteger m = BigInteger.ONE.shiftLeft(32);  // Modulus
    private final BigInteger aInv;

    private BigInteger x;

    public Lcg( BigInteger seed) {
        if (a == null || b == null || m == null || seed == null)
            throw new NullPointerException();
        if (a.signum() != 1 || b.signum() == -1 || m.signum() != 1 || seed.signum() == -1 || seed.compareTo(m) >= 0)
            throw new IllegalArgumentException("Arguments out of range");


        this.aInv = a.modInverse(m);
        this.x = seed;
    }

    public BigInteger getState() {return x;}

    public void next() {x = x.multiply(a).add(b).mod(m);}

    public void previous() {x = x.subtract(b).multiply(aInv).mod(m);}

    public void skip(int n) {
        if (n >= 0)
            skip(a, b, BigInteger.valueOf(n));
        else
            skip(aInv, aInv.multiply(b).negate(), BigInteger.valueOf(n).negate());
    }

    private void skip(BigInteger a, BigInteger b, BigInteger n) {
        BigInteger a1 = a.subtract(BigInteger.ONE);
        BigInteger ma = a1.multiply(m);
        BigInteger y = a.modPow(n, ma).subtract(BigInteger.ONE).divide(a1).multiply(b);
        BigInteger z = a.modPow(n, m).multiply(x);
        x = y.add(z).mod(m);
    }

}