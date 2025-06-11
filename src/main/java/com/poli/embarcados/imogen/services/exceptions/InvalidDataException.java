package com.poli.embarcados.imogen.services.exceptions;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String message) {
        super(message);
    }
}
