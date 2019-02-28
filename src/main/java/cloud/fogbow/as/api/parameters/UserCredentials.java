package cloud.fogbow.as.api.parameters;

import java.util.Map;

public class UserCredentials {
    private Map<String, String> credentials;
    private String publicKey;

    public Map<String, String> getCredentials() {
        return credentials;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
