package cloud.fogbow.as.core.systemidp.plugins.googlecloud;

import java.util.Map;

import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.as.core.models.GoogleCloudSystemUser;
import cloud.fogbow.as.core.systemidp.SystemIdentityProviderPlugin;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.models.GoogleCloudUser;
import cloud.fogbow.common.plugins.cloudidp.googlecloud.GoogleCloudIdentityProviderPlugin;

public class GoogleCloudSystemIdentityProviderPlugin implements SystemIdentityProviderPlugin<GoogleCloudSystemUser> {
	
	private GoogleCloudIdentityProviderPlugin identityProviderPlugin;
    private String identityProviderId;

    public GoogleCloudSystemIdentityProviderPlugin() {
        this.identityProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.PROVIDER_ID_KEY);
        this.identityProviderPlugin = new GoogleCloudIdentityProviderPlugin();
    }

    @Override
    public GoogleCloudSystemUser getSystemUser(Map<String, String> credentials) throws FogbowException {
        GoogleCloudUser cloudUser = this.identityProviderPlugin.getCloudUser(credentials);
        return new GoogleCloudSystemUser(this.identityProviderId, cloudUser);
    }
}
