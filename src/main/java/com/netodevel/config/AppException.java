package com.netodevel.config;

public class AppException extends RuntimeException {

    public String message;
    public int statusCode;

    public AppException(int statusCode, String message) {
        super(message);

        this.statusCode = statusCode;
        this.message = message;
    }
}
