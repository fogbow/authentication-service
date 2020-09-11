package cloud.fogbow.as.core.systemidp.plugins.cloudstack;

import java.util.Map;

import cloud.fogbow.as.core.models.CloudStackSystemUser;
import cloud.fogbow.as.core.systemidp.SystemIdentityProviderPlugin;
import cloud.fogbow.common.models.CloudStackUser;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.common.plugins.cloudidp.cloudstack.v4_9.CloudStackIdentityProviderPlugin;
import org.apache.log4j.Logger;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.as.constants.ConfigurationPropertyKeys;

public class CloudStackSystemIdentityProviderPlugin implements SystemIdentityProviderPlugin<CloudStackSystemUser> {
    private static final Logger LOGGER = Logger.getLogger(CloudStackSystemIdentityProviderPlugin.class);

    private CloudStackIdentityProviderPlugin identityProviderPlugin;
    private String identityProviderId;

    public CloudStackSystemIdentityProviderPlugin() {
        this.identityProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.PROVIDER_ID_KEY);
        String cloudStackUrl = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.CLOUDSTACK_URL_KEY);
        this.identityProviderPlugin = new CloudStackIdentityProviderPlugin(cloudStackUrl);
    }

    @Override
    public CloudStackSystemUser getSystemUser(Map<String, String> credentials) throws FogbowException {
        CloudStackUser cloudUser = this.identityProviderPlugin.getCloudUser(credentials);
        return new CloudStackSystemUser(this.identityProviderId, cloudUser);
    }
}
