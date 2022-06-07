package com.stamp.pdf.exception;

public class ImageReadException extends RuntimeException {
    public ImageReadException() { super("Failed to read image");}
}
