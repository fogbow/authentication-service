package org.fogbowcloud.as.core;

import org.apache.log4j.Logger;
import org.fogbowcloud.as.common.exceptions.FogbowException;
import org.fogbowcloud.as.common.exceptions.UnexpectedException;
import org.fogbowcloud.as.common.util.RSAUtil;
import org.fogbowcloud.as.common.util.ServiceAsymmetricKeysHolder;
import org.fogbowcloud.as.common.util.PropertiesUtil;
import org.fogbowcloud.as.core.constants.*;
import org.fogbowcloud.as.core.tokengenerator.TokenGeneratorPlugin;
import org.fogbowcloud.as.core.tokengenerator.TokenGeneratorPluginDecorator;

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
        this.buildNumber = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.BUILD_NUMBER,
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
            throws UnexpectedException, FogbowException {
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
        this.buildNumber = properties.getProperty(ConfigurationConstants.BUILD_NUMBER,
                DefaultConfigurationConstants.BUILD_NUMBER);
    }
}
