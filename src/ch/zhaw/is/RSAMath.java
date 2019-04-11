package ch.zhaw.is;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;

final class RSAMath {

    private RSAMath() {
        throw new AssertionError("utility constructor");
    }

    /**
     * Finds a value e so that gcd(e, phi(n)) = 1.
     *
     * @param phi phi of n
     * @param e0  start value for e
     * @return value for e
     */
    static BigInteger chooseE(BigInteger phi, BigInteger e0) {
        BigInteger e = e0;
        while (e.gcd(phi).compareTo(ONE) > 0) {
            e = e.add(ONE);
        }
        return e;

    }

    /**
     * For (semi-)primes, computing the Euler totient function is equivalent to factoring.
     * Indeed, if n = pq for distinct primes p and q, then
     * φ(n) = (p-1)(q-1) = pq - (p+q) + 1 = (n+1) - (p+q).
     */
    static BigInteger eulerTotient(BigInteger n, BigInteger p, BigInteger q) {
        return n.add(ONE).subtract(p.add(q));
    }
}
