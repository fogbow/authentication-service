package cloud.fogbow.as.util;

import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.as.stubs.StubTokenGenerator;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnauthenticatedUserException;
import cloud.fogbow.common.models.FederationUser;
import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.*;

import static org.junit.Assert.*;

public class AuthenticationUtilTest {

    private StubTokenGenerator tokenGenerator;
    private String publicKeyString;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @Before
    public void setUp() throws Exception {

        String keysPath = HomeDir.getPath();
        String pubKeyPath = keysPath + "public.key";
        String privKeyPath = keysPath + "private.key";

        ServiceAsymmetricKeysHolder.getInstance().setPublicKeyFilePath(pubKeyPath);
        ServiceAsymmetricKeysHolder.getInstance().setPrivateKeyFilePath(privKeyPath);

        this.publicKey = CryptoUtil.getPublicKey(pubKeyPath);
        this.privateKey = CryptoUtil.getPrivateKey(privKeyPath);

        this.publicKeyString = CryptoUtil.getKey(pubKeyPath);
        this.tokenGenerator = new StubTokenGenerator();
    }

    @Test
    public void testSuccessfulAuthentication() throws IOException, GeneralSecurityException, FogbowException {
        // set up
        String tokenValue = tokenGenerator.createTokenValue(publicKeyString, 1);

        // exercise
        FederationUser federationUser = AuthenticationUtil.authenticate(publicKey, tokenValue);

        // verify
        assertNotEquals(null, federationUser);
    }

    @Test(expected = UnauthenticatedUserException.class)
    public void testExpiredToken() throws InterruptedException, FogbowException {
        // set up
        String tokenValue = tokenGenerator.createTokenValue(publicKeyString, 0);
        Thread.sleep(2000);

        // exercise
        FederationUser federationUser = AuthenticationUtil.authenticate(publicKey, tokenValue);
    }

    @Test(expected = UnauthenticatedUserException.class)
    public void testInvalidSignature() throws FogbowException, GeneralSecurityException {
        // set up
        KeyPair keyPair = CryptoUtil.generateKeyPair();
        PublicKey differentKey = keyPair.getPublic();
        String differentKeyString = CryptoUtil.savePublicKey(differentKey);
        String tokenValue = tokenGenerator.createTokenValue(differentKeyString, 1);

        // exercise
        // Try to verify the signature of a different key
        FederationUser federationUser = AuthenticationUtil.authenticate(publicKey, tokenValue);
    }
}