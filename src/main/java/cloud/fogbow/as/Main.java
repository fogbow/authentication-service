package cloud.fogbow.as;

import cloud.fogbow.common.exceptions.FatalErrorException;
import cloud.fogbow.as.core.ApplicationFacade;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.as.core.TokenGeneratorPluginInstantiator;
import cloud.fogbow.as.core.tokengenerator.TokenGeneratorPlugin;
import org.apache.log4j.Logger;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.as.core.constants.ConfigurationConstants;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Main implements ApplicationRunner {
    private final Logger LOGGER = Logger.getLogger(Main.class);

    @Override
    public void run(ApplicationArguments args) {
        try {
            // Setting the name of the local member
            String localMemberId = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.LOCAL_MEMBER_ID);

            // Setting up asymmetric cryptography
            String publicKeyFilePath = PropertiesHolder.getInstance().getProperty(FogbowConstants.PUBLIC_KEY_FILE_PATH);
            String privateKeyFilePath = PropertiesHolder.getInstance().getProperty(FogbowConstants.PRIVATE_KEY_FILE_PATH);
            ServiceAsymmetricKeysHolder.getInstance().setPublicKeyFilePath(publicKeyFilePath);
            ServiceAsymmetricKeysHolder.getInstance().setPrivateKeyFilePath(privateKeyFilePath);

            // Setting up plugin
            TokenGeneratorPlugin tokenGeneratorPlugin = TokenGeneratorPluginInstantiator.getTokenGeneratorPlugin();

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
