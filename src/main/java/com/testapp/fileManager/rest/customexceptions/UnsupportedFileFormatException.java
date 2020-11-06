package com.testapp.fileManager.rest.customexceptions;

public class UnsupportedFileFormatException extends RuntimeException {

    public UnsupportedFileFormatException() {
    }

    public UnsupportedFileFormatException(String message) {
        super(message);
    }

    public UnsupportedFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedFileFormatException(Throwable cause) {
        super(cause);
    }

}
