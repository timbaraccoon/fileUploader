package com.testapp.fileManager.rest.customexceptions;

public class CustomFileNotFoundException extends RuntimeException {

    public CustomFileNotFoundException() {
    }

    public CustomFileNotFoundException(String message) {
        super(message);
    }

    public CustomFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomFileNotFoundException(Throwable cause) {
        super(cause);
    }

}
