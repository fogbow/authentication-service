package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.CloudStackUser;

import java.util.HashMap;

public class CloudStackSystemUser extends CloudProvidedSystemUser implements OneToOneMappableSystemUser {
    private String domain;
    private HashMap<String, String> cookieHeaders;

    public CloudStackSystemUser(String identityProviderId, CloudStackUser cloudUser) {
        super(identityProviderId, cloudUser);
        this.domain = cloudUser.getDomain();
        this.cookieHeaders = cloudUser.getCookieHeaders();
    }

    public String getDomain() {
        return domain;
    }

    public HashMap<String, String> getCookieHeaders() {
        return cookieHeaders;
    }

    @Override
    public CloudStackUser generateCloudUser() {
        return new CloudStackUser(super.getId(), super.getName(), super.getToken(), this.getDomain(), this.getCookieHeaders());
    }
}
