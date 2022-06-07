package com.stamp.pdf.payload.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stamp.pdf.model.*;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.time.LocalDate;
import java.util.Base64;

@Getter
public class PdfStampRequest {
    private String fileBytes;
    private String purpose;
    private String name;
    private String position;
    private String company;
    private LocalDate date;
    private TextFontRequest textFont;
    private PageCoordinatesRequest coordinates;
    private RenderingMode renderingMode;


    @JsonCreator
    public PdfStampRequest(
            @JsonProperty("fileBytes") String fileBytes,
            @JsonProperty("purpose") String purpose,
            @JsonProperty("name") String name,
            @JsonProperty("position") String position,
            @JsonProperty("company") String company,
            @JsonProperty("date") LocalDate date,
            @JsonProperty("textFont") TextFontRequest textFont,
            @JsonProperty("coordinates") PageCoordinatesRequest coordinates,
            @JsonProperty("renderingMode") RenderingMode renderingMode) {
        this.fileBytes = fileBytes;
        this.purpose = purpose;
        this.name = name;
        this.position = position;
        this.company = company;
        this.date = date;
        this.textFont = textFont;
        this.coordinates = coordinates;
        this.renderingMode = renderingMode;
    }

    public PdfStamp asPdfStamp() {
        PdfStampInformation pdfStampInformation = this.asPdfStampInformation();
        PDDocument doc = byteArrayToFile();
        return new PdfStamp(doc, pdfStampInformation);
    }

    private PdfStampInformation asPdfStampInformation() {
        PageCoordinates pageCoordinates = coordinates.asPageCoordinates();
        TextFont tFont = this.textFont.asTextFont();
        return new PdfStampInformation(this.purpose, this.name, this.position, this.company, this.date, tFont, pageCoordinates, this.renderingMode);
    }

    private PDDocument byteArrayToFile() {
        PDDocument doc = new PDDocument();
        try {
            byte[] documentBytes = Base64.getDecoder().decode(this.getFileBytes());
            doc = PDDocument.load(documentBytes);
        } catch (Exception e) {
            System.out.println(e);
        }
        return doc;
    }
}
