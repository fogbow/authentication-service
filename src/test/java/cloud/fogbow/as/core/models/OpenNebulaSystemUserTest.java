package cloud.fogbow.as.core.models;

import cloud.fogbow.common.exceptions.InternalServerErrorException;
import cloud.fogbow.common.models.OpenNebulaUser;
import cloud.fogbow.common.models.SystemUser;
import org.junit.Assert;
import org.junit.Test;

public class OpenNebulaSystemUserTest {
    // Test if an OpenNebulaSystemUser is correctly seralized and later deserialized
    @Test
    public void testSerializationDeserealizationOfOpenNebulaUserObject() throws InternalServerErrorException {
        // Setup
        OpenNebulaSystemUser systemUser = createUser();

        // Exercise
        String serializedUser = SystemUser.serialize(systemUser);
        OpenNebulaSystemUser recoveredSystemUser = (OpenNebulaSystemUser) SystemUser.deserialize(serializedUser);

        // Verify
        Assert.assertEquals(systemUser, recoveredSystemUser);
    }

    private OpenNebulaSystemUser createUser() {
        OpenNebulaUser cloudUser = new OpenNebulaUser("fakeUserId", "fakeUserName", "fakeToken");
        return new OpenNebulaSystemUser("fakeProviderId", cloudUser);
    }
}
