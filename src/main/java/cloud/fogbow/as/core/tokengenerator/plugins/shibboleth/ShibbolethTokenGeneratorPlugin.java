package cloud.fogbow.as.core.tokengenerator.plugins.shibboleth;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import cloud.fogbow.common.constants.Messages;
import cloud.fogbow.common.util.RSAUtil;
import cloud.fogbow.as.core.PropertiesHolder;
import org.apache.log4j.Logger;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.common.exceptions.FatalErrorException;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnauthenticatedUserException;
import cloud.fogbow.as.core.constants.ConfigurationConstants;
import cloud.fogbow.as.core.tokengenerator.TokenGeneratorPlugin;
import cloud.fogbow.as.core.tokengenerator.plugins.AttributeJoiner;

public class ShibbolethTokenGeneratorPlugin implements TokenGeneratorPlugin {
	private static final Logger LOGGER = Logger.getLogger(ShibbolethTokenGeneratorPlugin.class);
	// Shib token parameters
	private static final int SHIB_TOKEN_PARAMETERS_SIZE = 5;
	private static final int SAML_ATTRIBUTES_ATTR_SHIB_INDEX = 4;
	private static final int COMMON_NAME_ATTR_SHIB_INDEX = 3;
	private static final int EDU_PRINCIPAL_NAME_ATTR_SHIB_INDEX = 2;
	private static final int ASSERTION_URL_ATTR_SHIB_INDEX = 1;
	private static final int SECREC_ATTR_SHIB_INDEX = 0;
	// credentails
	private static final String TOKEN_CREDENTIAL = "token";
	private static final String KEY_SIGNATURE_CREDENTIAL = "keySignature";
	private static final String KEY_CREDENTIAL = "key";
	// Shib-specific token constants
	public static final String SHIB_TOKEN_STRING_SEPARATOR = ":";
	public static final String ASSERTION_URL_ATTR_SHIB_KEY = "assertionUrl";
	public static final String SAML_ATTRIBUTES_ATTR_SHIB_KEY = "samlAttributes";

	private String tokenProviderId;
	private RSAPrivateKey asPrivateKey;
	private RSAPublicKey shibAppPublicKey;
	private SecretManager secretManager;

