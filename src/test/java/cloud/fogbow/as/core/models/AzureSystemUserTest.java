package cloud.fogbow.as.core.models;

import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.models.AzureUser;
import cloud.fogbow.common.models.SystemUser;
import org.junit.Assert;
import org.junit.Test;

public class AzureSystemUserTest {
    // Test if an AzureSystemUser is correctly seralized and later deserialized
    @Test
    public void testSerializationAndDeserealizationOfAzureUserObject() throws UnexpectedException {
        // Setup
        AzureSystemUser systemUser = createUser();

        // Exercise
        String serializedUser = SystemUser.serialize(systemUser);
        AzureSystemUser recoveredSystemUser = (AzureSystemUser) SystemUser.deserialize(serializedUser);

        // Verify
        Assert.assertEquals(systemUser, recoveredSystemUser);
    }

    private AzureSystemUser createUser() {
        AzureUser cloudUser = new AzureUser("fakeUserId", "fakeUserName", "fakeClientId",
                "fakeTenantId", "fakeClientKey","fakeSubscriptionId");
        return new AzureSystemUser("fakeProviderId", cloudUser);
    }
}
