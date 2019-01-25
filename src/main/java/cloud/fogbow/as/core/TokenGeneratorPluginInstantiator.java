package cloud.fogbow.as.core;

import cloud.fogbow.common.util.PluginFactory;
import cloud.fogbow.as.core.constants.ConfigurationConstants;
import cloud.fogbow.as.core.tokengenerator.TokenGeneratorPlugin;

public class TokenGeneratorPluginInstantiator {
    private static PluginFactory pluginFactory = new PluginFactory();

    public static TokenGeneratorPlugin getTokenGeneratorPlugin() {
        String className = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.TOKEN_GENERATOR_PLUGIN_CLASS);
        return (TokenGeneratorPlugin) TokenGeneratorPluginInstantiator.pluginFactory.createPluginInstance(className);
    }
}
