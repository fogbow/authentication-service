package cloud.fogbow.as.core.models;

import cloud.fogbow.common.models.OpenStackV3User;

public class OpenStackV3UnscopedSystemUser extends OpenStackV3SystemUser {

   public OpenStackV3UnscopedSystemUser(String identityProviderId, OpenStackV3User cloudUser) {
      super(identityProviderId, cloudUser);
   }
}
