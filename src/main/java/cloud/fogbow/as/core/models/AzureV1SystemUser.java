package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.AzureV1User;
import cloud.fogbow.common.models.CloudUser;

public class AzureV1SystemUser extends CloudProvidedSystemUser implements OneToOneMappableSystemUser<AzureV1User> {

	public AzureV1SystemUser(String identityProviderId, CloudUser cloudUser) {
		super(identityProviderId, cloudUser);
	}

	@Override
	public AzureV1User generateCloudUser() {
		return new AzureV1User(this.getId(), this.getName(), this.getToken());
	}

}
