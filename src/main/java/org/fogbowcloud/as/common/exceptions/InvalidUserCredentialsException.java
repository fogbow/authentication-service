package org.fogbowcloud.as.common.exceptions;

import org.fogbowcloud.as.common.constants.Messages;

public class InvalidUserCredentialsException extends FogbowException {
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
