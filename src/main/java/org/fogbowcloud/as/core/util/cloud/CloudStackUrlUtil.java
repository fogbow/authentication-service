package org.fogbowcloud.as.core.util.cloud;

import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.fogbowcloud.as.common.exceptions.InvalidParameterException;
import org.fogbowcloud.as.core.constants.Messages;

public class CloudStackUrlUtil {
    private static final Logger LOGGER = Logger.getLogger(CloudStackUrlUtil.class);

    private static final String COMMAND = "command";
    private static final String JSON = "json";
    private static final String RESPONSE_FORMAT = "response";

    public static URIBuilder createURIBuilder(String endpoint, String command) throws InvalidParameterException {
        try {
            URIBuilder uriBuilder = new URIBuilder(endpoint);
            uriBuilder.addParameter(COMMAND, command);
            uriBuilder.addParameter(RESPONSE_FORMAT, JSON);
            return uriBuilder;
        } catch (Exception e) {
            throw new InvalidParameterException(Messages.Exception.WRONG_URI_SYNTAX);
        }
    }
}
