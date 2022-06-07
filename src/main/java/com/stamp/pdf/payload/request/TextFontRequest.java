package com.stamp.pdf.payload.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stamp.pdf.model.TextFont;

import java.awt.*;
import java.lang.reflect.Field;

public class TextFontRequest {
    private String name;
    private int style;
    private String color;

    @JsonCreator
    public TextFontRequest(@JsonProperty("name") String name, @JsonProperty("style") int style, @JsonProperty("color") String color) {
        this.name = name;
        this.style = style;
        this.color = color;
    }

    public TextFont asTextFont() {
        if (this.name == null || this.color == null) {
            Color textColor;
            try {
                Field field = Color.class.getField("black");
                textColor = (Color)field.get(null);
            } catch (Exception e) {
                textColor = null; // Not defined
            }
            return new TextFont("Verdana", 0, textColor);
        }
        Color textColor;
        try {
            Field field = Color.class.getField(color);
            textColor = (Color)field.get(null);
        } catch (Exception e) {
            textColor = null; // Not defined
        }
        return new TextFont(this.name, style, textColor);
    }
}
