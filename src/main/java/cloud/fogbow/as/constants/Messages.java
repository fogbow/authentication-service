package cloud.fogbow.as.constants;

public class Messages {

    public static class Exception {
        public static final String LDAP_URL_MISSING = "No LDAP url in configuration file.";
        public static final String NO_USER_CREDENTIALS = "No user credentials given.";
        public static final String UNABLE_TO_LOAD_LDAP_ACCOUNT = "Unable to load account summary from LDAP Network.";
        public static final String WRONG_URI_SYNTAX = "Wrong syntax for endpoint %s.";
    }

    public static class Fatal {
        public static final String ERROR_READING_PRIVATE_KEY_FILE = "Error reading private key file.";
        public static final String ERROR_READING_PUBLIC_KEY_FILE = "Error reading public key file.";
        public static final String INVALID_SERVICE_URL = "Invalid service URL: %s.";
    }

    public static class Info {
        public static final String RECEIVING_CREATE_TOKEN_REQUEST = "Create token request received with a credentials map of size %s.";
        public static final String RECEIVING_GET_PUBLIC_KEY_REQUEST = "Get public key received.";
        public static final String RECEIVING_GET_VERSION_REQUEST = "Get request for version received.";
        public static final String USER_POOL_LENGTH = "User pool length: %s.";
    }

    public static class Error {
        public static final String ERROR_WHILE_CREATING_CLIENT = "Error while creating client.";
        public static final String ERROR_WHILE_GETTING_USERS = "Error while getting info about users: %s.";
        public static final String UNABLE_TO_GET_TOKEN_FROM_JSON = "Unable to get token from json.";
    }
}
