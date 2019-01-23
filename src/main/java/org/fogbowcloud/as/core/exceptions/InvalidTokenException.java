package org.fogbowcloud.as.core.exceptions;

import org.fogbowcloud.as.core.constants.Messages;

public class InvalidTokenException extends FogbowAsException {
    private static final long serialVersionUID = 1L;

    public InvalidTokenException() {
        super(Messages.Exception.INVALID_TOKEN);
    }

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

}
