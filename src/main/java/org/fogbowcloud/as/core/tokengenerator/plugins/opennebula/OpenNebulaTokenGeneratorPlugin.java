package org.fogbowcloud.as.core.tokengenerator.plugins.opennebula;

import org.apache.log4j.Logger;
import org.fogbowcloud.as.common.constants.FogbowConstants;
import org.fogbowcloud.as.common.exceptions.*;
import org.fogbowcloud.as.core.PropertiesHolder;
import org.fogbowcloud.as.core.constants.ConfigurationConstants;
import org.fogbowcloud.as.core.constants.Messages;
import org.fogbowcloud.as.core.tokengenerator.TokenGeneratorPlugin;
import org.fogbowcloud.as.core.tokengenerator.plugins.AttributeJoiner;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.user.UserPool;

import java.util.HashMap;
import java.util.Map;

public class OpenNebulaTokenGeneratorPlugin implements TokenGeneratorPlugin {
    private static final Logger LOGGER = Logger.getLogger(OpenNebulaTokenGeneratorPlugin.class);

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String OPENNEBULA_TOKEN_VALUE_SEPARATOR = ":";

    private OpenNebulaClientFactory factory;
    private String provider;

    public OpenNebulaTokenGeneratorPlugin() throws FatalErrorException {
        this.provider = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.LOCAL_MEMBER_ID);
        this.factory = new OpenNebulaClientFactory();
    }

    /*
     * The userId is the same as the userName, because the userName is unique in Opennebula
     */
    @Override
    public String createTokenValue(Map<String, String> userCredentials) throws FogbowException {
        String username = userCredentials.get(USERNAME);
        String password = userCredentials.get(PASSWORD);

        if (userCredentials == null || username == null || password == null) {
            throw new InvalidParameterException(Messages.Exception.NO_USER_CREDENTIALS);
        }

        String openNebulaTokenValue = username + OPENNEBULA_TOKEN_VALUE_SEPARATOR + password;
        if (!isAuthenticated(openNebulaTokenValue)) {
            LOGGER.error(org.fogbowcloud.as.common.constants.Messages.Exception.AUTHENTICATION_ERROR);
            throw new UnauthenticatedUserException();
        }

        Map<String, String> attributes = new HashMap<>();
        attributes.put(FogbowConstants.PROVIDER_ID_KEY, this.provider);
        attributes.put(FogbowConstants.USER_ID_KEY, username);
        attributes.put(FogbowConstants.USER_NAME_KEY, username);
        attributes.put(FogbowConstants.TOKEN_VALUE_KEY, openNebulaTokenValue);
        return AttributeJoiner.join(attributes);
    }

    /*
     * Using the Opennebula Java Library it is necessary to do some operation in the cloud to check if the
     * user is authenticated. We opted to use UserPool.
     * TODO: check to request directly in the XML-RPC API
     */
    protected boolean isAuthenticated(String openNebulaTokenValue) {
        final Client client;
        final UserPool userPool;
        try {
            client = this.factory.createClient(openNebulaTokenValue);
            userPool = this.factory.createUserPool(client);
        } catch (UnexpectedException e) {
            LOGGER.error(org.fogbowcloud.as.common.constants.Messages.Exception.UNEXPECTED_ERROR, e);
            return false;
        }

        OneResponse info = userPool.info();
        if (info.isError()) {
            LOGGER.error(String.format(
                    org.fogbowcloud.as.common.constants.Messages.Exception.GENERIC_EXCEPTION, info.getMessage()));
            return false;
        }
        return true;
    }

    protected void setFactory(OpenNebulaClientFactory factory) {
        this.factory = factory;
    }

    protected OpenNebulaClientFactory getFactory() {
        return factory;
    }
}
