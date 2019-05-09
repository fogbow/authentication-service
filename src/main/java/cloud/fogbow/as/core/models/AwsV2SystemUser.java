package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.AwsV2User;

public class AwsV2SystemUser extends CloudProvidedSystemUser implements OneToOneMappableSystemUser<AwsV2User> {

    public AwsV2SystemUser(String identityProviderId, AwsV2User cloudUser) {
        super(identityProviderId, cloudUser);
    }

    @Override
    public AwsV2User generateCloudUser() {
        return new AwsV2User(this.getId(), this.getName(), this.getToken());
    }
}
