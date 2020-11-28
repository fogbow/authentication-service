package cloud.fogbow.as.core;

import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.core.systemidp.SystemIdentityProviderPlugin;

public class PluginInstantiator {
    private static ClassFactory classFactory = new ClassFactory();

    public static SystemIdentityProviderPlugin getSystemIdentityProviderPlugin() {
        String className = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.SYSTEM_IDENTITY_PROVIDER_PLUGIN_CLASS_KEY);
        return (SystemIdentityProviderPlugin) PluginInstantiator.classFactory.createPluginInstance(className);
    }
}
