package org.fogbowcloud.as.core.exceptions;

import org.fogbowcloud.as.core.constants.Messages;

public class InvalidUserCredentialsException extends FogbowAsException {
    private static final long serialVersionUID = 1L;

    public InvalidUserCredentialsException() {
        super(Messages.Exception.INVALID_CREDENTIALS);
    }

    public InvalidUserCredentialsException(String message) {
        super(message);
    }

    public InvalidUserCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
