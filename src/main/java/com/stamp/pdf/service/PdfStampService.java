package com.stamp.pdf.service;

import com.stamp.pdf.model.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
public class PdfStampService {

    private final StampImageService stampImageService;
    private final PdfDocumentService pdfDocumentService;
    private final StampCoordinatesService stampCoordinatesService;

    @Autowired
    public PdfStampService(StampImageService stampImageService, PdfDocumentService pdfDocumentService, StampCoordinatesService stampCoordinatesService) {
        this.stampImageService = stampImageService;
        this.pdfDocumentService = pdfDocumentService;
        this.stampCoordinatesService = stampCoordinatesService;
    }

    public String dummyService() {
        return "PDF Stamp Service";
    }

    /**
     * Creates visual stamp based on PDFStampInformation without attached signature
     *
     * @throws IOException
     * @return
     */
    public String stamp(PdfStamp pdfStamp) throws IOException {
        PDDocument doc = pdfStamp.getDoc();
        PdfStampInformation pdfStampInformation = pdfStamp.getStamperInfo();
        PDDocumentInformation info = doc.getDocumentInformation();

        int pageIndex = pdfStampInformation.getPage();

        PDPage page = this.pdfDocumentService.getStamperPage(doc, pageIndex); // getPage counts from 0

        /**
            Why three booleans passed into constructor. Solves incorrect stamping position.
            https://stackoverflow.com/a/24992794/3704059
         */

        PDPageContentStream contentStream = new PDPageContentStream(doc, page, true, true, true);

        InputStream image = this.stampImageService.getStampImage(pdfStamp);

        PDImageXObject pdImageXObject = LosslessFactory.createFromImage(doc, ImageIO.read(image));

        float stampWidth = pdImageXObject.getWidth();
        float stampHeight = pdImageXObject.getHeight();

        float[] coordinates;

        PageCoordinates pageCoordinates = pdfStampInformation.getCoordinates();
        if (pageCoordinates == null) {
            coordinates = this.stampCoordinatesService.getCoordinatesForRegularStamp(pdfStamp, info, doc);
            info.setCustomMetadataValue(pdfStampInformation.getStampPosition().name(), getNewStampMetadata(coordinates));
        } else {
            coordinates = this.stampCoordinatesService.getStamperInformationCoordinates(pdfStampInformation, doc);
        }

        contentStream.drawImage(pdImageXObject, coordinates[0], coordinates[1], this.stampImageService.getZoomedValue(stampWidth), this.stampImageService.getZoomedValue(stampHeight));

        doc.setDocumentInformation(info);

        contentStream.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.save(baos);
        String base64String = Base64.getEncoder().encodeToString(baos.toByteArray());

        this.pdfDocumentService.close(doc);
        System.out.println("PdfStampService Finished!");

        return base64String;
    }

    private String getNewStampMetadata(float[] coordinates) {
        return coordinates[0] + ";" + coordinates[1] + ";" + coordinates[2] + ";" + coordinates[3];
    }
}
