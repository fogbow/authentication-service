package org.fogbowcloud.as.core.constants;

public class Messages {

    public static class Exception {
        public static final String AUTHENTICATION_ERROR = "Authentication error.";
        public static final String FATAL_ERROR = "Fatal error.";
        public static final String FOGBOW_AS = "Fogbow AS exception.";
        public static final String GENERIC_EXCEPTION = "Operation returned error: %s";
        public static final String INVALID_CREDENTIALS = "Invalid credentials.";
        public static final String INVALID_PARAMETER = "Invalid parameter.";
        public static final String INVALID_TOKEN = "Invalid token value.";
        public static final String LDAP_URL_MISSING = "No LDAP url in configuration file.";
        public static final String NO_USER_CREDENTIALS = "No user credentials given.";
        public static final String UNABLE_TO_LOAD_LDAP_ACCOUNT = "Unable to load account summary from LDAP Network.";
        public static final String UNABLE_TO_SIGN_LDAP_TOKEN = "Unable to sign LDAP token.";
        public static final String UNAVAILABLE_PROVIDER = "Provider is not available.";
        public static final String UNEXPECTED_ERROR = "Unexpected error.";
        public static final String WRONG_URI_SYNTAX = "Wrong syntax for endpoint %s.";
    }

    public static class Warn {
        public static final String UNABLE_TO_GENERATE_SIGNATURE = "Unable to generate signature.";
    }

    public static class Fatal {
        public static final String ERROR_READING_PRIVATE_KEY_FILE = "Error reading private key file.";
        public static final String ERROR_READING_PUBLIC_KEY_FILE = "Error reading public key file.";
        public static final String INVALID_SERVICE_URL = "Invalid service URL: %s.";
        public static final String PROPERTY_FILE_NOT_FOUND = "Property file %s not found.";
        public static final String UNABLE_TO_FIND_CLASS = "Unable to find class %s.";
        public static final String UNABLE_TO_INITIALIZE_HTTP_REQUEST_UTIL = "Unable to initialize HttpRequestUtil.";
    }

    public static class Info {
        public static final String RECEIVING_CREATE_TOKEN_REQUEST = "Create token request received with a credentials map of size %s.";
        public static final String RECEIVING_GET_VERSION_REQUEST = "Get request for version received.";
        public static final String USER_POOL_LENGTH = "User pool length: %s.";
    }

    public static class Error {
        public static final String ERROR_WHILE_CONSUMING_RESPONSE = "Error while consuming response %s.";
        public static final String ERROR_WHILE_CREATING_CLIENT = "Error while creating client.";
        public static final String ERROR_WHILE_GETTING_USERS = "Error while getting info about users: %s.";
        public static final String UNABLE_TO_CLOSE_FILE = "Unable to close file %s.";
        public static final String UNABLE_TO_GET_TOKEN_FROM_JSON = "Unable to get token from json.";
    }
}
