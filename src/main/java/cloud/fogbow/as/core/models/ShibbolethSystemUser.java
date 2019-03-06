package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.SystemUser;

public class ShibbolethSystemUser extends SystemUser {
    private String assertionUrl;
    private String samlAttributes;

    public ShibbolethSystemUser(String userId, String userName, String identityProviderId, String assertionUrl,
                                String samlAttributes) {
        super(userId, userName, identityProviderId);
        this.assertionUrl = assertionUrl;
        this.samlAttributes = samlAttributes;
    }

    public String getAssertionUrl() {
        return assertionUrl;
    }

    public String getSamlAttributes() {
        return samlAttributes;
    }
}
