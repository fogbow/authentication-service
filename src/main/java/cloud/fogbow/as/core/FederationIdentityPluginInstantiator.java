package cloud.fogbow.as.core;

import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.core.federationidentity.FederationIdentityProviderPlugin;
import cloud.fogbow.common.util.PluginFactory;

public class FederationIdentityPluginInstantiator {
    private static PluginFactory pluginFactory = new PluginFactory();

    public static FederationIdentityProviderPlugin getTokenGeneratorPlugin() {
        String className = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.TOKEN_GENERATOR_PLUGIN_CLASS_KEY);
        return (FederationIdentityProviderPlugin) FederationIdentityPluginInstantiator.pluginFactory.createPluginInstance(className);
    }
}
