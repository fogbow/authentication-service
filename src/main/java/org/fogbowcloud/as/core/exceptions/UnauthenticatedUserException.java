package org.fogbowcloud.as.core.exceptions;

import org.fogbowcloud.as.core.constants.Messages;

public class UnauthenticatedUserException extends FogbowAsException {
    private static final long serialVersionUID = 1L;

    public UnauthenticatedUserException() {
        super(Messages.Exception.AUTHENTICATION_ERROR);
    }

    public UnauthenticatedUserException(String message) {
        super(message);
    }

    public UnauthenticatedUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
