package org.fogbowcloud.as.core.exceptions;

import org.fogbowcloud.as.core.constants.Messages;

public class FogbowAsException extends Exception {
    private static final long serialVersionUID = 1L;

    public FogbowAsException() {
        super(Messages.Exception.FOGBOW_AS);
    }

    public FogbowAsException(String message) {
        super(message);
    }

    public FogbowAsException(String message, Throwable cause) {
        super(message, cause);
    }
}
