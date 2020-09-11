package cloud.fogbow.as.core.systemidp;

import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.exceptions.FatalErrorException;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.InternalServerErrorException;
import cloud.fogbow.common.models.SystemUser;
import org.apache.log4j.Logger;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.as.constants.Messages;

import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

public class FogbowTokenGenerator {
    private static final Logger LOGGER = Logger.getLogger(FogbowTokenGenerator.class);

    private SystemIdentityProviderPlugin embeddedPlugin;

    private RSAPrivateKey privateKey;

    public FogbowTokenGenerator(SystemIdentityProviderPlugin embeddedPlugin) {
        this.embeddedPlugin = embeddedPlugin;
        try {
            this.privateKey = ServiceAsymmetricKeysHolder.getInstance().getPrivateKey();
        } catch (InternalServerErrorException e) {
            throw new FatalErrorException(Messages.Exception.ERROR_READING_PRIVATE_KEY_FILE, e);
        }
    }

    public String createToken(Map<String, String> userCredentials, String publicKeyString) throws FogbowException {
        SystemUser systemUser = this.embeddedPlugin.getSystemUser(userCredentials);
        return AuthenticationUtil.createFogbowToken(systemUser, this.privateKey, publicKeyString);
    }
}
