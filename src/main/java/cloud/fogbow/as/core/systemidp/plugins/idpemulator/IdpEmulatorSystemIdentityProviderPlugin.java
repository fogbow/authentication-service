package cloud.fogbow.as.core.systemidp.plugins.idpemulator;

import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.constants.Messages;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.as.core.systemidp.SystemIdentityProviderPlugin;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.InvalidParameterException;
import cloud.fogbow.common.exceptions.UnauthenticatedUserException;
import cloud.fogbow.common.models.SystemUser;
import org.apache.log4j.Logger;

import java.util.Map;

public class IdpEmulatorSystemIdentityProviderPlugin implements SystemIdentityProviderPlugin<SystemUser> {

    private final Logger LOGGER = Logger.getLogger(IdpEmulatorSystemIdentityProviderPlugin.class);;

    private static final String FOGBOW_ALLOWED_USER = "fogbow";
    private static final String USER_NAME_FIELD = "username";
    private static String identityProviderId;

    public IdpEmulatorSystemIdentityProviderPlugin() {
        this.identityProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.PROVIDER_ID_KEY);
    }

    @Override
    public SystemUser getSystemUser(Map<String, String> userCredentials) throws FogbowException {
        String username = userCredentials.get(USER_NAME_FIELD);

        if (username == null) {
            throw new InvalidParameterException(cloud.fogbow.as.constants.Messages.Exception.INVALID_CREDENTIALS);
        }

        if (!isAuthenticated(username)) {
            LOGGER.error(Messages.Log.AUTHENTICATION_ERROR);
            throw new UnauthenticatedUserException();
        }

        return new SystemUser(username, username, this.identityProviderId);
    }

    private boolean isAuthenticated(String username) {
        return username.equals(FOGBOW_ALLOWED_USER);
    }
}