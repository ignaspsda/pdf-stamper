package com.stamp.pdf.model;

/**
 * sets Page width, height
 *
 */
public class PageSize {
    private final float width;
    private final float height;

    public PageSize(float width, float height) {
        super();
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
