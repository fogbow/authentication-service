package org.fogbowcloud.as.core.util.cloud;

import org.apache.http.client.utils.URIBuilder;
import org.fogbowcloud.as.common.exceptions.InvalidParameterException;

public abstract class CloudStackRequest {
    private URIBuilder uriBuilder;

    protected CloudStackRequest() {}

    protected CloudStackRequest(String baseEndpoint) throws InvalidParameterException {
        this.uriBuilder = CloudStackUrlUtil.createURIBuilder(baseEndpoint, getCommand());
    }

    protected void addParameter(String parameter, String value) {
        if (value != null) {
            this.uriBuilder.addParameter(parameter, value);
        }
    }

    public URIBuilder getUriBuilder() {
        return this.uriBuilder;
    }

    public abstract String getCommand();
}
