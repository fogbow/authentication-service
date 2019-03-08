package cloud.fogbow.as.core.models;

import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.models.CloudStackUser;
import cloud.fogbow.common.util.SystemUserUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class CloudStackSystemUserTest {
    // Test if an CloudStackSystemUser is correctly seralized and later deserialized
    @Test
    public void testSerializationDeserealizationOfCloudStackUserObject() throws UnexpectedException {
        // Setup
        CloudStackSystemUser systemUser = createUser();

        // Exercise
        String serializedUser = SystemUserUtil.serialize(systemUser);
        CloudStackSystemUser recoveredSystemUser = (CloudStackSystemUser) SystemUserUtil.deserialize(serializedUser);

        // Verify
        Assert.assertEquals(systemUser, recoveredSystemUser);
    }

    private CloudStackSystemUser createUser() {
        CloudStackUser cloudUser = new CloudStackUser("fakeUserId", "fakeUserName", "fakeToken",
                new HashMap<>());
        return new CloudStackSystemUser("fakeProviderId", cloudUser);
    }
}