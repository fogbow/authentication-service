package org.fogbowcloud.as.common.exceptions;

import org.fogbowcloud.as.common.constants.Messages;

public class FogbowException extends Exception {
    private static final long serialVersionUID = 1L;

    public FogbowException() {
        super(Messages.Exception.FOGBOW);
    }

    public FogbowException(String message) {
        super(message);
    }

    public FogbowException(String message, Throwable cause) {
        super(message, cause);
    }
}
