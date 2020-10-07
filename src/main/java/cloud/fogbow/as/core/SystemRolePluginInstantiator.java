package cloud.fogbow.as.core;

import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.core.role.SystemRolePlugin;
import cloud.fogbow.as.core.role.plugins.DefaultSystemRolePlugin;

public class SystemRolePluginInstantiator {
    
    private static ClassFactory classFactory = new ClassFactory();

    public static SystemRolePlugin getSystemRolePlugin() {
        
        if (PropertiesHolder.getInstance().getProperties().containsKey(ConfigurationPropertyKeys.SYSTEM_ROLE_PLUGIN_CLASS_KEY)) {
            String className = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.SYSTEM_ROLE_PLUGIN_CLASS_KEY);
            return (SystemRolePlugin) SystemRolePluginInstantiator.classFactory.createPluginInstance(className);
        } else {
            return new DefaultSystemRolePlugin();
        }
    }
}
