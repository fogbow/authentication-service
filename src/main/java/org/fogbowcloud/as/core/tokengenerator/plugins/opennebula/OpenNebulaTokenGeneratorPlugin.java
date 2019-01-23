package org.fogbowcloud.as.core.tokengenerator.plugins.opennebula;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.fogbowcloud.as.core.PropertiesHolder;
import org.fogbowcloud.as.core.constants.ConfigurationConstants;
import org.fogbowcloud.as.core.constants.Messages;
import org.fogbowcloud.as.core.exceptions.*;
import org.fogbowcloud.as.common.util.FogbowAuthenticationHolder;
import org.fogbowcloud.as.core.tokengenerator.TokenGeneratorPlugin;
import org.fogbowcloud.as.common.util.PropertiesUtil;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.user.UserPool;

import java.util.Map;
import java.util.Properties;

public class OpenNebulaTokenGeneratorPlugin implements TokenGeneratorPlugin {

    private static final Logger LOGGER = Logger.getLogger(OpenNebulaTokenGeneratorPlugin.class);

    public static final int FEDERATION_TOKEN_PARAMETER_SIZE = 5;
    public static final int RAW_TOKEN_TOKEN_PARAMETER_SIZE = FEDERATION_TOKEN_PARAMETER_SIZE - 1;
    public static final int PROVIDER_ID_TOKEN_VALUE_PARAMETER = 0;
    public static final int ONE_TOKEN_VALUE_PARAMETER = 1;
    public static final int USERNAME_TOKEN_VALUE_PARAMETER = 2;
    public static final int USER_ID_TOKEN_VALUE_PARAMETER = 3;

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String OPENNEBULA_TOKEN_VALUE_SEPARATOR = ":";
    public static final String OPENNEBULA_FIELD_SEPARATOR = "#&#";

    private FogbowAuthenticationHolder fogbowAuthenticationHolder;
    private OpenNebulaClientFactory factory;
    private Properties properties;
    private String provider;

    public OpenNebulaTokenGeneratorPlugin(String confFilePath) throws FatalErrorException {
        this.properties = PropertiesUtil.readProperties(confFilePath);
        this.provider = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.LOCAL_MEMBER_ID);
        this.fogbowAuthenticationHolder = FogbowAuthenticationHolder.getInstance();
        this.factory = new OpenNebulaClientFactory(confFilePath);
    }

    /*
     * The userId is the equals userName, because the userName is unique in the Opennebula
     */
    @Override
    public String createTokenValue(Map<String, String> userCredentials) throws FogbowAsException {
        String username = userCredentials.get(USERNAME);
        String password = userCredentials.get(PASSWORD);

        if (userCredentials == null || username == null || password == null) {
            throw new InvalidParameterException(Messages.Exception.NO_USER_CREDENTIALS);
        }

        String openNebulaTokenValue = username + OPENNEBULA_TOKEN_VALUE_SEPARATOR + password;
        if (!isAuthenticated(openNebulaTokenValue)) {
            LOGGER.error(Messages.Exception.AUTHENTICATION_ERROR);
            throw new UnauthenticatedUserException();
        }

        String userId = username;
        String[] federationTokenParameters = new String[RAW_TOKEN_TOKEN_PARAMETER_SIZE];
        federationTokenParameters[PROVIDER_ID_TOKEN_VALUE_PARAMETER] = this.provider;
        federationTokenParameters[ONE_TOKEN_VALUE_PARAMETER] = openNebulaTokenValue;
        federationTokenParameters[USERNAME_TOKEN_VALUE_PARAMETER] = username;
        federationTokenParameters[USER_ID_TOKEN_VALUE_PARAMETER] = userId;
        String rawTokenValue = StringUtils.join(federationTokenParameters, OPENNEBULA_FIELD_SEPARATOR);

        final String signature = this.fogbowAuthenticationHolder.createSignature(rawTokenValue);
        return rawTokenValue + OPENNEBULA_FIELD_SEPARATOR + signature;
    }

    /*
     * Using the Opennebula Java Library it is necessary to do some operation in the cloud to check if the
     * user is authenticated. We opted to use UserPool.
     * TODO check to request directly in the XML-RPC API
     */
    protected boolean isAuthenticated(String openNebulaTokenValue) {
        final Client client;
        final UserPool userPool;
        try {
            client = this.factory.createClient(openNebulaTokenValue);
            userPool = this.factory.createUserPool(client);
        } catch (UnexpectedException e) {
            LOGGER.error(Messages.Exception.UNEXPECTED_ERROR, e);
            return false;
        }

        OneResponse info = userPool.info();
        if (info.isError()) {
            LOGGER.error(String.format(Messages.Exception.GENERIC_EXCEPTION, info.getMessage()));
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
