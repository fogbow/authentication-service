package cloud.fogbow.as.core.models;

import cloud.fogbow.common.exceptions.InternalServerErrorException;
import cloud.fogbow.common.models.OpenStackV3User;
import cloud.fogbow.common.models.SystemUser;
import org.junit.Assert;
import org.junit.Test;

public class OpenStackV3SystemUserTest {
    // Test if an OpenStackV3SystemUser is correctly seralized and later deserialized
    @Test
    public void testSerializationDeserealizationOfOpenstackV3UserObject() throws InternalServerErrorException {
        // Setup
        OpenStackV3SystemUser systemUser = createUser();

        // Exercise
        String serializedUser = SystemUser.serialize(systemUser);
        OpenStackV3SystemUser recoveredSystemUser = (OpenStackV3SystemUser) SystemUser.deserialize(serializedUser);

        // Verify
        Assert.assertEquals(systemUser, recoveredSystemUser);
    }

    private OpenStackV3SystemUser createUser() {
        OpenStackV3User cloudUser = new OpenStackV3User("fakeUserId", "fakeUserName", "fakeToken", "fakeProjectId");
        return new OpenStackV3SystemUser("fakeProviderId", cloudUser);
    }
}
