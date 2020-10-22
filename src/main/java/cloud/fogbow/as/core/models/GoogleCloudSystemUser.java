package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.GoogleCloudUser;

public class GoogleCloudSystemUser extends CloudProvidedSystemUser implements OneToOneMappableSystemUser<GoogleCloudUser> {
	
	private String projectId;

	public GoogleCloudSystemUser(String identityProviderId, GoogleCloudUser cloudUser) {
		super(identityProviderId, cloudUser);
		this.projectId = cloudUser.getProjectId();
	}

	@Override
	public GoogleCloudUser generateCloudUser() {
		return new GoogleCloudUser(this.getId(), this.getName(), this.getToken(), this.projectId);
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
