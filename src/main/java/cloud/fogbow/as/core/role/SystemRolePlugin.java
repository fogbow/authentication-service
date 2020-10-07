package cloud.fogbow.as.core.role;

import cloud.fogbow.common.models.SystemUser;

public interface SystemRolePlugin {
    /**
     * Assigns the corresponding roles to an authenticated user.
     * 
     * @param user a SystemUser object that represents a successfully authenticated user and
     * used to store the user roles.
     */
    public void setUserRoles(SystemUser user);
}
