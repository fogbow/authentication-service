package cloud.fogbow.as.core.util;

import cloud.fogbow.as.constants.Messages;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.exceptions.InvalidTokenException;
import cloud.fogbow.common.exceptions.UnauthenticatedUserException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.models.FederationUser;
import cloud.fogbow.common.util.FederationUserUtil;
import cloud.fogbow.common.util.RSAUtil;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.common.util.TokenValueProtector;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AuthenticationUtil {
    private static final long EXPIRATION_INTERVAL = TimeUnit.DAYS.toMillis(1); // One day

    public static FederationUser authenticate(PublicKey asPublicKey, String encryptedTokenValue)
            throws UnauthenticatedUserException, InvalidTokenException {
        try {
            RSAPrivateKey privateKey = ServiceAsymmetricKeysHolder.getInstance().getPrivateKey();
            String plainTokenValue = TokenValueProtector.decrypt(privateKey, encryptedTokenValue,
                    FogbowConstants.TOKEN_STRING_SEPARATOR);
            String[] tokenFields = StringUtils.splitByWholeSeparator(plainTokenValue, FogbowConstants.TOKEN_SEPARATOR);
            String payload = tokenFields[0];
            String signature = tokenFields[1];
            checkIfSignatureIsValid(asPublicKey, payload, signature);
            String[] payloadFields = StringUtils.splitByWholeSeparator(payload, FogbowConstants.PAYLOAD_SEPARATOR);
            String federationUserString = payloadFields[0];
            String expirationTime = payloadFields[1];
            checkIfTokenHasNotExprired(expirationTime);
            return FederationUserUtil.deserialize(federationUserString);
        } catch (IOException | GeneralSecurityException e) {
            throw new InvalidTokenException();
        }
    }

    public static String createFogbowToken(FederationUser federationUser, RSAPrivateKey privateKey, String publicKeyString)
            throws UnexpectedException {
        String tokenAttributes = FederationUserUtil.serialize(federationUser);
        String expirationTime = generateExpirationTime();
        String payload = tokenAttributes + FogbowConstants.PAYLOAD_SEPARATOR + expirationTime;
        try {
            String signature = RSAUtil.sign(privateKey, payload);
            String signedUnprotectedToken = payload + FogbowConstants.TOKEN_SEPARATOR + signature;
            RSAPublicKey publicKey = RSAUtil.getPublicKeyFromString(publicKeyString);
            return TokenValueProtector.encrypt(publicKey, signedUnprotectedToken, FogbowConstants.TOKEN_STRING_SEPARATOR);
        } catch (UnsupportedEncodingException | GeneralSecurityException e) {
            throw new UnexpectedException();
        }
    }

    private static void checkIfSignatureIsValid(PublicKey publicKey, String payload, String signature)
            throws UnauthenticatedUserException {

        try {
            if (!RSAUtil.verify(publicKey, payload, signature)) {
                throw new UnauthenticatedUserException(Messages.Exception.INVALID_TOKEN);
            }
        } catch (SignatureException | NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            throw new UnauthenticatedUserException(e.getMessage(), e);
        }
    }

    private static void checkIfTokenHasNotExprired(String expirationTime) throws UnauthenticatedUserException {
        Date currentDate = new Date(getNow());
        Date expirationDate = new Date(Long.parseLong(expirationTime));
        if (expirationDate.before(currentDate)) {
            throw new UnauthenticatedUserException(Messages.Exception.EXPIRED_TOKEN);
        }
    }

    public static Map<String, String> getAttributes(String attributeString) {
        Map<String, String> attributes = new HashMap<>();
        String attributePairs[] = StringUtils.splitByWholeSeparator(attributeString, FogbowConstants.ATTRIBUTE_SEPARATOR);
        for (String pair : attributePairs){
            String[] pairFields = StringUtils.splitByWholeSeparator(pair, FogbowConstants.KEY_VALUE_SEPARATOR);
            String key = pairFields[0];
            String value = pairFields[1];
            attributes.put(key, value);
        }
        return attributes;
    }

    private static String generateExpirationTime() {
        Date expirationDate = new Date(getNow() + EXPIRATION_INTERVAL);
        String expirationTime = Long.toString(expirationDate.getTime());
        return expirationTime;
    }

    private static long getNow() {
        return System.currentTimeMillis();
    }
}
