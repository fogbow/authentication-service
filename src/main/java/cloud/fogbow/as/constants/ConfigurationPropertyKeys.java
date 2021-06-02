package cloud.fogbow.as.constants;

public class ConfigurationPropertyKeys {
    // Service configuration
    public static final String PROVIDER_ID_KEY = "provider_id";
    public static final String BUILD_NUMBER_KEY = "build_number";
    public static final String TOKEN_EXPIRATION_INTERVAL = "token_expiration_interval";

    // Plugins
    public static final String SYSTEM_IDENTITY_PROVIDER_PLUGIN_CLASS_KEY = "system_identity_provider_plugin_class";
    public static final String SYSTEM_ROLE_PLUGIN_CLASS_KEY = "system_role_plugin_class";

    // Federation Identity Plugins configurations
    public static final String CLOUD_IDENTITY_PROVIDER_URL_KEY = "cloud_identity_provider_url";
    public static final String CLOUDSTACK_URL_KEY = "cloudstack_url";
    public static final String LDAP_BASE_KEY = "ldap_base";
    public static final String LDAP_URL_KEY = "ldap_url";
    public static final String LDAP_ENCRYPT_TYPE_KEY = "ldap_encrypt_type";
    public static final String OPENNEBULA_URL_KEY = "opennebula_url";
    public static final String OPENSTACK_KEYSTONE_V3_URL_KEY = "openstack_keystone_v3_url";
    public static final String SHIB_PUBLIC_FILE_PATH_KEY = "shib_public_key_file_path";
    
    // Federation Roles configurations
    public static final String DEFAULT_ROLE_KEY = "default_role";
    public static final String ROLES_KEY = "roles";
}
