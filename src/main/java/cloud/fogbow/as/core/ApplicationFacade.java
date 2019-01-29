package cloud.fogbow.as.core;

import cloud.fogbow.common.util.PropertiesUtil;
import cloud.fogbow.common.util.RSAUtil;
import cloud.fogbow.as.core.constants.ConfigurationConstants;
import cloud.fogbow.as.core.constants.DefaultConfigurationConstants;
import cloud.fogbow.as.core.constants.SystemConstants;
import org.apache.log4j.Logger;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.as.core.tokengenerator.TokenGeneratorPlugin;
import cloud.fogbow.as.core.tokengenerator.TokenGeneratorPluginDecorator;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Properties;

public class ApplicationFacade {
    private final Logger LOGGER = Logger.getLogger(ApplicationFacade.class);

    private static ApplicationFacade instance;

    private String buildNumber;

    private TokenGeneratorPluginDecorator tokenGeneratorPluginDecorator;

    private ApplicationFacade() {
        this.buildNumber = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.BUILD_NUMBER_KEY,
                DefaultConfigurationConstants.BUILD_NUMBER);
    }

    public static ApplicationFacade getInstance() {
        synchronized (ApplicationFacade.class) {
            if (instance == null) {
                instance = new ApplicationFacade();
            }
            return instance;
        }
    }

    public void setTokenGeneratorPlugin(TokenGeneratorPlugin tokenGeneratorPlugin) {
        // The token generator plugin generates a raw token; the wrapper adds an expiration time,
        // a signature, and encrypts the token using the public key provided by the client.
        this.tokenGeneratorPluginDecorator = new TokenGeneratorPluginDecorator(tokenGeneratorPlugin);
    }

    public String createTokenValue(Map<String, String> userCredentials, String publicKey)
            throws FogbowException {
        // There is no need to authenticate the user or authorize this operation
        return this.tokenGeneratorPluginDecorator.createTokenValue(userCredentials, publicKey);
    }

    public String getPublicKey() throws UnexpectedException {
        // There is no need to authenticate the user or authorize this operation
        try {
            return RSAUtil.savePublicKey(ServiceAsymmetricKeysHolder.getInstance().getPublicKey());
        } catch (IOException | GeneralSecurityException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }

    public String getVersionNumber() {
        // There is no need to authenticate the user or authorize this operation
        return SystemConstants.API_VERSION_NUMBER + "-" + this.buildNumber;
    }

    // Used for testing
    protected void setBuildNumber(String fileName) {
        Properties properties = PropertiesUtil.readProperties(fileName);
        this.buildNumber = properties.getProperty(ConfigurationConstants.BUILD_NUMBER_KEY,
                DefaultConfigurationConstants.BUILD_NUMBER);
    }
}
