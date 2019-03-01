package cloud.fogbow.as.core.util;

import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.exceptions.UnauthenticatedUserException;
import cloud.fogbow.common.util.CryptoUtil;
import org.apache.commons.lang.StringUtils;

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
            randomKey = CryptoUtil.generateAESKey();
            encryptedToken = CryptoUtil.encryptAES(randomKey.getBytes("UTF-8"), unprotectedString);
            encryptedKey = CryptoUtil.encrypt(randomKey, key);
            return encryptedKey + tokenSeparator + encryptedToken;
        } catch (Exception e) {
            throw new UnexpectedException();
        }
    }

    // Decrypts the tokenValue string using the provided key.
    public static String decrypt(Key key, String protectedString, String tokenSeparator)
            throws UnauthenticatedUserException {
        String randomKey;
        String decryptedToken;
        String[] tokenParts = StringUtils.splitByWholeSeparator(protectedString, tokenSeparator);

        try {
            randomKey = CryptoUtil.decrypt(tokenParts[0], key);
            decryptedToken = CryptoUtil.decryptAES(randomKey.getBytes("UTF-8"), tokenParts[1]);
            return decryptedToken;
        } catch (Exception e) {
            throw new UnauthenticatedUserException();
        }
    }

    // Decrypts the tokenValue and re-encrypts it with a new publicKey; this is needed before forwarding the token
    // to another service.
    public static String rewrap(Key decryptKey, Key encryptKey, String protectedString, String tokenSeparator)
            throws UnauthenticatedUserException, UnexpectedException {
        String unprotectedString = decrypt(decryptKey, protectedString, tokenSeparator);
        String newProtectedString = encrypt(encryptKey, unprotectedString, tokenSeparator);
        return newProtectedString;
    }
}
