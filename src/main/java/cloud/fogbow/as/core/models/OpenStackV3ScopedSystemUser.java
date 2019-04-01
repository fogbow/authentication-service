package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.OpenStackV3User;

public class OpenStackV3ScopedSystemUser extends OpenStackV3SystemUser implements OneToOneMappableSystemUser {
    private String projectId;

    public OpenStackV3ScopedSystemUser(String identityProviderId, OpenStackV3User cloudUser) {
        super(identityProviderId, cloudUser);
        this.projectId = cloudUser.getProjectId();
    }

    public String getProjectId() {
        return projectId;
    }

    @Override
    public OpenStackV3User generateCloudUser() {
        return new OpenStackV3User(super.getId(), super.getName(), super.getToken(), this.getProjectId());
    }
}
