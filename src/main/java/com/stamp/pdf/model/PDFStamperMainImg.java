package com.stamp.pdf.model;

import com.stamp.pdf.model.PdfStamp;
import com.stamp.pdf.model.PdfStampInformation;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PDFStamperMainImg extends PdfStamp {
    public PDFStamperMainImg(PDDocument doc, PdfStampInformation stamperInfo) {
        super(doc, stamperInfo);
    }

    //override
    protected InputStream getStampImage() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage stampImage = this.getStamperInfo().getStampImage();
        ImageIO.write(stampImage, "PNG", baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }
}
