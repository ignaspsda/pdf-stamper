package com.stamp.pdf.service;

import com.stamp.pdf.model.PdfStampInformation;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PdfDocumentService {

    public PdfDocumentService() {
    }

    public float getPageHeight(PDDocument doc, int pageIndex) {
        PDPage page = getStamperPage(doc, pageIndex);
        int rotation = page.getRotation();
        PDRectangle mediaBox = page.getMediaBox();
        if (rotation == 90 || rotation == 270) {
            return mediaBox.getWidth();
        }

        return mediaBox.getHeight();
    }

    public float getPageWidth(int pageIndex, PDDocument doc) {
        PDPage page = getStamperPage(doc, pageIndex);
        int rotation = page.getRotation();
        PDRectangle mediaBox = page.getMediaBox();

        if (rotation == 90 || rotation == 270) {
            return mediaBox.getHeight();
        }

        return mediaBox.getWidth();
    }

    public PDPage getStamperPage(PDDocument doc, int pageIndex) {
        return doc.getPage(getStamperPageIndex(pageIndex));
    }

    private int getStamperPageIndex(int page) {
        if (page == 0) {
            return page;
        } else {
            return page - 1;
        }
    }

    public int getStamperPageRotation(int page, PDDocument doc) {
        return getStamperPage(doc, page).getRotation();
    }

    /**
     * Close PDF document
     *
     * @throws IOException
     */
    public void close(PDDocument doc) throws IOException {
        try {
            if (doc != null) {
                doc.close();
            }
        } catch (IOException e) {
            System.out.println("Error occurred during the close of a COSDocument : " + e.getMessage());
        }
    }
}
