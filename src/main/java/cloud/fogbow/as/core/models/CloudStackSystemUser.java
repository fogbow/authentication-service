package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.CloudStackUser;

import java.util.HashMap;

public class CloudStackSystemUser extends OneToOneMappableSystemUser {
    private HashMap<String, String> cookieHeaders;

    public CloudStackSystemUser(String identityProviderId, CloudStackUser cloudUser) {
        super(cloudUser.getId(), cloudUser.getName(), identityProviderId, cloudUser.getToken());
        this.cookieHeaders = cloudUser.getCookieHeaders();
    }

    public HashMap<String, String> getCookieHeaders() {
        return cookieHeaders;
    }
}
