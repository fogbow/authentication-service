package cloud.fogbow.as.core.federationidentity.plugins.opennebula;

import cloud.fogbow.as.core.federationidentity.FederationIdentityProviderPlugin;
import cloud.fogbow.common.constants.Messages;
import cloud.fogbow.common.constants.OpenNebulaConstants;
import cloud.fogbow.common.exceptions.*;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.common.models.FederationUser;
import org.apache.log4j.Logger;
import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.user.UserPool;

import java.util.HashMap;
import java.util.Map;

public class OpenNebulaFederationIdentityProviderPlugin implements FederationIdentityProviderPlugin {
    private static final Logger LOGGER = Logger.getLogger(OpenNebulaFederationIdentityProviderPlugin.class);

    private OpenNebulaClientFactory factory;
    private String tokenProviderId;

    public OpenNebulaFederationIdentityProviderPlugin() throws FatalErrorException {
        this.tokenProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.LOCAL_MEMBER_ID_KEY);
        this.factory = new OpenNebulaClientFactory();
    }

    public OpenNebulaFederationIdentityProviderPlugin(OpenNebulaClientFactory factory, String tokenProviderId) {
        this.factory = factory;
        this.tokenProviderId = tokenProviderId;
    }

    /*
     * The userId is the same as the userName, because the userName is unique in Opennebula
     */
    @Override
    public FederationUser getFederationUser(Map<String, String> userCredentials) throws FogbowException {
        if (userCredentials == null) {
            throw new InvalidParameterException(cloud.fogbow.as.constants.Messages.Exception.NO_USER_CREDENTIALS);
        }

        String username = userCredentials.get(OpenNebulaConstants.USERNAME);
        String password = userCredentials.get(OpenNebulaConstants.PASSWORD);

        if (username == null || password == null) {
            throw new InvalidParameterException(cloud.fogbow.as.constants.Messages.Exception.NO_USER_CREDENTIALS);
        }

        String openNebulaTokenValue = username + OpenNebulaConstants.TOKEN_VALUE_SEPARATOR + password;
        if (!isAuthenticated(openNebulaTokenValue)) {
            LOGGER.error(Messages.Exception.AUTHENTICATION_ERROR);
            throw new UnauthenticatedUserException();
        }

        return new FederationUser(this.tokenProviderId, username, username, openNebulaTokenValue, new HashMap<>());
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
            LOGGER.error(Messages.Exception.UNEXPECTED, e);
            return false;
        }

        OneResponse info = userPool.info();
        if (info.isError()) {
            LOGGER.error(String.format(
                    Messages.Exception.GENERIC_EXCEPTION, info.getMessage()));
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
