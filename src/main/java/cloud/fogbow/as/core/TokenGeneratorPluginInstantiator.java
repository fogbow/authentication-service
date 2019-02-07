package cloud.fogbow.as.core;

import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.core.tokengenerator.TokenGeneratorPlugin;

public class TokenGeneratorPluginInstantiator {
    private static PluginFactory pluginFactory = new PluginFactory();

    public static TokenGeneratorPlugin getTokenGeneratorPlugin() {
        String className = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.TOKEN_GENERATOR_PLUGIN_CLASS_KEY);
        return (TokenGeneratorPlugin) TokenGeneratorPluginInstantiator.pluginFactory.createPluginInstance(className);
    }
}
