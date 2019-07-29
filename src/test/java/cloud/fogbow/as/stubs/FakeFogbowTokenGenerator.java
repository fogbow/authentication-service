package cloud.fogbow.as.stubs;

import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.as.core.util.TokenProtector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FakeFogbowTokenGenerator {

    private String provider;
    private PrivateKey privateKey;

    public FakeFogbowTokenGenerator() throws IOException, GeneralSecurityException {
        this.provider = "fake-provider";

        String keysPath = HomeDir.getPath();
        String privKeyPath = keysPath + "private.key";

        this.privateKey = CryptoUtil.getPrivateKey(privKeyPath);
    }

    public String createToken(String publicKeyString, int duration) throws FogbowException {
        String tokenAttributes = this.createToken();
        String expirationTime = generateExpirationTime(duration);
        String payload = tokenAttributes + FogbowConstants.PAYLOAD_SEPARATOR + expirationTime;
        String signature = null;
        RSAPublicKey publicKey;
        String signedUnprotectedToken;
        try {
            signature = CryptoUtil.sign(this.privateKey, payload);
            signedUnprotectedToken = payload + FogbowConstants.TOKEN_SEPARATOR + signature;
            publicKey = CryptoUtil.getPublicKeyFromString(publicKeyString);
        } catch (UnsupportedEncodingException | GeneralSecurityException e) {
            throw new UnexpectedException();
        }
        return TokenProtector.encrypt(publicKey, signedUnprotectedToken, FogbowConstants.TOKEN_STRING_SEPARATOR);
    }

    private String generateExpirationTime(int duration) {
        Date expirationDate = new Date(getNow() + TimeUnit.DAYS.toMillis(duration));
        String expirationTime = Long.toString(expirationDate.getTime());
        return expirationTime;
    }

    public long getNow() {
        return System.currentTimeMillis();
    }

    public String createToken() throws UnexpectedException {
        String userId = "fake-userid";
        String userName = "fake-username";

        SystemUser user = new SystemUser(userId, userName, this.provider);
        return SystemUser.serialize(user);
    }
}
