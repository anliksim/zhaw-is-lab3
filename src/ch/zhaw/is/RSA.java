package ch.zhaw.is;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;

interface RSA {

        // Exercise 3

    BigInteger encrypt(BigInteger plain) throws BadMessageException;

    BigInteger decrypt(BigInteger cipher) throws BadMessageException;

    void save(ObjectOutputStream os) throws IOException;

    void savePublic(ObjectOutputStream os) throws IOException;

    // Exercise 10

    BigInteger sign(BigInteger message) throws BadMessageException;

    boolean verify(BigInteger message, BigInteger signature);
}
