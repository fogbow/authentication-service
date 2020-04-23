package cloud.fogbow.as.core.systemidp.plugins.aws.v2;

import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.as.core.models.AwsV2SystemUser;
import cloud.fogbow.as.core.systemidp.SystemIdentityProviderPlugin;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.models.AwsV2User;
import cloud.fogbow.common.plugins.cloudidp.aws.v2.AwsIdentityProviderPlugin;

import java.util.Map;

public class AwsSystemIdentityProviderPlugin implements SystemIdentityProviderPlugin<AwsV2SystemUser> {

    private AwsIdentityProviderPlugin identityProviderPlugin;
    private String identityProviderId;

    public AwsSystemIdentityProviderPlugin() {
        this.identityProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.PROVIDER_ID_KEY);
        this.identityProviderPlugin = new AwsIdentityProviderPlugin();
    }

    @Override
    public AwsV2SystemUser getSystemUser(Map<String, String> credentials) throws FogbowException {
        AwsV2User cloudUser = this.identityProviderPlugin.getCloudUser(credentials);
        return new AwsV2SystemUser(this.identityProviderId, cloudUser);
    }
}
