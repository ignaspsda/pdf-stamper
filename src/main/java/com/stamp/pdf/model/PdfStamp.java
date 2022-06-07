package com.stamp.pdf.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class PdfStamp {
    private PDDocument doc;
    private PdfStampInformation stamperInfo;
    private final float DefaultPositionValue = 10;

    public PdfStamp(PDDocument doc, PdfStampInformation stamperInfo) {
        this.doc = doc;
        this.stamperInfo = stamperInfo;
    }
}
