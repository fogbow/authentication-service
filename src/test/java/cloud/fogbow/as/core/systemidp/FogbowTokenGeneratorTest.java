package cloud.fogbow.as.core.systemidp;

import cloud.fogbow.as.core.role.SystemRolePlugin;
import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;

@PrepareForTest({AuthenticationUtil.class, ServiceAsymmetricKeysHolder.class})
@RunWith(PowerMockRunner.class)
public class FogbowTokenGeneratorTest {

    public static final String VALID_TOKEN = "token";
    public static final String VALID_PUBLIC_KEY = "validPublicKey";

    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String IDENTITY_PROVIDER_ID = "identityProviderId";

    @Test
    public void testCreateToken() throws FogbowException {
        PowerMockito.mockStatic(ServiceAsymmetricKeysHolder.class);
        BDDMockito.given(ServiceAsymmetricKeysHolder.getInstance()).willReturn(Mockito.mock(ServiceAsymmetricKeysHolder.class));

        PowerMockito.mockStatic(AuthenticationUtil.class);
        BDDMockito.given(AuthenticationUtil.createFogbowToken(Mockito.any(SystemUser.class), Mockito.any(RSAPrivateKey.class), Mockito.anyString())).willReturn(VALID_TOKEN);

        SystemIdentityProviderPlugin identityProviderPlugin = Mockito.mock(SystemIdentityProviderPlugin.class);
        Mockito.when(identityProviderPlugin.getSystemUser(Mockito.anyMap())).thenReturn(new SystemUser(USER_ID, USER_NAME, IDENTITY_PROVIDER_ID));

        SystemRolePlugin roleProviderPlugin = Mockito.mock(SystemRolePlugin.class);
        
        FogbowTokenGenerator fogbowTokenGenerator = new FogbowTokenGenerator(identityProviderPlugin, roleProviderPlugin);
        String token = fogbowTokenGenerator.createToken(new HashMap<>(), VALID_PUBLIC_KEY);

        Assert.assertEquals(VALID_TOKEN, token);
    }
}
