package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.CloudUser;

public interface OneToOneMappableSystemUser<T extends CloudUser> {
    public T generateCloudUser();
}
