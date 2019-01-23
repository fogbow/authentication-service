package org.fogbowcloud.as;

import org.apache.log4j.Logger;
import org.fogbowcloud.as.common.util.HomeDir;
import org.fogbowcloud.as.core.*;
import org.fogbowcloud.as.core.constants.ConfigurationConstants;
import org.fogbowcloud.as.core.constants.SystemConstants;
import org.fogbowcloud.as.core.exceptions.FatalErrorException;
import org.fogbowcloud.as.core.tokengenerator.TokenGeneratorPlugin;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Main implements ApplicationRunner {
    private final Logger LOGGER = Logger.getLogger(Main.class);

    @Override
    public void run(ApplicationArguments args) {
        try {
            // Getting the name of the local member
            String localMemberId = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.LOCAL_MEMBER_ID);

            // Setting up plugin
            String confFilePath = HomeDir.getPath() + SystemConstants.AS_CONF_FILE_NAME;
            TokenGeneratorPlugin tokenGeneratorPlugin = TokenGeneratorPluginInstantiator.getTokenGeneratorPlugin(confFilePath);

            // Setting up application facade
            ApplicationFacade applicationFacade = ApplicationFacade.getInstance();
            applicationFacade.setTokenGeneratorPlugin(tokenGeneratorPlugin);
        } catch (FatalErrorException errorException) {
            LOGGER.fatal(errorException.getMessage(), errorException);
            tryExit();
        }
    }

    private void tryExit() {
        if (!Boolean.parseBoolean(System.getenv("SKIP_TEST_ON_TRAVIS")))
            System.exit(1);
    }
}
