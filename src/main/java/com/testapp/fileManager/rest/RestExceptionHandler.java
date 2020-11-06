package com.testapp.fileManager.rest;

import com.testapp.fileManager.rest.customexceptions.CustomFileNotFoundException;
import com.testapp.fileManager.rest.customexceptions.FileStorageException;
import com.testapp.fileManager.rest.customexceptions.FileUploaderErrorResponse;
import com.testapp.fileManager.rest.customexceptions.UnsupportedFileFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<FileUploaderErrorResponse> handleException(CustomFileNotFoundException exc) {
        FileUploaderErrorResponse errResponse = new FileUploaderErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exc.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<FileUploaderErrorResponse>(errResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<FileUploaderErrorResponse> handleException(MaxUploadSizeExceededException exc) {
        FileUploaderErrorResponse errResponse = new FileUploaderErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exc.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<FileUploaderErrorResponse>(errResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<FileUploaderErrorResponse> handleException(FileStorageException exc) {
        FileUploaderErrorResponse errResponse = new FileUploaderErrorResponse(
                HttpStatus.EXPECTATION_FAILED.value(),
                exc.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<FileUploaderErrorResponse>(errResponse, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler
    public ResponseEntity<FileUploaderErrorResponse> handleException(UnsupportedFileFormatException exc) {
        FileUploaderErrorResponse errResponse = new FileUploaderErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exc.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<FileUploaderErrorResponse>(errResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<FileUploaderErrorResponse> handleException(Exception otherException) {
        FileUploaderErrorResponse errorResponse = new FileUploaderErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                otherException.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<FileUploaderErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}
