package cloud.fogbow.as.core.federationidentity;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.models.FederationUser;

import java.util.Map;

public interface FederationIdentityProviderPlugin {
    /**
     * Authenticates the user using the provided credentials and in case of success returns a representation of
     * the authenticated user.
     *
     * @param userCredentials a map containing the credentials to authenticate the user with the identity provider service.
     * @return a FederationUser object reprensenting the successfully authenticated user.
     */
    public FederationUser getFederationUser(Map<String, String> userCredentials) throws UnexpectedException, FogbowException;
}
