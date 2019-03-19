package cloud.fogbow.as.util;

import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.as.stubs.StubFogbowTokenGenerator;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnauthenticatedUserException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.security.*;

import static org.junit.Assert.*;

// Tests are executed by order of their names
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthenticationUtilTest extends ConfigureRSAKeyTest {
    private StubFogbowTokenGenerator tokenGenerator;

    private String publicKeyString;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @Before
    public void setUp() throws Exception {
        super.init();

        ServiceAsymmetricKeysHolder.getInstance().setPublicKeyFilePath(super.publicKeyPath);
        ServiceAsymmetricKeysHolder.getInstance().setPrivateKeyFilePath(super.privateKeyPath);

        this.publicKey = CryptoUtil.getPublicKey(super.publicKeyPath);
        this.privateKey = CryptoUtil.getPrivateKey(super.privateKeyPath);

        this.publicKeyString = CryptoUtil.getKey(super.publicKeyPath);
        this.tokenGenerator = new StubFogbowTokenGenerator();
    }

    @After
    public void tearDown(){
        super.tearDown();
    }

    @Test
    public void testAuthenticationSuccessful() throws FogbowException {
        // set up
        String token = tokenGenerator.createToken(publicKeyString, 1);

        // exercise
        SystemUser systemUser = null;
        try {
            systemUser = AuthenticationUtil.authenticate(publicKey, token);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());

        }

        // verify
        assertNotEquals(null, systemUser);
    }

    @Test(expected = UnauthenticatedUserException.class)
    public void testExpiredToken() throws InterruptedException, FogbowException {
        // set up
        String token = tokenGenerator.createToken(publicKeyString, 0);

        // exercise/verify
        SystemUser systemUser = AuthenticationUtil.authenticate(publicKey, token);
    }

    @Test(expected = UnauthenticatedUserException.class)
    public void testInvalidSignature() throws FogbowException, GeneralSecurityException {
        // set up
        KeyPair keyPair = CryptoUtil.generateKeyPair();
        PublicKey differentKey = keyPair.getPublic();
        String differentKeyString = CryptoUtil.savePublicKey(differentKey);
        String token = tokenGenerator.createToken(differentKeyString, 1);

        // exercise
        // Try to verify the signature of a different key
        SystemUser systemUser = AuthenticationUtil.authenticate(publicKey, token);
    }
}