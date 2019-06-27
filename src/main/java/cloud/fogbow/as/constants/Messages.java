package cloud.fogbow.as.constants;

public class Messages {

    public static class Exception {
        public static final String GENERIC_EXCEPTION = "Operation returned error: %s";
        public static final String EXPIRED_TOKEN = "Expired token.";
        public static final String INVALID_CREDENTIALS = "Invalid credentials.";
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
        public static final String INVALID_FORMAT_SECRET = "Invalid format for the secret.";
        public static final String RECEIVING_GET_PUBLIC_KEY_REQUEST = "Get public key received.";
        public static final String RECEIVING_GET_VERSION_REQUEST = "Get request for version received.";
        public static final String SECRET_ALREADY_EXISTS = "Secret already exists.";
        public static final String SECRET_CREATED_BEFORE_AS_START_TIME = "Secret was created before the start time of the AS service.";
    }

    public static class Error {
    }
}
