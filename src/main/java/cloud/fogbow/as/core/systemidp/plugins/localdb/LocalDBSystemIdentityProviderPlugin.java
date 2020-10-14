package cloud.fogbow.as.core.systemidp.plugins.localdb;

import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.constants.Messages;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.as.core.systemidp.SystemIdentityProviderPlugin;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.InvalidParameterException;
import cloud.fogbow.common.exceptions.UnauthenticatedUserException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.util.HomeDir;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LocalDBSystemIdentityProviderPlugin implements SystemIdentityProviderPlugin<SystemUser> {

    private final Logger LOGGER = Logger.getLogger(LocalDBSystemIdentityProviderPlugin.class);;

    private static final String PASSWORD_FIELD_KEY = "password";
    private static final String USER_NAME_FIELD_KEY = "username";
    private static final String DB_FILE_PATH_KEY = "db_file_path";
    private static final String DEFAULT_FILE_NAME = "users.db";

    private String identityProviderId;

    private Map<String, String> usersDb;

    public LocalDBSystemIdentityProviderPlugin() {
        this.identityProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.PROVIDER_ID_KEY);
        this.usersDb = getUsersMap();
    }

    @Override
    public SystemUser getSystemUser(Map<String, String> userCredentials) throws FogbowException {
        String username = userCredentials.get(USER_NAME_FIELD_KEY);
        String password = userCredentials.get(PASSWORD_FIELD_KEY);

        if (username == null || password == null) {
            throw new InvalidParameterException(Messages.Exception.INVALID_CREDENTIALS);
        }

        if (!isAuthenticated(username, password)) {
            LOGGER.error(Messages.Log.AUTHENTICATION_ERROR);
            throw new UnauthenticatedUserException();
        }

        return new SystemUser(username, username, this.identityProviderId);
    }

    private boolean isAuthenticated(String username, String password) {
        String correctPassword = this.usersDb.get(username);
        return password.equals(correctPassword);
    }

    private Map<String, String> getUsersMap() {
        Map<String, String> usersMap = new HashMap<>();
        BufferedReader csvReader = null;
        String row;
        try {
            String fileName = PropertiesHolder.getInstance().getProperty(DB_FILE_PATH_KEY, DEFAULT_FILE_NAME);
            csvReader = new BufferedReader(new FileReader(HomeDir.getPath() + "/" + fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return usersMap;
        }
        while (true) {
            try {
                if (!((row = csvReader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            String[] data = row.split(",");
            usersMap.put(data[0], data[1]);
        }
        try {
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usersMap;
    }
}