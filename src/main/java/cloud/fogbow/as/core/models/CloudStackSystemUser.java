package cloud.fogbow.as.core.models;

import java.util.Map;

import cloud.fogbow.common.models.CloudStackUser;

public class CloudStackSystemUser extends CloudProvidedSystemUser implements OneToOneMappableSystemUser<CloudStackUser> {
    private String domain;
    private Map<String, String> cookieHeaders;

    public CloudStackSystemUser(String identityProviderId, CloudStackUser cloudUser) {
        super(identityProviderId, cloudUser);
        this.domain = cloudUser.getDomain();
        this.cookieHeaders = cloudUser.getCookieHeaders();
    }

    public String getDomain() {
        return domain;
    }

    public Map<String, String> getCookieHeaders() {
        return cookieHeaders;
    }

    @Override
    public CloudStackUser generateCloudUser() {
        return new CloudStackUser(super.getId(), super.getName(), super.getToken(), this.getDomain(), this.getCookieHeaders());
    }
}
