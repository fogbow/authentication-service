package cloud.fogbow.as.util;

import cloud.fogbow.as.core.util.TokenProtector;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.InternalServerErrorException;
import cloud.fogbow.common.exceptions.UnauthenticatedUserException;
import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.common.util.CryptoUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.*;

import static org.junit.Assert.*;

public class TokenProtectorTest {

    private String tokenSeparator;

    @Before
    public void setup() throws FogbowException, GeneralSecurityException, IOException {
        ConfigureRSAKeyTest.init();
        this.tokenSeparator = FogbowConstants.TOKEN_SEPARATOR;
    }

    @After
    public void tearDown(){
        ConfigureRSAKeyTest.tearDown();
    }

    @Test
    public void testEcryptAndDecrypt() throws IOException, GeneralSecurityException, InternalServerErrorException, UnauthenticatedUserException {
        // set up
        String keysPath = HomeDir.getPath();
        String pubKeyPath = keysPath + "public.key";
        String privKeyPath = keysPath + "private.key";

        String originalToken = "This is the token to be encrypted and decrypted";
        PublicKey localPubKey = CryptoUtil.getPublicKey(pubKeyPath);
        PrivateKey localPrivKey = CryptoUtil.getPrivateKey(privKeyPath);

        // exercise
        String encryptedToken = TokenProtector.encrypt(localPubKey, originalToken, tokenSeparator);
        String decryptedToken = TokenProtector.decrypt(localPrivKey, encryptedToken, tokenSeparator);

        // exercise
        assertNotEquals(originalToken, encryptedToken);
        assertEquals(originalToken, decryptedToken);
    }

    @Test
    public void testRewrap() throws GeneralSecurityException, InternalServerErrorException, IOException, UnauthenticatedUserException {
        // set up
        String keysPath = HomeDir.getPath();
        String pubKeyPath = keysPath + "public.key";
        String privKeyPath = keysPath + "private.key";

        PublicKey localPubKey = CryptoUtil.getPublicKey(pubKeyPath);
        PrivateKey localPrivKey = CryptoUtil.getPrivateKey(privKeyPath);

        String originalToken = "This is the token to be encrypted and decrypted";
        String protectedToken = TokenProtector.encrypt(localPubKey, originalToken, tokenSeparator);

        KeyPair forwardKeyPair = CryptoUtil.generateKeyPair();
        PublicKey forwardPubKey = forwardKeyPair.getPublic();
        PrivateKey forwardPrivKey = forwardKeyPair.getPrivate();

        // exercise
        String newProtectedToken = TokenProtector.rewrap(localPrivKey, forwardPubKey, protectedToken, tokenSeparator);
        String decryptedNewToken = TokenProtector.decrypt(forwardPrivKey, newProtectedToken, tokenSeparator);

        // verify
        assertNotEquals(protectedToken, newProtectedToken);
        assertEquals(originalToken, decryptedNewToken);
    }
}