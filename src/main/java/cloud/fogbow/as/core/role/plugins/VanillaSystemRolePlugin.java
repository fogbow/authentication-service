package cloud.fogbow.as.core.role.plugins;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.constants.SystemConstants;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.as.core.role.SystemRolePlugin;
import cloud.fogbow.common.models.SystemUser;

public class VanillaSystemRolePlugin implements SystemRolePlugin {

	private Map<String, Set<String>> usersWithSpecialRoles;
	private String defaultRole;
	
	public VanillaSystemRolePlugin() {
		defaultRole = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.DEFAULT_ROLE_KEY);
		getUsersWithSpecialRoles();
	}

	private void getUsersWithSpecialRoles() {
		usersWithSpecialRoles = new HashMap<String, Set<String>>();
		String rolesNamesString = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.ROLES_KEY);
		
		if (!rolesNamesString.isEmpty()) {
			for (String roleName : rolesNamesString.trim().split(SystemConstants.ROLE_NAMES_SEPARATOR)) {
				getUserNamesWithRole(roleName);
			}			
		}
	}

	private void getUserNamesWithRole(String roleName) {
		String userNamesWithRoleString = PropertiesHolder.getInstance().getProperty(roleName);
		
		if (!userNamesWithRoleString.isEmpty()) {
			for (String userName : userNamesWithRoleString.trim().split(SystemConstants.ROLE_NAMES_SEPARATOR)) {
				if (!usersWithSpecialRoles.containsKey(userName)) {
					usersWithSpecialRoles.put(userName, new HashSet<String>());
				}
				
				usersWithSpecialRoles.get(userName).add(roleName);
			}					
		}
	}
	
	@Override
	public void setUserRoles(SystemUser user) {
		String userName = user.getName();
		
		if (usersWithSpecialRoles.containsKey(userName)) {
			user.setUserRoles(usersWithSpecialRoles.get(userName));			
		} else {
			HashSet<String> defaultRoleSet = new HashSet<String>();
			defaultRoleSet.add(defaultRole);
			user.setUserRoles(defaultRoleSet);
		}
	}
}