package com.stamp.pdf.model;

/**
 * Specifies resolution of page, which would be used as propotions
 *
 */
public enum Proportion {
    NONE(0, 0),
    PERCENTAGE(100, 100),
    A4_PAGE_72_DPI(595, 842);

    private float width;
    private float height;

    Proportion(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }
}
