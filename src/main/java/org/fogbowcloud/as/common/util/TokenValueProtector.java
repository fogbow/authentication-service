package org.fogbowcloud.as.common.util;

import org.fogbowcloud.as.common.exceptions.UnexpectedException;

import java.security.Key;

public class TokenValueProtector {
    // Encrypts a tokenValue string so that only the possessor of a private key corresponding to a given
    // public key is able to read the tokenValue string.

    public static String encrypt(Key key, String unprotectedString, String tokenSeparator)
            throws UnexpectedException {
        String randomKey;
        String encryptedToken;
        String encryptedKey;
        try {
            randomKey = RSAUtil.generateAESKey();
            encryptedToken = RSAUtil.encryptAES(randomKey.getBytes("UTF-8"), unprotectedString);
            encryptedKey = RSAUtil.encrypt(randomKey, key);
            return encryptedKey + tokenSeparator + encryptedToken;
        } catch (Exception e) {
            throw new UnexpectedException();
        }
    }
}
