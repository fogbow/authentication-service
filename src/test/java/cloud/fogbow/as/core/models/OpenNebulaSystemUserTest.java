package cloud.fogbow.as.core.models;

import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.models.OpenNebulaUser;
import cloud.fogbow.common.util.SystemUserUtil;
import org.junit.Assert;
import org.junit.Test;

public class OpenNebulaSystemUserTest {
    // Test if an OpenNebulaSystemUser is correctly seralized and later deserialized
    @Test
    public void testSerializationDeserealizationOfOpenNebulaUserObject() throws UnexpectedException {
        // Setup
        OpenNebulaSystemUser systemUser = createUser();

        // Exercise
        String serializedUser = SystemUserUtil.serialize(systemUser);
        OpenNebulaSystemUser recoveredSystemUser = (OpenNebulaSystemUser) SystemUserUtil.deserialize(serializedUser);

        // Verify
        Assert.assertEquals(systemUser, recoveredSystemUser);
    }

    private OpenNebulaSystemUser createUser() {
        OpenNebulaUser cloudUser = new OpenNebulaUser("fakeUserId", "fakeUserName", "fakeToken");
        return new OpenNebulaSystemUser("fakeProviderId", cloudUser);
    }
}
