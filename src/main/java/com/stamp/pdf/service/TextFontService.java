package com.stamp.pdf.service;

import com.stamp.pdf.model.PdfStamp;
import com.stamp.pdf.model.PdfStampInformation;
import com.stamp.pdf.model.StampNameImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextFontService {

    @Value("#{T(Float).parseFloat('${text-padding-coefficient}')}")
    private float textPaddingCoefficient;

    @Autowired
    public TextFontService() {}

    public int getFontSize(PdfStampInformation pdfStampInformation, int stampHeight) {

        String purpose = pdfStampInformation.getPurpose();
        String position = pdfStampInformation.getPosition();
        String company = pdfStampInformation.getCompany();
        String name = pdfStampInformation.getName();
        StampNameImage nameImage = pdfStampInformation.getNameImage();
        String date = pdfStampInformation.getDateText();

        int linesCount = 0;

        if (purpose != null) {
            linesCount++;
        }
        if (position != null || company != null) {
            linesCount++;
        }
        if (date != null) {
            linesCount++;
        }

        if (name != null || nameImage != null) {
            linesCount++;
        }

        return (int) ((stampHeight / linesCount) * this.textPaddingCoefficient);
    }

    public int resizeFont(PdfStampInformation pdfStampInformation, int stampHeight, List<String> name) {
        String purpose = pdfStampInformation.getPurpose();
        String position = pdfStampInformation.getPosition();
        String company = pdfStampInformation.getCompany();
        String date = pdfStampInformation.getDateText();

        int linesCount = 0;

        if (purpose != null) {
            linesCount++;
        }
        if (position != null || company != null) {
            linesCount++;
        }
        if (date != null) {
            linesCount++;
        }

        if (name != null) {
            linesCount += name.size();
        }

        return (int) ((stampHeight / linesCount) * this.textPaddingCoefficient);
    }
}
