package cloud.fogbow.as.core.util;

import cloud.fogbow.as.constants.ConfigurationPropertyDefaults;
import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.constants.Messages;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.exceptions.UnauthenticatedUserException;
import cloud.fogbow.common.exceptions.InternalServerErrorException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

public class AuthenticationUtil {

    public static SystemUser authenticate(PublicKey asPublicKey, String encryptedTokenValue)
            throws UnauthenticatedUserException {
        RSAPrivateKey privateKey = null;
        try {
            privateKey = ServiceAsymmetricKeysHolder.getInstance().getPrivateKey();
            String plainTokenValue = TokenProtector.decrypt(privateKey, encryptedTokenValue,
                    FogbowConstants.TOKEN_STRING_SEPARATOR);
            String[] tokenFields = StringUtils.splitByWholeSeparator(plainTokenValue, FogbowConstants.TOKEN_SEPARATOR);
            String payload = tokenFields[0];
            String signature = tokenFields[1];
            checkIfSignatureIsValid(asPublicKey, payload, signature);
            String[] payloadFields = StringUtils.splitByWholeSeparator(payload, FogbowConstants.PAYLOAD_SEPARATOR);
            String federationUserString = payloadFields[0];
            String expirationTime = payloadFields[1];
            checkIfTokenHasNotExprired(expirationTime);
            return SystemUser.deserialize(federationUserString);
        } catch (InternalServerErrorException e) {
            throw new UnauthenticatedUserException(e.getMessage());
        }
    }
    
    public static String createFogbowToken(SystemUser systemUser, RSAPrivateKey privateKey, String publicKeyString)
            throws InternalServerErrorException {
        RSAPublicKey publicKey;
        
        try {
            publicKey = CryptoUtil.getPublicKeyFromString(publicKeyString);
        } catch (GeneralSecurityException e) {
            throw new InternalServerErrorException();
        }
        
        return createFogbowToken(systemUser, privateKey, publicKey);
    }
    
    public static String createFogbowToken(SystemUser systemUser, RSAPrivateKey privateKey, RSAPublicKey publicKey)
            throws InternalServerErrorException {
        String tokenAttributes = SystemUser.serialize(systemUser);
        String expirationTime = generateExpirationTime();
        String payload = tokenAttributes + FogbowConstants.PAYLOAD_SEPARATOR + expirationTime;
        return encryptToken(payload, privateKey, publicKey);
    }
    
    public static String encryptToken(String token, RSAPrivateKey privateKey, RSAPublicKey publicKey) 
            throws InternalServerErrorException {
        try {
            String signature = CryptoUtil.sign(privateKey, token);
            String signedUnprotectedToken = token + FogbowConstants.TOKEN_SEPARATOR + signature;
            return TokenProtector.encrypt(publicKey, signedUnprotectedToken, FogbowConstants.TOKEN_STRING_SEPARATOR);
        } catch (UnsupportedEncodingException | GeneralSecurityException e) {
            throw new InternalServerErrorException();
        }
    }

    private static void checkIfSignatureIsValid(PublicKey publicKey, String payload, String signature)
            throws UnauthenticatedUserException {

        try {
            if (!CryptoUtil.verify(publicKey, payload, signature)) {
                throw new UnauthenticatedUserException(Messages.Exception.INVALID_TOKEN);
            }
        } catch (SignatureException | NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            throw new UnauthenticatedUserException(e.getMessage());
        }
    }

    private static void checkIfTokenHasNotExprired(String expirationTime) throws UnauthenticatedUserException {
        Date currentDate = new Date(getNow());
        Date expirationDate = new Date(Long.parseLong(expirationTime));
        if (expirationDate.before(currentDate)) {
            throw new UnauthenticatedUserException(Messages.Exception.EXPIRED_TOKEN);
        }
    }

    private static String generateExpirationTime() {
        String expirationIntervalProperty = PropertiesHolder.getInstance().getProperty(
                ConfigurationPropertyKeys.TOKEN_EXPIRATION_INTERVAL, 
                ConfigurationPropertyDefaults.DEFAULT_TOKEN_EXPIRATION_INTERVAL);
        Long expirationInterval = Long.valueOf(expirationIntervalProperty);
        Date expirationDate = new Date(getNow() + expirationInterval);
        String expirationTime = Long.toString(expirationDate.getTime());
        return expirationTime;
    }

    private static long getNow() {
        return System.currentTimeMillis();
    }
}
