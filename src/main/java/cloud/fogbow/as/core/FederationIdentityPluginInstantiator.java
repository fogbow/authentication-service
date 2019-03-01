package cloud.fogbow.as.core;

import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.core.federationidentity.FederationIdentityProviderPlugin;
import cloud.fogbow.common.util.ClassFactory;

public class FederationIdentityPluginInstantiator {
    private static ClassFactory classFactory = new ClassFactory();

    public static FederationIdentityProviderPlugin getTokenGeneratorPlugin() {
        String className = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.TOKEN_GENERATOR_PLUGIN_CLASS_KEY);
        return (FederationIdentityProviderPlugin) FederationIdentityPluginInstantiator.classFactory.createPluginInstance(className);
    }
}
