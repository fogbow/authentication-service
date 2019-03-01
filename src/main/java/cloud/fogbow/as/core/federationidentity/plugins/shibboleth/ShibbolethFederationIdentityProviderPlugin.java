package cloud.fogbow.as.core.federationidentity.plugins.shibboleth;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cloud.fogbow.as.core.federationidentity.FederationIdentityProviderPlugin;
import cloud.fogbow.common.constants.Messages;
import cloud.fogbow.common.models.FederationUser;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.as.core.PropertiesHolder;
import org.apache.log4j.Logger;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.common.exceptions.FatalErrorException;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnauthenticatedUserException;
import cloud.fogbow.as.constants.ConfigurationPropertyKeys;

public class ShibbolethFederationIdentityProviderPlugin implements FederationIdentityProviderPlugin {
	private static final Logger LOGGER = Logger.getLogger(ShibbolethFederationIdentityProviderPlugin.class);

	// Shib token parameters
	private static final int SHIB_TOKEN_PARAMETERS_SIZE = 5;
	private static final int SAML_ATTRIBUTES_ATTR_SHIB_INDEX = 4;
	private static final int COMMON_NAME_ATTR_SHIB_INDEX = 3;
	private static final int EDU_PRINCIPAL_NAME_ATTR_SHIB_INDEX = 2;
	private static final int ASSERTION_URL_ATTR_SHIB_INDEX = 1;
	private static final int SECREC_ATTR_SHIB_INDEX = 0;

	// credentials
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

	public ShibbolethFederationIdentityProviderPlugin() {
		this.tokenProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.LOCAL_MEMBER_ID_KEY);
        try {
			this.asPrivateKey = ServiceAsymmetricKeysHolder.getInstance().getPrivateKey();
        } catch (IOException | GeneralSecurityException e) {
            throw new FatalErrorException(
            		String.format(cloud.fogbow.as.constants.Messages.Fatal.ERROR_READING_PRIVATE_KEY_FILE, e.getMessage()));
        }
		try {
			this.shibAppPublicKey = getShibbolethApplicationPublicKey();
		} catch (IOException | GeneralSecurityException e) {
			throw new FatalErrorException(
					String.format(cloud.fogbow.as.constants.Messages.Fatal.ERROR_READING_PUBLIC_KEY_FILE, e.getMessage()));
		}
		this.secretManager = new SecretManager();
	}
	
	@Override
	public FederationUser getFederationUser(Map<String, String> userCredentials) throws FogbowException {
		String tokenShibAppEncrypted = userCredentials.get(TOKEN_CREDENTIAL);
		String keyShibAppEncrypted = userCredentials.get(KEY_CREDENTIAL);
		String keySignatureShibApp = userCredentials.get(KEY_SIGNATURE_CREDENTIAL);
		
		String keyShibApp = decryptKeyShib(keyShibAppEncrypted);
		String tokenShib = decryptTokenShib(keyShibApp, tokenShibAppEncrypted);
		verifyShibAppKeyAuthenticity(keySignatureShibApp, keyShibApp);

		String[] tokenShibAppParameters = tokenShib.split(SHIB_TOKEN_STRING_SEPARATOR);
		checkTokenFormat(tokenShibAppParameters);
		
		verifySecretShibAppToken(tokenShibAppParameters);
		
		return createFederationUser(tokenShibAppParameters);
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

	protected FederationUser createFederationUser(String[] tokenShibParameters) {
		LOGGER.debug(String.format("Creating FederationUser from %s", Arrays.toString(tokenShibParameters)));
		String assertionUrl = tokenShibParameters[ASSERTION_URL_ATTR_SHIB_INDEX];
		String eduPrincipalName = tokenShibParameters[EDU_PRINCIPAL_NAME_ATTR_SHIB_INDEX];
		String commonName = tokenShibParameters[COMMON_NAME_ATTR_SHIB_INDEX];
		// attributes in json format, like this "{\"key\": \"value\"}"
		String samlAttributes = tokenShibParameters[SAML_ATTRIBUTES_ATTR_SHIB_INDEX];

		Map<String, String> extraAttributes = new HashMap<>();
		extraAttributes.put(ASSERTION_URL_ATTR_SHIB_KEY, assertionUrl);
		extraAttributes.put(SAML_ATTRIBUTES_ATTR_SHIB_KEY, samlAttributes);
		return new FederationUser(this.tokenProviderId, eduPrincipalName, commonName, null, extraAttributes);
	}

	protected void verifyShibAppKeyAuthenticity(String signature, String message) throws UnauthenticatedUserException {
		try {
			CryptoUtil.verify(this.shibAppPublicKey, message, signature);
		} catch (Exception e) {
        	String errorMsg = String.format(Messages.Exception.AUTHENTICATION_ERROR);
        	LOGGER.error(errorMsg, e);
            throw new UnauthenticatedUserException(errorMsg, e);
		}
	}

	protected String decryptTokenShib(String keyShib, String rasToken) throws UnauthenticatedUserException {
		String tokenShibApp = null;
		try {
			tokenShibApp = CryptoUtil.decryptAES(keyShib.getBytes(CryptoUtil.UTF_8), rasToken);
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
			keyShibApp = CryptoUtil.decrypt(keyShibAppEncrypted, this.asPrivateKey);
		} catch (Exception e) {
        	String errorMsg = String.format(Messages.Exception.AUTHENTICATION_ERROR);
        	LOGGER.error(errorMsg, e);
            throw new UnauthenticatedUserException(errorMsg, e);
		}
		return keyShibApp;
	}
	
    protected RSAPublicKey getShibbolethApplicationPublicKey() throws IOException, GeneralSecurityException {
        String filename = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.SHIB_PUBLIC_FILE_PATH_KEY);
        LOGGER.debug("Shibboleth application public key path: " + filename);
        String publicKeyPEM = CryptoUtil.getKey(filename);
        LOGGER.debug("Shibboleth application Public key: " + publicKeyPEM);
        return CryptoUtil.getPublicKeyFromString(publicKeyPEM);
    }
}
