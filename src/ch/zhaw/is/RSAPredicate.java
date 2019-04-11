package ch.zhaw.is;

import java.math.BigInteger;
import java.util.function.Predicate;

final class RSAPredicate implements Predicate<BigInteger> {

    private final BigInteger lowerBound;
    private final BigInteger upperBound;

    RSAPredicate(BigInteger lowerBound, BigInteger upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * Test for RSA compliance.
     *
     * @param value non-null BigInteger
     * @return {@code true} if input is a valid RSA value
     */
    @Override
    public boolean test(BigInteger value) {
        return value.compareTo(lowerBound) > 0
                && value.compareTo(upperBound) < 0;
    }
}
