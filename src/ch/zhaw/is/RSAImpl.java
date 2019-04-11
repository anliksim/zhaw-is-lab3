package ch.zhaw.is;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import static java.math.BigInteger.ONE;

final class RSAImpl implements RSA {

    private final BigInteger n;
    private final BigInteger e;
    private final BigInteger d;

    private final Predicate<BigInteger> validRSA;

    // Generates random RSA key pair
    RSAImpl() {
        BigInteger p = BigInteger.probablePrime(1015, ThreadLocalRandom.current());
        BigInteger q = BigInteger.probablePrime(1033, ThreadLocalRandom.current());
        this.n = p.multiply(q);
        BigInteger phi = RSAMath.eulerTotient(n, p, q);
        this.e = RSAMath.chooseE(phi, BigInteger.valueOf(3));
        this.d = e.modInverse(phi);
        validRSA = new RSAPredicate(ONE, n.subtract(ONE));
    }


    // Reads public key or key pair from input stream
    RSAImpl(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.n = readBigInt(in);
        this.e = readBigInt(in);
        this.d = readBigInt(in);
        validRSA = new RSAPredicate(ONE, n.subtract(ONE));
    }

    private static BigInteger readBigInt(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        BigInteger value;
        try {
            value = (BigInteger) inputStream.readObject();
        } catch (EOFException e) {
            value = null;
        }
        return value;
    }

    private static void writeBigInt(ObjectOutputStream out, BigInteger value) throws IOException {
        out.writeObject(value);
    }


    @Override
    public BigInteger encrypt(BigInteger plain) throws BadMessageException {
        if (validRSA.test(plain)) {
            return plain.modPow(e, n);
        } else {
            throw new BadMessageException("Plain value not between 1 and n−1");
        }
    }

    @Override
    public BigInteger decrypt(BigInteger cipher) throws BadMessageException {
        checkPrivateKey();
        if (validRSA.test(cipher)) {
            return cipher.modPow(d, n);
        } else {
            throw new BadMessageException("Cipher not between 1 and n−1");
        }
    }

    @Override
    public void save(ObjectOutputStream os) throws IOException {
        checkPrivateKey();
        writeBigInt(os, n);
        writeBigInt(os, e);
        writeBigInt(os, d);
    }

    @Override
    public void savePublic(ObjectOutputStream os) throws IOException {
        writeBigInt(os, n);
        writeBigInt(os, e);
    }

    @Override
    public BigInteger sign(BigInteger message) throws BadMessageException {
        checkPrivateKey();
        return decrypt(message);
    }

    @Override
    public boolean verify(BigInteger message, BigInteger signature) {
        boolean valid;
        try {
            valid = message.compareTo(this.encrypt(signature)) == 0;
        } catch (BadMessageException e) {
            valid = false;
        }
        return valid;
    }

    private void checkPrivateKey() {
        if (d == null) {
            throw new OperationNotSupportedError("Private key does not exist");
        }
    }
}
