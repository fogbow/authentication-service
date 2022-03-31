package cloud.fogbow.as.core.systemidp.plugins.federated;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import cloud.fogbow.as.core.systemidp.SystemIdentityProviderPlugin;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.models.SystemUser;

public class FederatedSystemIdentityProviderPluginTest {
    private static final String FEDERATION_ID = "federationId";
    private static final String USER_ID = "userId";
    
    private FederatedSystemIdentityProviderPlugin plugin;
    
    @Test
    public void testGetSystemUserFederationIdIsEmpty() throws FogbowException {
        Map<String, String> credentials = new HashMap<String, String>();
        credentials.put(FogbowConstants.FEDERATION_ID_KEY, "");
        
        SystemUser user = Mockito.mock(SystemUser.class);
        
        SystemIdentityProviderPlugin<SystemUser> internalPlugin = Mockito.mock(SystemIdentityProviderPlugin.class);
        Mockito.when(internalPlugin.getSystemUser(credentials)).thenReturn(user);
        
        plugin = new FederatedSystemIdentityProviderPlugin(internalPlugin);
        
        SystemUser response = plugin.getSystemUser(credentials);
        
        assertEquals(user, response);
        Mockito.verify(internalPlugin).getSystemUser(credentials);
        Mockito.verifyZeroInteractions(user);
    }
    
    @Test
    public void testGetSystemUserFederationIdIsNotEmpty() throws FogbowException {
        Map<String, String> credentials = new HashMap<String, String>();
        credentials.put(FogbowConstants.FEDERATION_ID_KEY, FEDERATION_ID);
        
        SystemUser user = Mockito.mock(SystemUser.class);
        Mockito.when(user.getId()).thenReturn(USER_ID);
        
        SystemIdentityProviderPlugin<SystemUser> internalPlugin = Mockito.mock(SystemIdentityProviderPlugin.class);
        Mockito.when(internalPlugin.getSystemUser(credentials)).thenReturn(user);
        
        plugin = new FederatedSystemIdentityProviderPlugin(internalPlugin);
        
        SystemUser response = plugin.getSystemUser(credentials);
        
        assertEquals(user, response);
        Mockito.verify(internalPlugin).getSystemUser(credentials);
        Mockito.verify(user).setId(USER_ID + FogbowConstants.FEDERATION_ID_SEPARATOR + FEDERATION_ID);
    }
}
