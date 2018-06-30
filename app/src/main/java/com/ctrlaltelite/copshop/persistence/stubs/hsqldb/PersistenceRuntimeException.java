package com.ctrlaltelite.copshop.persistence.stubs.hsqldb;

public class PersistenceRuntimeException extends RuntimeException {
    public PersistenceRuntimeException(final String message, final Exception cause) {
        super(message, cause);
    }

    public PersistenceRuntimeException(final String message) {
        super(message);
    }

    public PersistenceRuntimeException(final Exception cause) {
        super(cause);
    }
}
