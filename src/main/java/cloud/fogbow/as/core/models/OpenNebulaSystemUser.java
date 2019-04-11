package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.OpenNebulaUser;

public class OpenNebulaSystemUser extends CloudProvidedSystemUser implements OneToOneMappableSystemUser<OpenNebulaUser> {
    public OpenNebulaSystemUser(String identityProviderId, OpenNebulaUser cloudUser) {
        super(identityProviderId, cloudUser);
    }

    @Override
    public OpenNebulaUser generateCloudUser() {
        return new OpenNebulaUser(this.getId(), this.getName(), this.getToken());
    }
}
