/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Crypt {
    private static final Logger LOGGER = LogManager.getLogger();

    public static SecretKey generateSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            return keyGenerator.generateKey();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new Error(noSuchAlgorithmException);
        }
    }

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            return keyPairGenerator.generateKeyPair();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace();
            LOGGER.error("Key pair generation failed!");
            return null;
        }
    }

    public static byte[] digestData(String string, PublicKey publicKey, SecretKey secretKey) {
        try {
            return Crypt.digestData("SHA-1", string.getBytes("ISO_8859_1"), secretKey.getEncoded(), publicKey.getEncoded());
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            return null;
        }
    }

    private static byte[] digestData(String string, byte[] ... arrby) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(string);
            for (byte[] arrby2 : arrby) {
                messageDigest.update(arrby2);
            }
            return messageDigest.digest();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace();
            return null;
        }
    }

    public static PublicKey byteToPublicKey(byte[] arrby) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(arrby);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(x509EncodedKeySpec);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (InvalidKeySpecException invalidKeySpecException) {
            // empty catch block
        }
        LOGGER.error("Public key reconstitute failed!");
        return null;
    }

    public static SecretKey decryptByteToSecretKey(PrivateKey privateKey, byte[] arrby) {
        return new SecretKeySpec(Crypt.decryptUsingKey(privateKey, arrby), "AES");
    }

    public static byte[] encryptUsingKey(Key key, byte[] arrby) {
        return Crypt.cipherData(1, key, arrby);
    }

    public static byte[] decryptUsingKey(Key key, byte[] arrby) {
        return Crypt.cipherData(2, key, arrby);
    }

    private static byte[] cipherData(int n, Key key, byte[] arrby) {
        try {
            return Crypt.setupCipher(n, key.getAlgorithm(), key).doFinal(arrby);
        }
        catch (IllegalBlockSizeException illegalBlockSizeException) {
            illegalBlockSizeException.printStackTrace();
        }
        catch (BadPaddingException badPaddingException) {
            badPaddingException.printStackTrace();
        }
        LOGGER.error("Cipher data failed!");
        return null;
    }

    private static Cipher setupCipher(int n, String string, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(string);
            cipher.init(n, key);
            return cipher;
        }
        catch (InvalidKeyException invalidKeyException) {
            invalidKeyException.printStackTrace();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace();
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            noSuchPaddingException.printStackTrace();
        }
        LOGGER.error("Cipher creation failed!");
        return null;
    }

    public static Cipher getCipher(int n, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(n, key, new IvParameterSpec(key.getEncoded()));
            return cipher;
        }
        catch (GeneralSecurityException generalSecurityException) {
            throw new RuntimeException(generalSecurityException);
        }
    }
}

