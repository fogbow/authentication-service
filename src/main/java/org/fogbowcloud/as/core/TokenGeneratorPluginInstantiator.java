package org.fogbowcloud.as.core;

import org.fogbowcloud.as.common.util.HomeDir;
import org.fogbowcloud.as.common.util.PluginFactory;
import org.fogbowcloud.as.core.constants.ConfigurationConstants;
import org.fogbowcloud.as.core.tokengenerator.TokenGeneratorPlugin;
import org.fogbowcloud.as.core.tokengenerator.TokenGeneratorPluginWrapper;
import org.fogbowcloud.as.common.util.PropertiesUtil;

import java.util.Properties;

public class TokenGeneratorPluginInstantiator {
    private static PluginFactory pluginFactory = new PluginFactory();

    public static TokenGeneratorPlugin getTokenGeneratorPlugin(String confFile) {
        Properties properties = PropertiesUtil.readProperties(confFile);
        String className = properties.getProperty(ConfigurationConstants.TOKEN_GENERATOR_PLUGIN_CLASS);
        String pluginConfFile = properties.getProperty(ConfigurationConstants.TOKEN_GENERATOR_CONF_FILE);
        return new TokenGeneratorPluginWrapper((TokenGeneratorPlugin)
                TokenGeneratorPluginInstantiator.pluginFactory.createPluginInstance(className, (HomeDir.getPath() + pluginConfFile)));
    }
}
