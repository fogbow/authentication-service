package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.SystemUser;

public class OneToOneMappableSystemUser extends SystemUser {
    private String token;

    public OneToOneMappableSystemUser(String userId, String userName, String identityProviderId, String token) {
        super(userId, userName, identityProviderId);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
