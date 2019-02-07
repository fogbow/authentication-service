package cloud.fogbow.as.constants;

public class ApiDocumentation {
    public static class Token {
        public static final String API = "Creates tokens for the users of the federation.";
        public static final String CREATE_OPERATION = "Creates a token.";
        public static final String CREATE_REQUEST_BODY =
                "The body of the request must specify the values and keys for the type of TokenGeneratorPlugin\n" +
                "your AS deploy is using. For instance, if you are using LDAP, you should provide a username\n" +
                "and a password. For Openstack Keystone, you should provide a username, a password, a domain\n" +
                "and a project name.";
        public static final String PUBLIC_KEY = "The public key used to encrypt the generated token.";
    }

    public static class Version {
        public static final String API = "Queries the version of the service's API.";
        public static final String GET_OPERATION = "Returns the version of the API.";
    }

    public static class PublicKey {
        public static final String API = "Queries the public key of the service.";
        public static final String GET_OPERATION = "Returns the public key of the service.";
    }
}
