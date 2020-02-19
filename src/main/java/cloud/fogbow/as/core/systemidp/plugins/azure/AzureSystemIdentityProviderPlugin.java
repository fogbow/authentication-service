package cloud.fogbow.as.core.systemidp.plugins.azure;

import java.util.Map;

import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.as.core.models.AzureSystemUser;
import cloud.fogbow.as.core.systemidp.SystemIdentityProviderPlugin;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.models.AzureUser;
import cloud.fogbow.common.plugins.cloudidp.azure.AzureIdentityProviderPlugin;

public class AzureSystemIdentityProviderPlugin implements SystemIdentityProviderPlugin<AzureSystemUser> {

	private AzureIdentityProviderPlugin identityProviderPlugin;
    private String identityProviderId;
    
	public AzureSystemIdentityProviderPlugin() {
		this.identityProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.LOCAL_MEMBER_ID_KEY);
		this.identityProviderPlugin = new AzureIdentityProviderPlugin();
	}

	@Override
	public AzureSystemUser getSystemUser(Map<String, String> credentials) throws FogbowException {
		AzureUser cloudUser = this.identityProviderPlugin.getCloudUser(credentials);
        return new AzureSystemUser(this.identityProviderId, cloudUser);
	}

}
