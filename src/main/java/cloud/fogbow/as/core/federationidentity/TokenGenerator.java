package cloud.fogbow.as.core.federationidentity;

import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.exceptions.FatalErrorException;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.models.FederationUser;
import org.apache.log4j.Logger;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.as.constants.Messages;

import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

public class TokenGenerator {
    private static final Logger LOGGER = Logger.getLogger(TokenGenerator.class);

    private FederationIdentityProviderPlugin embeddedPlugin;

    private RSAPrivateKey privateKey;

    public TokenGenerator(FederationIdentityProviderPlugin embeddedPlugin) {
        this.embeddedPlugin = embeddedPlugin;
        try {
            this.privateKey = ServiceAsymmetricKeysHolder.getInstance().getPrivateKey();
        } catch (IOException | GeneralSecurityException e) {
            throw new FatalErrorException(Messages.Fatal.ERROR_READING_PRIVATE_KEY_FILE, e);
        }
    }

    public String createTokenValue(Map<String, String> userCredentials, String publicKeyString)
            throws FogbowException {
        FederationUser federationUser = this.embeddedPlugin.getFederationUser(userCredentials);
        return AuthenticationUtil.createFogbowToken(federationUser, this.privateKey, publicKeyString);
    }
}
