package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.CloudStackUser;

import java.util.HashMap;

public class CloudStackSystemUser extends OneToOneMappableSystemUser {
    private HashMap<String, String> cookieHeader;

    public CloudStackSystemUser(String identityProviderId, CloudStackUser cloudUser) {
        super(cloudUser.getId(), cloudUser.getName(), identityProviderId, cloudUser.getToken());
        this.cookieHeader = cloudUser.getCookieHeader();
    }

    public HashMap<String, String> getCookieHeader() {
        return cookieHeader;
    }
}
