package cloud.fogbow.as.core.models;

import cloud.fogbow.common.exceptions.InternalServerErrorException;
import cloud.fogbow.common.models.SystemUser;
import org.junit.Assert;
import org.junit.Test;

public class ShibbolethSystemUserTest {
    // Test if an ShibbolethSystemUser is correctly seralized and later deserialized
    @Test
    public void testSerializationDeserealizationOfShibbolethUserObject() throws InternalServerErrorException {
        // Setup
        ShibbolethSystemUser systemUser = createUser();

        // Exercise
        String serializedUser = SystemUser.serialize(systemUser);
        ShibbolethSystemUser recoveredSystemUser = (ShibbolethSystemUser) SystemUser.deserialize(serializedUser);

        // Verify
        Assert.assertEquals(systemUser, recoveredSystemUser);
    }

    private ShibbolethSystemUser createUser() {
        return new ShibbolethSystemUser("fakeId", "fakeName", "fakeProviderId",
                "fakeAssertionUrl", "fakeSamlAttributes");
    }
}
