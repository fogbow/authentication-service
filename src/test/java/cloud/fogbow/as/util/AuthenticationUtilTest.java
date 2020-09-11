package cloud.fogbow.as.util;

import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.as.stubs.FakeFogbowTokenGenerator;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnauthenticatedUserException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import org.junit.*;

import java.io.IOException;
import java.security.*;

import static org.junit.Assert.*;

public class AuthenticationUtilTest {
    private FakeFogbowTokenGenerator tokenGenerator;

    private String publicKeyString;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @BeforeClass
    public static void setUp() throws Exception {
        ConfigureRSAKeyTest.init();

        ServiceAsymmetricKeysHolder.getInstance().setPublicKeyFilePath(ConfigureRSAKeyTest.publicKeyPath);
        ServiceAsymmetricKeysHolder.getInstance().setPrivateKeyFilePath(ConfigureRSAKeyTest.privateKeyPath);
    }

    @Before
    public void before() throws IOException, GeneralSecurityException {
        this.publicKey = CryptoUtil.getPublicKey(ConfigureRSAKeyTest.publicKeyPath);
        this.privateKey = CryptoUtil.getPrivateKey(ConfigureRSAKeyTest.privateKeyPath);

        this.publicKeyString = CryptoUtil.getKey(ConfigureRSAKeyTest.publicKeyPath);
        this.tokenGenerator = new FakeFogbowTokenGenerator();
    }

    @AfterClass
    public static void tearDown(){
        ConfigureRSAKeyTest.tearDown();
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
    public void testExpiredToken() throws FogbowException {
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
        String differentKeyString = CryptoUtil.toBase64(differentKey);
        String token = tokenGenerator.createToken(differentKeyString, 1);

        // exercise
        // Try to verify the signature of a different key
        SystemUser systemUser = AuthenticationUtil.authenticate(publicKey, token);
    }
}