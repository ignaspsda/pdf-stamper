package com.stamp.pdf.service;

import com.stamp.pdf.enums.StampPosition;
import com.stamp.pdf.model.PageCoordinates;
import com.stamp.pdf.model.PageSize;
import com.stamp.pdf.model.PdfStamp;
import com.stamp.pdf.model.PdfStampInformation;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

@Service
public class StampCoordinatesService {

    @Value("${margin-between-lines}")
    private int marginBetweenLines;

    @Value("${margin-between-stamps}")
    private int marginBetweenStamps;

    private final PdfDocumentService pdfDocumentService;
    private final StampImageService stampImageService;

    @Autowired
    public StampCoordinatesService(PdfDocumentService pdfDocumentService, StampImageService stampImageService) {
        this.pdfDocumentService = pdfDocumentService;
        this.stampImageService = stampImageService;
    }

    public float[] getCoordinatesForRegularStamp(PdfStamp pdfStamp, PDDocumentInformation info, PDDocument doc) {
        float x;
        float y;
        float highestElemInLine;

        InputStream inputStream = this.stampImageService.getStampImage(pdfStamp);

        float imgHeight = 0;
        float imgWidth = 0;

        try {
            PDImageXObject pdImageXObject = LosslessFactory.createFromImage(doc, ImageIO.read(inputStream));
            imgHeight = pdImageXObject.getHeight();
            imgWidth = pdImageXObject.getWidth();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        float resizedStampImageHeight = this.stampImageService.getZoomedValue(imgHeight);
        float resizedStampImageWidth = this.stampImageService.getZoomedValue(imgWidth);

        PdfStampInformation pdfStampInformation = pdfStamp.getStamperInfo();

        String stampPositionOnPage = pdfStampInformation.getStampPosition().name();

        String stampMetadata = info.getCustomMetadataValue(stampPositionOnPage);

        if (stampMetadata != null) {
            String[] splitApproveField = stampMetadata.split(";");
            x = Float.parseFloat(splitApproveField[0]) + this.marginBetweenStamps;
            x += Float.parseFloat(splitApproveField[3]);
            y = Float.parseFloat(splitApproveField[1]);
            highestElemInLine = Float.parseFloat(splitApproveField[2]);

            int pageIndex = pdfStampInformation.getPage();
            float pageWidth = this.pdfDocumentService.getPageWidth(pageIndex, doc);

            if (pageWidth - (x + resizedStampImageWidth) < 0) {
                x = pdfStamp.getDefaultPositionValue();
                if (pdfStampInformation.getStampPosition() == StampPosition.TOP_LEFT) {
                    y = y - this.marginBetweenLines - highestElemInLine;
                } else if (pdfStampInformation.getStampPosition() == StampPosition.BOTTOM_LEFT) {
                    y = y + this.marginBetweenLines + highestElemInLine;
                }
            }

            if (resizedStampImageHeight > highestElemInLine) {
                highestElemInLine = resizedStampImageHeight;
            }
        } else { // if it is first stamp and there is no metadata
            int defaultPositionValue = (int) pdfStamp.getDefaultPositionValue();
            x = defaultPositionValue;
            y = 0;
            highestElemInLine = resizedStampImageHeight;
            if (pdfStampInformation.getStampPosition() == StampPosition.TOP_LEFT) {
                int pageIndex = pdfStamp.getStamperInfo().getPage();
                float pageHeight = this.pdfDocumentService.getPageHeight(pdfStamp.getDoc(), pageIndex);
                y = pageHeight - resizedStampImageHeight - defaultPositionValue;
            } else if (pdfStampInformation.getStampPosition() == StampPosition.BOTTOM_LEFT) {
                y = defaultPositionValue;
            }
        }

        return new float[]
                {x, y, highestElemInLine, resizedStampImageWidth};
    }

    public float[] getStamperInformationCoordinates(PdfStampInformation pdfStampInformation, PDDocument doc) {
        float x;
        float y;

        PageCoordinates coordinates = pdfStampInformation.getCoordinates();
        PDPage page = doc.getPage(pdfStampInformation.getPage() - 1); // getPage counts from 0
        PDRectangle rect = page.getMediaBox();

        float pageWidth = rect.getWidth();
        float pageHeight = rect.getHeight();

        PageSize pageSize = new PageSize(pageWidth, pageHeight);

        x = coordinates.getX(pageSize);
        y = coordinates.getY(pageSize);

        return new float[]{x, y};

    }

    private float getXPadding(int pageIndex, PDDocument doc) {
        int rotation = this.pdfDocumentService.getStamperPageRotation(pageIndex, doc);

        return (rotation == 90 || rotation == 270) ? 5 : 10;
    }

    private float getYPadding(int pageIndex, PDDocument doc) {
        int rotation = this.pdfDocumentService.getStamperPageRotation(pageIndex, doc);

        return (rotation == 90 || rotation == 270) ? 10 : 5;
    }
}
