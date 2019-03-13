package cloud.fogbow.as.core.systemidp.plugins.opennebula;

import cloud.fogbow.as.core.models.OpenNebulaSystemUser;
import cloud.fogbow.as.core.systemidp.SystemIdentityProviderPlugin;
import cloud.fogbow.common.exceptions.*;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.common.models.OpenNebulaUser;
import cloud.fogbow.common.plugins.cloudidp.opennebula.OpenNebulaIdentityProviderPlugin;
import cloud.fogbow.common.util.PropertiesUtil;

import org.apache.log4j.Logger;
import cloud.fogbow.as.constants.ConfigurationPropertyKeys;

import java.util.Map;
import java.util.Properties;

public class OpenNebulaSystemIdentityProviderPlugin implements SystemIdentityProviderPlugin<OpenNebulaSystemUser> {
    
	private static final Logger LOGGER = Logger.getLogger(OpenNebulaSystemIdentityProviderPlugin.class);
    
    private OpenNebulaIdentityProviderPlugin identityProviderPlugin;
    private String identityProviderId;

    public OpenNebulaSystemIdentityProviderPlugin() throws FatalErrorException {
        this.identityProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.LOCAL_MEMBER_ID_KEY);
    	String identityUrl = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.OPENNEBULA_URL_KEY);
        this.identityProviderPlugin = new OpenNebulaIdentityProviderPlugin(identityUrl);
    }

    @Override
    public OpenNebulaSystemUser getSystemUser(Map<String, String> credentials) throws FogbowException {
        OpenNebulaUser cloudUser = this.identityProviderPlugin.getCloudUser(credentials);
        return new OpenNebulaSystemUser(this.identityProviderId, cloudUser);

    }
}
