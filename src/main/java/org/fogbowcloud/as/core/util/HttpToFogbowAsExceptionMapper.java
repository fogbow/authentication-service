package org.fogbowcloud.as.core.util;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.fogbowcloud.as.common.exceptions.*;

public class HttpToFogbowAsExceptionMapper {

    public static void map(HttpResponseException e) throws FogbowException, UnexpectedException {
        switch (e.getStatusCode()) {
            case HttpStatus.SC_FORBIDDEN:
            case HttpStatus.SC_UNAUTHORIZED:
                throw new UnauthenticatedUserException(e.getMessage(), e);
            case HttpStatus.SC_NOT_FOUND:
            case HttpStatus.SC_BAD_REQUEST:
            case HttpStatus.SC_CONFLICT:
            case HttpStatus.SC_NOT_ACCEPTABLE:
                throw new InvalidParameterException(e.getMessage(), e);
            case HttpStatus.SC_GATEWAY_TIMEOUT:
                throw new UnavailableProviderException(e.getMessage(), e);
            default:
                throw new UnexpectedException(e.getMessage(), e);
        }
    }
}
