package com.stamp.pdf.exception;

public class NoJwtProvidedException extends RuntimeException {

    public NoJwtProvidedException() {
        super("No JWT provided for authorization");
    }
}
