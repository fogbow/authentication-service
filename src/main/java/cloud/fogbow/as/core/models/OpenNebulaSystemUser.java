package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.OpenNebulaUser;

public class OpenNebulaSystemUser extends CloudProvidedSystemUser {
    public OpenNebulaSystemUser(String identityProviderId, OpenNebulaUser cloudUser) {
        super(identityProviderId, cloudUser);
    }
}
