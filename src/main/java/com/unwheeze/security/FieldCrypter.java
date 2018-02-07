package com.unwheeze.security;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

public class FieldCrypter {

    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 1000;
    private static final int HASH_KEY_LENGHT = 256;
    private static final String ALGORITHM_HASH = "PBKDF2WithHmacSHA1";

    private static final Logger log = LogManager.getLogger(FieldCrypter.class.getSimpleName());

    public FieldCrypter() {}

    public static byte[] generateRandomSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    public static byte[] generateHash(char [] pwd, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(pwd,salt,ITERATIONS,HASH_KEY_LENGHT);
        Arrays.fill(pwd,Character.MIN_VALUE);

        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM_HASH);
            return secretKeyFactory.generateSecret(spec).getEncoded();
        } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getMessage());
            throw new AssertionError("Error while hashing password :"+e.getMessage());
        } finally {
            spec.clearPassword();
        }
    }

    public static boolean isExpectedHash(char[] pwd, byte[] salt, byte[] expectedHash) {
        byte[] pwdHash = generateHash(pwd,salt);
        Arrays.fill(pwd,Character.MIN_VALUE);
        if(pwdHash.length != expectedHash.length) return false;
        for(int i=0; i<pwdHash.length;i++) {
            if(pwdHash[i] != expectedHash[i]) return false;
        }

        return true;
    }
}
