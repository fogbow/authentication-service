package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.CloudUser;
import cloud.fogbow.common.models.SystemUser;

public class CloudProvidedSystemUser extends SystemUser {
    public String token;

    public CloudProvidedSystemUser(String identityProviderId, CloudUser cloudUser) {
        super(cloudUser.getId(), cloudUser.getName(), identityProviderId);
        this.token = cloudUser.getToken();
    }

    public String getToken() {
        return this.token;
    }
}
