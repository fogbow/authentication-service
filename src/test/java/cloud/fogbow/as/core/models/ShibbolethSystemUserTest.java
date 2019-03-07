package cloud.fogbow.as.core.models;

import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.util.SystemUserUtil;
import org.junit.Assert;
import org.junit.Test;

public class ShibbolethSystemUserTest {
    // Test if an ShibbolethSystemUser is correctly seralized and later deserialized
    @Test
    public void testSerializationDeserealizationOfShibbolethUserObject() throws UnexpectedException {
        // Setup
        ShibbolethSystemUser systemUser = createUser();

        // Exercise
        String serializedUser = SystemUserUtil.serialize(systemUser);
        ShibbolethSystemUser recoveredSystemUser = (ShibbolethSystemUser) SystemUserUtil.deserialize(serializedUser);

        // Verify
        Assert.assertEquals(systemUser, recoveredSystemUser);
    }

    private ShibbolethSystemUser createUser() {
        return new ShibbolethSystemUser("fakeId", "fakeName", "fakeProviderId",
                "fakeAssertionUrl", "fakeSamlAttributes");
    }
}
