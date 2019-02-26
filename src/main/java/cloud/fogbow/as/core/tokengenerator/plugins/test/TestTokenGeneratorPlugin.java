package cloud.fogbow.as.core.tokengenerator.plugins.test;

import cloud.fogbow.as.core.tokengenerator.TokenGeneratorPlugin;
import cloud.fogbow.as.core.tokengenerator.plugins.AttributeJoiner;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.constants.Messages;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.InvalidParameterException;
import cloud.fogbow.common.exceptions.UnauthenticatedUserException;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class TestTokenGeneratorPlugin implements TokenGeneratorPlugin {

    private final Logger LOGGER;

    private final String USER_NAME_FIELD = "username";
    private final String PASSWORD_FIELD = "password";

    private final String FAKE_PROVIDER_ID = "fake-provider-id";
    private final String FAKE_USER_ID = "fake-user-id";
    private final String FAKE_TOKEN_VALUE = "fake-token-value";

    public TestTokenGeneratorPlugin() {
        this.LOGGER = Logger.getLogger(TestTokenGeneratorPlugin.class);
    }

    @Override
    public String createTokenValue(Map<String, String> userCredentials) throws FogbowException {

        String username = userCredentials.get(USER_NAME_FIELD);
        String password = userCredentials.get(PASSWORD_FIELD);

        if (username == null || password == null) {
            throw new InvalidParameterException(cloud.fogbow.as.constants.Messages.Exception.NO_USER_CREDENTIALS);
        }

        if (!isAuthenticated(password)) {
            LOGGER.error(Messages.Exception.AUTHENTICATION_ERROR);
            throw new UnauthenticatedUserException();
        }

        Map<String, String> attributes = new HashMap<>();

        attributes.put(FogbowConstants.USER_NAME_KEY, username);
        attributes.put(FogbowConstants.PROVIDER_ID_KEY, FAKE_PROVIDER_ID);
        attributes.put(FogbowConstants.USER_ID_KEY, FAKE_USER_ID);
        attributes.put(FogbowConstants.TOKEN_VALUE_KEY, FAKE_TOKEN_VALUE);

        return AttributeJoiner.join(attributes);
    }

    private boolean isAuthenticated(String password) {
        return password.equals("pass") || password.equals("password");
    }
}

