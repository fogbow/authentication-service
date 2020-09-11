package cloud.fogbow.as.core;

import cloud.fogbow.as.core.systemidp.SystemIdentityProviderPlugin;
import cloud.fogbow.common.exceptions.InternalServerErrorException;
import cloud.fogbow.common.util.PropertiesUtil;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.constants.ConfigurationPropertyDefaults;
import cloud.fogbow.as.constants.SystemConstants;
import org.apache.log4j.Logger;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.as.core.systemidp.FogbowTokenGenerator;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Properties;

public class ApplicationFacade {
    private final Logger LOGGER = Logger.getLogger(ApplicationFacade.class);

    private static ApplicationFacade instance;

    private String buildNumber;

    private FogbowTokenGenerator fogbowTokenGenerator;

    private ApplicationFacade() {
        this.buildNumber = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.BUILD_NUMBER_KEY,
                ConfigurationPropertyDefaults.BUILD_NUMBER);
    }

    public static ApplicationFacade getInstance() {
        synchronized (ApplicationFacade.class) {
            if (instance == null) {
                instance = new ApplicationFacade();
            }
            return instance;
        }
    }

    public void initializeFogbowTokenGenerator(SystemIdentityProviderPlugin systemIdentityProviderPlugin) {
        // The token generator plugin generates a raw token; the wrapper adds an expiration time,
        // a signature, and encrypts the token using the public key provided by the client.
        this.fogbowTokenGenerator = new FogbowTokenGenerator(systemIdentityProviderPlugin);
    }

    public String createToken(Map<String, String> userCredentials, String publicKey) throws FogbowException {
        // There is no need to authenticate the user or authorize this operation
        return this.fogbowTokenGenerator.createToken(userCredentials, publicKey);
    }

    public String getPublicKey() throws InternalServerErrorException {
        // There is no need to authenticate the user or authorize this operation
        try {
            return CryptoUtil.toBase64(ServiceAsymmetricKeysHolder.getInstance().getPublicKey());
        } catch (GeneralSecurityException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public String getVersionNumber() {
        // There is no need to authenticate the user or authorize this operation
        return SystemConstants.API_VERSION_NUMBER + "-" + this.buildNumber;
    }

    // Used for testing
    protected void setBuildNumber(String fileName) {
        Properties properties = PropertiesUtil.readProperties(fileName);
        this.buildNumber = properties.getProperty(ConfigurationPropertyKeys.BUILD_NUMBER_KEY,
                ConfigurationPropertyDefaults.BUILD_NUMBER);
    }
}
