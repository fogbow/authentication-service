package cloud.fogbow.as.core.role;

import cloud.fogbow.common.models.SystemUser;

public interface SystemRolePlugin {
    public void setUserRoles(SystemUser user);
}
