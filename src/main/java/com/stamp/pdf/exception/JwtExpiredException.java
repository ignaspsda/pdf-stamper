package com.stamp.pdf.exception;

public class JwtExpiredException extends RuntimeException {

    public JwtExpiredException() {
        super("The expiration date for JWT have passed!");
    }
}
