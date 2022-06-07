package com.stamp.pdf.payload.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stamp.pdf.model.PdfStampInformation;
import com.stamp.pdf.model.PageCoordinates;
import com.stamp.pdf.model.RenderingMode;
import com.stamp.pdf.model.TextFont;

import java.time.LocalDate;

public class PdfStampInformationRequest {
    private final String purpose;
    private final String name;
    private final String position;
    private final String company;
    private final LocalDate date;
    private final TextFontRequest textFont;
    private final PageCoordinatesRequest coordinates;
    private final RenderingMode renderingMode;

    @JsonCreator
    public PdfStampInformationRequest(
            @JsonProperty("purpose") String purpose,
            @JsonProperty("name") String name,
            @JsonProperty("position") String position,
            @JsonProperty("company") String company,
            @JsonProperty("date") LocalDate date,
            @JsonProperty("textFont") TextFontRequest textFont,
            @JsonProperty("coordinates") PageCoordinatesRequest coordinates,
            @JsonProperty("renderingMode") RenderingMode renderingMode) {
        this.purpose = purpose;
        this.name = name;
        this.position = position;
        this.company = company;
        this.date = date;
        this.textFont = textFont;
        this.coordinates = coordinates;
        this.renderingMode = renderingMode;
    }

    public PdfStampInformation asPdfStampInformation() {
        PageCoordinates pageCoordinates = coordinates.asPageCoordinates();
        TextFont tFont = this.textFont.asTextFont();
        return new PdfStampInformation(this.purpose, this.name, this.position, this.company, this.date, tFont, pageCoordinates, this.renderingMode);
    }


}