	public ShibbolethTokenGeneratorPlugin() {
		this.tokenProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.LOCAL_MEMBER_ID_KEY);
        try {
			this.asPrivateKey = ServiceAsymmetricKeysHolder.getInstance().getPrivateKey();
        } catch (IOException | GeneralSecurityException e) {
            throw new FatalErrorException(
            		String.format(cloud.fogbow.as.core.constants.Messages.Fatal.ERROR_READING_PRIVATE_KEY_FILE, e.getMessage()));
        }
		try {
			this.shibAppPublicKey = getShibbolethApplicationPublicKey();
		} catch (IOException | GeneralSecurityException e) {
			throw new FatalErrorException(
					String.format(cloud.fogbow.as.core.constants.Messages.Fatal.ERROR_READING_PUBLIC_KEY_FILE, e.getMessage()));
		}
		this.secretManager = new SecretManager();
	}
	
	@Override
	public String createTokenValue(Map<String, String> userCredentials) throws FogbowException {
		String tokenShibAppEncrypted = userCredentials.get(TOKEN_CREDENTIAL);
		String keyShibAppEncrypted = userCredentials.get(KEY_CREDENTIAL);
		String keySignatureShibApp = userCredentials.get(KEY_SIGNATURE_CREDENTIAL);
		
		String keyShibApp = decryptKeyShib(keyShibAppEncrypted);
		String tokenShib = decryptTokenShib(keyShibApp, tokenShibAppEncrypted);
		verifyShibAppKeyAuthenticity(keySignatureShibApp, keyShibApp);
		
		String[] tokenShibAppParameters = tokenShib.split(SHIB_TOKEN_STRING_SEPARATOR);
		checkTokenFormat(tokenShibAppParameters);
		
		verifySecretShibAppToken(tokenShibAppParameters);
		
		String rawToken = createRawToken(tokenShibAppParameters);

		return rawToken;
	}

	protected void verifySecretShibAppToken(String[] tokenShibParameters) throws UnauthenticatedUserException {
		String secret = tokenShibParameters[SECREC_ATTR_SHIB_INDEX];
		boolean isValid = this.secretManager.verify(secret);
		if (!isValid) {
        	String errorMsg = String.format(Messages.Exception.AUTHENTICATION_ERROR);
        	LOGGER.error(errorMsg);
            throw new UnauthenticatedUserException(errorMsg);			
		}
	}

	protected void checkTokenFormat(String[] tokenShibParameters) throws UnauthenticatedUserException {
		if (tokenShibParameters.length != SHIB_TOKEN_PARAMETERS_SIZE) {
        	String errorMsg = String.format(Messages.Exception.AUTHENTICATION_ERROR);
        	LOGGER.error(errorMsg);
            throw new UnauthenticatedUserException(errorMsg);
		}
	}

	protected String createRawToken(String[] tokenShibParameters) {
		LOGGER.debug("Creating raw token");
		String assertionUrl = tokenShibParameters[ASSERTION_URL_ATTR_SHIB_INDEX];
		String eduPrincipalName = tokenShibParameters[EDU_PRINCIPAL_NAME_ATTR_SHIB_INDEX];
		String commonName = tokenShibParameters[COMMON_NAME_ATTR_SHIB_INDEX];
		// attributes in json format, like this "{\"key\": \"value\"}"
		String samlAttributes = tokenShibParameters[SAML_ATTRIBUTES_ATTR_SHIB_INDEX];

		Map<String, String> attributes = new HashMap<>();
		attributes.put(FogbowConstants.PROVIDER_ID_KEY, this.tokenProviderId);
		attributes.put(FogbowConstants.USER_ID_KEY, eduPrincipalName);
		attributes.put(FogbowConstants.USER_NAME_KEY, commonName);
		attributes.put(ASSERTION_URL_ATTR_SHIB_KEY, assertionUrl);
		attributes.put(SAML_ATTRIBUTES_ATTR_SHIB_KEY, samlAttributes);
		return AttributeJoiner.join(attributes);

	}

	protected void verifyShibAppKeyAuthenticity(String signature, String message) throws UnauthenticatedUserException {
		try {
			RSAUtil.verify(this.shibAppPublicKey, message, signature);
		} catch (Exception e) {
        	String errorMsg = String.format(Messages.Exception.AUTHENTICATION_ERROR);
        	LOGGER.error(errorMsg, e);
            throw new UnauthenticatedUserException(errorMsg, e);
		}
	}

	protected String decryptTokenShib(String keyShib, String rasToken) throws UnauthenticatedUserException {
		String tokenShibApp = null;
		try {
			tokenShibApp = RSAUtil.decryptAES(keyShib.getBytes(RSAUtil.UTF_8), rasToken);
		} catch (Exception e) {
        	String errorMsg = String.format(Messages.Exception.AUTHENTICATION_ERROR);
        	LOGGER.error(errorMsg, e);
            throw new UnauthenticatedUserException(errorMsg, e);
		}
		return tokenShibApp;
	}
	
	protected String decryptKeyShib(String keyShibAppEncrypted) throws UnauthenticatedUserException {
		String keyShibApp = null;
		try {
			keyShibApp = RSAUtil.decrypt(keyShibAppEncrypted, this.asPrivateKey);
		} catch (Exception e) {
        	String errorMsg = String.format(Messages.Exception.AUTHENTICATION_ERROR);
        	LOGGER.error(errorMsg, e);
            throw new UnauthenticatedUserException(errorMsg, e);
		}
		return keyShibApp;
	}
	
    protected RSAPublicKey getShibbolethApplicationPublicKey() throws IOException, GeneralSecurityException {
        String filename = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.SHIB_PUBLIC_FILE_PATH_KEY);
        LOGGER.debug("Shibboleth application public key path: " + filename);
        String publicKeyPEM = RSAUtil.getKey(filename);
        LOGGER.debug("Shibboleth application Public key: " + publicKeyPEM);
        return RSAUtil.getPublicKeyFromString(publicKeyPEM);
    }
}
