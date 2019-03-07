package cloud.fogbow.as.core.systemidp;

import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.models.SystemUser;

import java.util.Map;

public interface SystemIdentityProviderPlugin<T extends SystemUser> {
    /**
     * Authenticates the user using the provided credentials and in case of success returns a representation of
     * the authenticated user.
     *
     * @param userCredentials a map containing the credentials to authenticate the user with the identity provider
     *                        service.
     * @return a SystemUser object that represents the successfully authenticated user and uniquely identifies
     * the user in the whole system.
     */
    public T getSystemUser(Map<String, String> userCredentials) throws FogbowException;
}
