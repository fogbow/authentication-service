package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.OpenStackV3User;

public class OpenStackV3SystemUser extends CloudProvidedSystemUser {

    public OpenStackV3SystemUser(String identityProviderId, OpenStackV3User cloudUser) {
        super(identityProviderId, cloudUser);
    }
}
