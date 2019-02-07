package cloud.fogbow.as.core.tokengenerator.plugins.opennebula;

import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.as.core.PropertiesHolder;
import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.constants.Messages;
import org.apache.log4j.Logger;
import org.opennebula.client.*;
import org.opennebula.client.group.GroupPool;
import org.opennebula.client.image.ImagePool;
import org.opennebula.client.template.TemplatePool;
import org.opennebula.client.user.UserPool;

public class OpenNebulaClientFactory {
	private final static Logger LOGGER = Logger.getLogger(OpenNebulaClientFactory.class);

    private String endpoint;

	public OpenNebulaClientFactory() {
		this.endpoint = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.OPENNEBULA_URL_KEY);
	}

	public OpenNebulaClientFactory(String endpoint) {
		this.endpoint = endpoint;
	}

	public Client createClient(String federationTokenValue) throws UnexpectedException {
		try {
			return new Client(federationTokenValue, this.endpoint);
		} catch (ClientConfigurationException e) {
			LOGGER.error(Messages.Error.ERROR_WHILE_CREATING_CLIENT, e);
			throw new UnexpectedException();
		}
	}

	public UserPool createUserPool(Client client) throws UnexpectedException {
		UserPool userpool = (UserPool) generateOnePool(client, UserPool.class);
 		OneResponse response = userpool.info();
 		if (response.isError()) {
 			LOGGER.error(String.format(Messages.Error.ERROR_WHILE_GETTING_USERS, response.getErrorMessage()));
			throw new UnexpectedException(response.getErrorMessage());
 		}
 		LOGGER.info(String.format(Messages.Info.USER_POOL_LENGTH, userpool.getLength()));
		return userpool;
	}

	protected Pool generateOnePool(Client client, Class classType) {
		if (classType.isAssignableFrom(TemplatePool.class)) {
			return new GroupPool(client);
		} else if (classType.isAssignableFrom(GroupPool.class)) {
			return new GroupPool(client);
		} else if (classType.isAssignableFrom(TemplatePool.class)) {
		    return new TemplatePool(client);
        } else if (classType.isAssignableFrom(ImagePool.class)) {
            return new ImagePool(client);
        } else if (classType.isAssignableFrom(UserPool.class)) {
		    return new UserPool(client);
        }

		return null;
	}
}
