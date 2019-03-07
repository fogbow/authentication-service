package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.CloudStackUser;

public class CloudStackSystemUser extends OneToOneMappableSystemUser {
    public CloudStackSystemUser(String identityProviderId, CloudStackUser cloudUser) {
        super(cloudUser.getId(), cloudUser.getName(), identityProviderId, cloudUser.getToken());
    }
}
