package me.xdrop.passlock.exceptions;

public class InvalidDataException extends RuntimeException {

    public InvalidDataException() {
        super();
    }

    public InvalidDataException(String message) {
        super(message);
    }
}
