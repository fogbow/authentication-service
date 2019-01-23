package org.fogbowcloud.as.core;

import org.apache.log4j.Logger;
import org.fogbowcloud.as.core.constants.*;
import org.fogbowcloud.as.core.exceptions.*;
import org.fogbowcloud.as.core.tokengenerator.TokenGeneratorPlugin;
import org.fogbowcloud.as.common.util.PropertiesUtil;

import java.util.Map;
import java.util.Properties;

public class ApplicationFacade {
    private final Logger LOGGER = Logger.getLogger(ApplicationFacade.class);

    private static ApplicationFacade instance;

    private String buildNumber;

    private TokenGeneratorPlugin tokenGeneratorPlugin;

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
        this.tokenGeneratorPlugin = tokenGeneratorPlugin;
    }

    public String getVersionNumber() {
        return SystemConstants.API_VERSION_NUMBER + "-" + this.buildNumber;
    }

    public String createTokenValue(Map<String, String> userCredentials) throws UnexpectedException, FogbowAsException {
        // There is no need to authenticate the user or authorize this operation
        return this.tokenGeneratorPlugin.createTokenValue(userCredentials);
    }

    // Used for testing
    protected void setBuildNumber(String fileName) {
        Properties properties = PropertiesUtil.readProperties(fileName);
        this.buildNumber = properties.getProperty(ConfigurationConstants.BUILD_NUMBER,
                DefaultConfigurationConstants.BUILD_NUMBER);
    }
}
