package org.fogbowcloud.as.core;

import org.fogbowcloud.as.common.util.PluginFactory;
import org.fogbowcloud.as.core.constants.ConfigurationConstants;
import org.fogbowcloud.as.core.tokengenerator.TokenGeneratorPlugin;

public class TokenGeneratorPluginInstantiator {
    private static PluginFactory pluginFactory = new PluginFactory();

    public static TokenGeneratorPlugin getTokenGeneratorPlugin() {
        String className = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.TOKEN_GENERATOR_PLUGIN_CLASS);
        return (TokenGeneratorPlugin) TokenGeneratorPluginInstantiator.pluginFactory.createPluginInstance(className);
    }
}
