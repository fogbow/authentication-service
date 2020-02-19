package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.AzureUser;
import cloud.fogbow.common.models.CloudUser;

public class AzureSystemUser extends CloudProvidedSystemUser implements OneToOneMappableSystemUser<AzureUser> {

	public AzureSystemUser(String identityProviderId, CloudUser cloudUser) {
		super(identityProviderId, cloudUser);
	}

	@Override
	public AzureUser generateCloudUser() {
		return new AzureUser(this.getId(), this.getName());
	}

}
