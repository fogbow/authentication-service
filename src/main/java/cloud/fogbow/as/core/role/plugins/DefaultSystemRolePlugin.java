package cloud.fogbow.as.core.role.plugins;

import cloud.fogbow.as.core.role.SystemRolePlugin;
import cloud.fogbow.common.models.SystemUser;

public class DefaultSystemRolePlugin implements SystemRolePlugin {

    @Override
    public void setUserRoles(SystemUser user) {
        user.setUserRoles(null);
    }
}
