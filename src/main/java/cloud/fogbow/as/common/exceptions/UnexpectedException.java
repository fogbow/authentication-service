package cloud.fogbow.as.common.exceptions;

import cloud.fogbow.as.common.constants.Messages;

public class UnexpectedException extends FogbowException {
    private static final long serialVersionUID = 1L;

    public UnexpectedException() {
        super(Messages.Exception.UNEXPECTED);
    }

    public UnexpectedException(String message) {
        super(message);
    }

    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
