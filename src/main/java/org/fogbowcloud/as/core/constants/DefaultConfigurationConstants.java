package org.fogbowcloud.as.core.constants;

import java.util.concurrent.TimeUnit;

public class DefaultConfigurationConstants {
    // AS CONF DEFAULTS
    public static final String HTTP_REQUEST_TIMEOUT = Long.toString(TimeUnit.MINUTES.toMillis(1));
    public static final String BUILD_NUMBER = "[testing mode]";
}
