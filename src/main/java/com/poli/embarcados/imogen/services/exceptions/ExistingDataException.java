package com.poli.embarcados.imogen.services.exceptions;

public class ExistingDataException extends RuntimeException {
    public ExistingDataException(String message) {
        super(message);
    }
}
