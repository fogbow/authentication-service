package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.OpenNebulaUser;

public class OpenNebulaSystemUser extends OneToOneMappableSystemUser {
    public OpenNebulaSystemUser(String identityProviderId, OpenNebulaUser cloudUser) {
        super(cloudUser.getId(), cloudUser.getName(), identityProviderId, cloudUser.getToken());
    }
}
