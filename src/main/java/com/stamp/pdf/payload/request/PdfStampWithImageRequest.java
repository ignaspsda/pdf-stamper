package com.stamp.pdf.payload.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stamp.pdf.enums.StampPosition;
import com.stamp.pdf.model.*;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;

@Getter
public class PdfStampWithImageRequest {
    private String fileBytes;
    private String imageBytes;
    private String purpose;
    private String name;
    private String position;
    private String company;
    private LocalDate date;
    private TextFontRequest textFont;
    private RenderingMode renderingMode;
    private StampPosition stampPosition;

    @JsonCreator
    public PdfStampWithImageRequest(
            @JsonProperty("fileBytes") String fileBytes,
            @JsonProperty("imageBytes") String imageBytes,
            @JsonProperty("purpose") String purpose,
            @JsonProperty("name") String name,
            @JsonProperty("position") String position,
            @JsonProperty("company") String company,
            @JsonProperty("date") LocalDate date,
            @JsonProperty("textFont") TextFontRequest textFont,
            @JsonProperty("renderingMode") RenderingMode renderingMode,
            @JsonProperty("stampPosition") StampPosition stampPosition) {
        this.fileBytes = fileBytes;
        this.imageBytes = imageBytes;
        this.purpose = purpose;
        this.name = name;
        this.position = position;
        this.company = company;
        this.date = date;
        this.textFont = textFont;
        this.renderingMode = renderingMode;
        this.stampPosition = stampPosition;
    }

    public PDFStamperMainImg asPdfStamp() {
        PdfStampInformation pdfStampInformation = this.asPdfStampInformation();
        PDDocument doc = byteArrayToPDDocument();
        return new PDFStamperMainImg(doc, pdfStampInformation);
    }

    private PdfStampInformation asPdfStampInformation() {
        TextFont tFont = this.textFont.asTextFont();
        StampImage stampImage = this.byteArrayToStampImage();
        return new PdfStampInformation(this.purpose, this.name, this.position, this.company, this.date, tFont, this.renderingMode, stampImage, this.stampPosition);
    }

    private StampImage byteArrayToStampImage() {
        StampImage stampImage = null;
        try {
            stampImage = StampImage.createInstance(Base64.getDecoder().decode(this.getImageBytes()));
        } catch (IOException e) {
            System.out.println(e);
        }
        return stampImage;
    }

    private PDDocument byteArrayToPDDocument() {
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
