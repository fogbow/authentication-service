package cloud.fogbow.as.stubs;

import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.constants.OpenNebulaConstants;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.models.FederationUser;
import cloud.fogbow.common.util.FederationUserUtil;
import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.common.util.RSAUtil;
import cloud.fogbow.as.core.util.TokenValueProtector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class StubTokenGenerator {

    private String provider;
    private PrivateKey privateKey;

    public StubTokenGenerator() throws IOException, GeneralSecurityException {
        this.provider = "fake-provider";

        String keysPath = HomeDir.getPath();
        String privKeyPath = keysPath + "private.key";

        this.privateKey = RSAUtil.getPrivateKey(privKeyPath);
    }

    public String createTokenValue(String publicKeyString, int duration)
            throws FogbowException {

        String tokenAttributes = this.createTokenValue();
        String expirationTime = generateExpirationTime(duration);
        String payload = tokenAttributes + FogbowConstants.PAYLOAD_SEPARATOR + expirationTime;
        String signature = null;
        RSAPublicKey publicKey;
        String signedUnprotectedToken;
        try {
            signature = RSAUtil.sign(this.privateKey, payload);
            signedUnprotectedToken = payload + FogbowConstants.TOKEN_SEPARATOR + signature;
            publicKey = RSAUtil.getPublicKeyFromString(publicKeyString);
        } catch (UnsupportedEncodingException | GeneralSecurityException e) {
            throw new UnexpectedException();
        }
        return TokenValueProtector.encrypt(publicKey, signedUnprotectedToken, FogbowConstants.TOKEN_STRING_SEPARATOR);
    }

    private String generateExpirationTime(int duration) {
        Date expirationDate = new Date(getNow() + TimeUnit.DAYS.toMillis(duration));
        String expirationTime = Long.toString(expirationDate.getTime());
        return expirationTime;
    }

    public long getNow() {
        return System.currentTimeMillis();
    }

    public String createTokenValue() {
        String username = "fake-username";
        String password = "fake-password";

        String openNebulaTokenValue = username + OpenNebulaConstants.TOKEN_VALUE_SEPARATOR + password;

        FederationUser user = new FederationUser(this.provider, username, username, openNebulaTokenValue, new HashMap<>());
        return FederationUserUtil.serialize(user);
    }
}
