package org.fogbowcloud.as.core;

import org.fogbowcloud.as.common.exceptions.FatalErrorException;
import org.fogbowcloud.as.common.util.HomeDir;
import org.fogbowcloud.as.common.util.PropertiesUtil;
import org.fogbowcloud.as.core.constants.SystemConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertiesHolder {
    private Properties properties;
    private static PropertiesHolder instance;

    private PropertiesHolder() throws FatalErrorException {
        String path = HomeDir.getPath();
        List<String> configFilesNames = new ArrayList<>();
        configFilesNames.add(path + SystemConstants.AS_CONF_FILE_NAME);
        this.properties = PropertiesUtil.readProperties(configFilesNames);
    }

    public static synchronized PropertiesHolder getInstance() throws FatalErrorException {
        if (instance == null) {
            instance = new PropertiesHolder();
        }
        return instance;
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public String getProperty(String propertyName, String defaultPropertyValue) {
        String propertyValue = this.properties.getProperty(propertyName, defaultPropertyValue);
        if (propertyValue.trim().isEmpty()) {
            propertyValue = defaultPropertyValue;
        }
        return propertyValue;
    }

    public Properties getProperties() {
        return this.properties;
    }
}
