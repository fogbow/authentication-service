package cloud.fogbow.as.constants;

public class ApiDocumentation {
    public static class ApiInfo {
        public static final String API_TITLE = "Fogbow Authentication Service (AS) API";
        public static final String API_DESCRIPTION = "This API allows clients to create authenticated tokens "
                                                   + "that they can later use to authenticate themselves with "
                                                   + "other Fogbow services. The tokens are encrypted using "
                                                   + "the public key of the target service, thus, clients need "
                                                   + "a different token for each target service with which "
                                                   + "they need to interact.";
    }

    public static class Token {
        public static final String API = "Creates tokens that clients can later use to authenticate themeselves.";
        public static final String CREATE_OPERATION = "Creates a token.";
        public static final String CREATE_REQUEST_BODY =
                "The body of the request must specify the client credentials that are needed to authenticate " +
                "the client. Credentials are specified as a map, and the values and keys required vary, depending " +
                "on how the AS backend is configured (this is defined by the SystemIdentityProviderPlugin " +
                "implementation used). It is also necessary to provide the public key of the target service with " +
                "which the client will interact. Tokens are encrypted using this key, so that only the target " +
                "service is able to decrypt the token.";
    }

    public static class Model {
        public static final String CREDENTIALS = "{\"username\": \"myUserName\", \"password\": \"myPassword\"}";
        public static final String TOKEN = "AOgqYhXIsTsbGgJ8TiKfIxQh1SGRN03w5KesX4Vzd+2XNQbOKxHpgx4UAxgOfT+cU" +
                "uptzGLiixr4tJWaqbkPB1djx6f6P8X6tDrEZXbsyUAAj53eTi6AJ/Rh9qfdDd9XnqLjnLbU7+96gmnKIcgs9Yc1eMcDe" +
                "g4kuruMd6tl7Iarvls9Gn1zqLxyhc3K1JTtys9vhwwPZoSMHJeNG7vflCGh+YxDPx1A2d0nw+wEHDQksWct9+RKEgUkl" +
                "KYhGOQa11RMZn+Nsj7t9UZnQ5KwQEyAPBKSFgEuXShQvKXgmwLjUw0/E34qacwQzL/c4MoU6756/zf4l5h7VC8yjz7Lm" +
                "A==!^!MXfZhdcTdonx8fjVPTJ4dVEYlf4KISyP8Sad1G0FwTs/nz23vchkf54jXq/asApSEOaXejOtk3HFv/Efgxz1c8" +
                "hylPQgwpdpZEK4MvSnNrlVvVBio6D125hMi6gW4HNFWoeRJG6PQTZu/XD9gMM6Zg8M78P+qQ4dXrlFUK4DHnjbhETapl" +
                "7JTkdp2d8uipRrBObvhtKjFtU72ftu6OasQCEnRFRjpUXxXrz7yNXytp4waEsGQtrjTvlN59obPUD43u8NeURqQknXSN" +
                "6VPtFNechcCgzFU9rS7iFEspmrZTDDIo8L1Jrpz3s6UUKrEgLuapKBqojphSZd5mwNVm6PGiC2TO8gFvNX2uagmI8Tdg" +
                "PA+RvSvdVnmQq1MasTaQTHTfrjytrfgSN+QvdO/6rdrkSFdRyyZNJ2E1CQ7v9IY+WcgAzlXbTtUMzSHRmrhCdyoPFmQu" +
                "4pJQy4rhZhF+G4Oawd/KUzpf9mM5yz6OEk/Idy7vkXCoB9YXJGtyuW34gNFlDsR1lOfHOX2ScMJEHRv7Vp8VIR3MqL7O" +
                "ewGg/0m7lnSoYH+Ln9eNOzMWtfznPJT6yGvLbLnbfkW6oTW/jX9twfUEJ9rOAJNrN7mWcnUJTwI5UWu77gwmZ858Rr6E" +
                "AeHj3IJOvPlogluttQQAnhoPQ4OKUZtqjt7CslNLjeGqbWJZ5dL3gVSov306FwfF3WpviYcaZlWPJwVKjr5VGY5nC9Vm" +
                "4bZVbBwZZk/2S9DokBsgGHXNdgtwSsO0do/Qx9gP4Qk+XLa48Y0hQ+Q3+f9KgX4u3ilxumgJqK8/caTO24NlXBGfhFvn" +
                "fNkA9dsp99h/6GTkZF5B8CuOkYZTHJ+aJBjiUAmacplyEhf/zxGqpQ98sDfozp50+C5/trj0mToPo1jTjetuXIPUE+zJ" +
                "E+rQaXhyRjbXFdn0X3djUY1oUv+Q13S+NyCb8r7bxK8Ltj2J3hl2P5SVak2X462F2JJ0ttzDBVXMsN3Vws5U4cHYpn+K" +
                "kBMKN9c+Vulh0UuXOmAWD8vn1zkiMWby/aE0/uF4h4QSytU3wrlzttcNDJbDJ7qcQhT24pPH+N0mYu+HjaZDqnylpzQc" +
                "lAxph72yXytck8tCdEwCDG5+s6BAqoa4A=";
    }
}
