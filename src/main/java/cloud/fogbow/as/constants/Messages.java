package cloud.fogbow.as.constants;

public class Messages {

    public static class Exception {
        public static final String EXPIRED_TOKEN = "Expired token.";
        public static final String INVALID_TOKEN = "Invalid token value.";
        public static final String LDAP_URL_MISSING = "No LDAP url in configuration file.";
        public static final String UNABLE_TO_LOAD_LDAP_ACCOUNT = "Unable to load account summary from LDAP Network.";
    }

    public static class Fatal {
        public static final String ERROR_READING_PRIVATE_KEY_FILE = "Error reading private key file.";
        public static final String ERROR_READING_PUBLIC_KEY_FILE = "Error reading public key file.";
        public static final String UNABLE_TO_FIND_CLASS_S = "Unable to find class %s.";
    }

    public static class Info {
        public static final String CREATE_TOKEN_REQUEST_RECEIVED_S = "Create token request received with a credentials map of size %s.";
        public static final String RECEIVING_GET_PUBLIC_KEY_REQUEST = "Get public key received.";
        public static final String RECEIVING_GET_VERSION_REQUEST = "Get request for version received.";
    }

    public static class Error {
    }
}
