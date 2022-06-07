package com.stamp.pdf.payload.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stamp.pdf.model.PageCoordinates;
import com.stamp.pdf.model.Proportion;

public class PageCoordinatesRequest {
    private final int x;
    private final int y;
    private final Proportion proportion;

    @JsonCreator
    public PageCoordinatesRequest(@JsonProperty("x") int x, @JsonProperty("y") int y, @JsonProperty("proportion") Proportion proportion) {
        this.x = x;
        this.y = y;
        this.proportion = proportion;
    }

    public PageCoordinates asPageCoordinates() {
        return new PageCoordinates(this.x, this.y, this.proportion);
    }
}
