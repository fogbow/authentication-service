package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.CloudUser;

public interface OneToOneMappableSystemUser {
    public CloudUser generateCloudUser();
}
