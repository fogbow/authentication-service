package cloud.fogbow.as.common.exceptions;

import cloud.fogbow.as.common.constants.Messages;

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
