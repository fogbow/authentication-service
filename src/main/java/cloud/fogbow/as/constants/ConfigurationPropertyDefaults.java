package cloud.fogbow.as.constants;

import java.util.concurrent.TimeUnit;

public class ConfigurationPropertyDefaults {
    // AS CONF DEFAULTS
    public static final String BUILD_NUMBER = "[testing mode]";
    public static final String DEFAULT_TOKEN_EXPIRATION_INTERVAL = String.valueOf(TimeUnit.DAYS.toMillis(1)); // One day
}
