package cloud.fogbow.as.core.systemidp.plugins.federated;

import java.util.Map;

import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.core.ClassFactory;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.as.core.systemidp.SystemIdentityProviderPlugin;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.models.SystemUser;

public class FederatedSystemIdentityProviderPlugin implements SystemIdentityProviderPlugin<SystemUser> {

    private SystemIdentityProviderPlugin<?> internalIdentityProviderPlugin;
    
    public FederatedSystemIdentityProviderPlugin(SystemIdentityProviderPlugin<?> internalIdentityProviderPlugin) {
        this.internalIdentityProviderPlugin = internalIdentityProviderPlugin;
    }
    
    public FederatedSystemIdentityProviderPlugin() {
        String internalIdentityPluginClassName = PropertiesHolder.getInstance().
                getProperty(ConfigurationPropertyKeys.INTERNAL_IDENTITY_PROVIDER_PLUGIN_CLASS_KEY);
        this.internalIdentityProviderPlugin = (SystemIdentityProviderPlugin<?>) 
                new ClassFactory().createPluginInstance(internalIdentityPluginClassName);
    }
    
    @Override
    public SystemUser getSystemUser(Map<String, String> userCredentials) throws FogbowException {
        SystemUser user = this.internalIdentityProviderPlugin.getSystemUser(userCredentials);
        String federationId = userCredentials.get(FogbowConstants.FEDERATION_ID_KEY);
        
        if (federationId != null && !federationId.isEmpty()) {
            user.setId(user.getId() + FogbowConstants.FEDERATION_ID_SEPARATOR + federationId);
        }
        
        return user;
    }
}
