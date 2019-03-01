package cloud.fogbow.as.util;

import cloud.fogbow.as.core.util.TokenValueProtector;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.exceptions.UnauthenticatedUserException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.common.util.RSAUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.*;

import static org.junit.Assert.*;

public class TokenValueProtectorTest {

    private String tokenSeparator;

    @Before
    public void setup(){
        this.tokenSeparator = FogbowConstants.TOKEN_SEPARATOR;
    }

    @Test
    public void testEcryptAndDecrypt() throws IOException, GeneralSecurityException, UnexpectedException, UnauthenticatedUserException {
        // set up
        String keysPath = HomeDir.getPath();
        String pubKeyPath = keysPath + "public.key";
        String privKeyPath = keysPath + "private.key";

        String originalMessage = "This is one more message to be encrypted and decrypted";
        PublicKey localPubKey = RSAUtil.getPublicKey(pubKeyPath);
        PrivateKey localPrivKey = RSAUtil.getPrivateKey(privKeyPath);

        // exercise
        String encryptedMessage = TokenValueProtector.encrypt(localPubKey, originalMessage, tokenSeparator);
        String decryptedMessage = TokenValueProtector.decrypt(localPrivKey, encryptedMessage, tokenSeparator);

        // exercise
        assertNotEquals(originalMessage, encryptedMessage);
        assertEquals(originalMessage, decryptedMessage);
    }

    @Test
    public void testRewrap() throws GeneralSecurityException, UnexpectedException, IOException, UnauthenticatedUserException {
        // set up
        String keysPath = HomeDir.getPath();
        String pubKeyPath = keysPath + "public.key";
        String privKeyPath = keysPath + "private.key";

        PublicKey localPubKey = RSAUtil.getPublicKey(pubKeyPath);
        PrivateKey localPrivKey = RSAUtil.getPrivateKey(privKeyPath);

        String originalMessage = "This is a second more message to be encrypted and decrypted";
        String protectedString = TokenValueProtector.encrypt(localPubKey, originalMessage, tokenSeparator);

        KeyPair forwardKeyPair = RSAUtil.generateKeyPair();
        PublicKey forwardPubKey = forwardKeyPair.getPublic();
        PrivateKey forwardPrivKey = forwardKeyPair.getPrivate();

        // exercise
        String newProtectedString = TokenValueProtector.rewrap(localPrivKey, forwardPubKey, protectedString, tokenSeparator);
        String decryptedNewString = TokenValueProtector.decrypt(forwardPrivKey, newProtectedString, tokenSeparator);

        // verify
        assertNotEquals(protectedString, newProtectedString);
        assertEquals(originalMessage, decryptedNewString);
    }
}