package com.stamp.pdf.exception;

public class JwtBearerException extends RuntimeException {
    public JwtBearerException() {
        super("JWT must start with Bearer!");
    }
}
