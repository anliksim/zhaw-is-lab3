package ch.zhaw.is;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * Exercise 5
 */
final class StringEncrypter {

    private static final String BASE_DIR = System.getProperty("user.dir");
    private static final String KEY_FILE = BASE_DIR + "/key.txt";
    //    private static final String KEY_FILE = BASE_DIR + "/private.key";
    private static final String PUBLIC_KEY_FILE = BASE_DIR + "/public_key.txt";
    //    private static final String PUBLIC_KEY_FILE = BASE_DIR + "/public.key";
    private static final String MESSAGE_FILE = BASE_DIR + "/encrypted_message.txt";
    //    private static final String MESSAGE_FILE = BASE_DIR + "/encryptedMessageEx5.txt";

    public static void main(String[] args) {
        String stringInput = "This is a test string"; // could use args here
        byte[] asciiString = stringInput.getBytes(StandardCharsets.US_ASCII);
        BigInteger input = new BigInteger(asciiString);
        try {
            writeKeys();
            BigInteger encryptedMsg = encryptMessage(input);
            writeMessage(encryptedMsg, signMessage(encryptedMsg));

            ObjectInputStream inputStream = in(MESSAGE_FILE);
            BigInteger message = new BigInteger((String) inputStream.readObject());
            BigInteger signature = new BigInteger((String) inputStream.readObject());

            if (verifyMessage(message, signature)) {
                BigInteger decrypted = decryptMessage(message);
                System.out.println("Decoded value: " + new String(decrypted.toByteArray()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeKeys() throws IOException {
        RSA rsa = new RSAImpl();
        rsa.savePublic(out(PUBLIC_KEY_FILE));
        rsa.save(out(KEY_FILE));
    }

    private static BigInteger encryptMessage(BigInteger input) throws IOException, ClassNotFoundException, BadMessageException {
        RSA rsa = new RSAImpl(in(PUBLIC_KEY_FILE));
        return rsa.encrypt(input);
    }

    private static BigInteger signMessage(BigInteger input) throws IOException, ClassNotFoundException, BadMessageException {
        RSA rsa = new RSAImpl(in(KEY_FILE));
        return rsa.sign(input);
    }

    private static void writeMessage(BigInteger... values) throws IOException {
        ObjectOutputStream outputStream = out(MESSAGE_FILE);
        for (BigInteger value : values) {
            outputStream.writeObject(value.toString());
        }
    }

    private static BigInteger decryptMessage(BigInteger message) throws IOException, ClassNotFoundException, BadMessageException {
        RSA rsa = new RSAImpl(in(KEY_FILE));
        return rsa.decrypt(message);
    }

    private static boolean verifyMessage(BigInteger message, BigInteger signature) throws IOException, ClassNotFoundException {
        RSA rsa = new RSAImpl(in(PUBLIC_KEY_FILE));
        return rsa.verify(message, signature);
    }

    private static ObjectInputStream in(String name) throws IOException {
        return new ObjectInputStream(new FileInputStream(name));
    }

    private static ObjectOutputStream out(String name) throws IOException {
        return new ObjectOutputStream(new FileOutputStream(name));
    }
}
