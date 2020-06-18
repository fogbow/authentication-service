package cloud.fogbow.as.constants;

public class Messages {

    public static class Exception {
        public static final String ERROR_READING_PRIVATE_KEY_FILE = "Error reading private key file.";
        public static final String ERROR_READING_PUBLIC_KEY_FILE = "Error reading public key file.";
        public static final String EXPIRED_TOKEN = "Expired token.";
        public static final String INSTANCE_NOT_FOUND = "Instance not found.";
        public static final String INVALID_ALGORITHM_OR_ENCODING = "Invalid LDAP encryption algorithm or encoding.";
        public static final String INVALID_CREDENTIALS = "Invalid credentials.";
        public static final String INVALID_TOKEN = "Invalid token.";
        public static final String MISSING_LDAP_ENDPOINT = "No LDAP endpoint in configuration file.";
        public static final String UNABLE_TO_LOAD_LDAP_INFO = "Unable to load account information from LDAP service.";
        public static final String UNABLE_TO_FIND_CLASS_S = "Unable to find class %s.";
    }

    public static class Log {
        public static final String AUTHENTICATION_ERROR = "Authentication error.";
        public static final String CREATE_TOKEN_REQUEST_RECEIVED_S = "Create token request received with a credentials map of size %s.";
        public static final String INVALID_FORMAT_SECRET = "Invalid format for the secret.";
        public static final String OPERATION_RETURNED_ERROR_S = "Operation returned error: %s.";
        public static final String RECEIVING_GET_PUBLIC_KEY_REQUEST = "Get public key received.";
        public static final String SECRET_ALREADY_EXISTS = "Secret already exists.";
        public static final String SECRET_CREATED_BEFORE_AS_START_TIME = "Secret was created before the start time of the AS service.";
        public static final String SECRET_VALIDATED = "Valid secret received.";
    }
}
