package cloud.fogbow.as.core.systemidp.plugins.openstack.v3;

import java.util.Map;

import cloud.fogbow.as.core.models.OpenStackV3SystemUser;
import cloud.fogbow.as.core.systemidp.SystemIdentityProviderPlugin;
import cloud.fogbow.common.models.OpenStackV3User;
import cloud.fogbow.common.plugins.cloudidp.openstack.v3.OpenStackIdentityProviderPlugin;
import cloud.fogbow.as.core.PropertiesHolder;
import org.apache.log4j.Logger;
import cloud.fogbow.common.exceptions.FatalErrorException;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.as.constants.ConfigurationPropertyKeys;

public class OpenStackSystemIdentityProviderPlugin implements SystemIdentityProviderPlugin<OpenStackV3SystemUser> {
    private static final Logger LOGGER = Logger.getLogger(OpenStackSystemIdentityProviderPlugin.class);

    private OpenStackIdentityProviderPlugin identityProviderPlugin;
    private String identityProviderId;

    public OpenStackSystemIdentityProviderPlugin() throws FatalErrorException {
        this.identityProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.PROVIDER_ID_KEY);
        String identityUrl = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.OPENSTACK_KEYSTONE_V3_URL_KEY);
        this.identityProviderPlugin = new OpenStackIdentityProviderPlugin(identityUrl);
    }

    @Override
    public OpenStackV3SystemUser getSystemUser(Map<String, String> credentials) throws FogbowException {
        OpenStackV3User cloudUser = this.identityProviderPlugin.getCloudUser(credentials);
        return new OpenStackV3SystemUser(this.identityProviderId, cloudUser);
    }
}
