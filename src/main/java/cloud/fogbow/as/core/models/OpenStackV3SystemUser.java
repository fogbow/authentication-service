package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.OpenStackV3User;

public class OpenStackV3SystemUser extends OneToOneMappableSystemUser {
    private String projectId;

    public OpenStackV3SystemUser(String identityProviderId, OpenStackV3User cloudUser) {
        super(cloudUser.getId(), cloudUser.getName(), identityProviderId, cloudUser.getToken());
        this.projectId = cloudUser.getProjectId();
    }

    public String getProjectId() {
        return projectId;
    }
}
