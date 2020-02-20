package cloud.fogbow.as.core.systemidp.plugins.aws.v2;

import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.as.core.models.AwsV2SystemUser;
import cloud.fogbow.as.core.systemidp.SystemIdentityProviderPlugin;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.models.AwsV2User;
import cloud.fogbow.common.plugins.cloudidp.aws.v2.AwsV2IdentityProviderPlugin;

import java.util.Map;

public class AwsV2SystemIdentityProviderPlugin implements SystemIdentityProviderPlugin<AwsV2SystemUser> {

    private AwsV2IdentityProviderPlugin identityProviderPlugin;
    private String identityProviderId;

    public AwsV2SystemIdentityProviderPlugin() {
        this.identityProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.PROVIDER_ID_KEY);
        this.identityProviderPlugin = new AwsV2IdentityProviderPlugin();
    }

    @Override
    public AwsV2SystemUser getSystemUser(Map<String, String> credentials) throws FogbowException {
        AwsV2User cloudUser = this.identityProviderPlugin.getCloudUser(credentials);
        return new AwsV2SystemUser(this.identityProviderId, cloudUser);
    }
}
