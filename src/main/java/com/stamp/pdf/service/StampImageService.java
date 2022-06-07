package com.stamp.pdf.service;

import com.stamp.pdf.model.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

@Service
public class StampImageService {
    private static Graphics2D g2d;
    private static BufferedImage img;
    private final TextFontService textFontService;
    private final PdfDocumentService pdfDocumentService;

    @Autowired
    public StampImageService(TextFontService textFontService, PdfDocumentService pdfDocumentService) {
        this.textFontService = textFontService;
        this.pdfDocumentService = pdfDocumentService;
    }

    protected InputStream getStampImage(PdfStamp pdfStamp) {
        PdfStampInformation pdfStampInformation = pdfStamp.getStamperInfo();
        PDDocument doc = pdfStamp.getDoc();

        RenderingMode mode = pdfStampInformation.getRenderingMode();

        int stampHeight = this.getStampHeight(pdfStampInformation, doc);
        int fontSize = this.textFontService.getFontSize(pdfStampInformation, stampHeight);
        int[] imageSize = getStampImageSize(pdfStampInformation, fontSize, doc);

         img = pdfStampInformation.getStampImage();
         this.createImage(imageSize, mode);

        ImageUtils.setDefaultGraphicsConfiguration(g2d);

        TextFont detailsFont = pdfStampInformation.getTextFont(fontSize);
        FontMetrics fm = this.setStampFontMetrics(detailsFont);
        int space = getSpace(pdfStampInformation, detailsFont);

        this.setStampInformation(pdfStampInformation, space, fm, stampHeight);

        if (pdfStampInformation.getStampImage() == null) {
            this.drawOutline(imageSize);
        }

        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "PNG", baos);
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }

    private void setStampInformation(PdfStampInformation pdfStampInformation, int space, FontMetrics fm, int stampHeight) {
        int y = 0;

        String purpose = pdfStampInformation.getPurpose();
        if (purpose != null) {
            // the purpose is drawn after stamp name image is drawn to prevent
            // letters overlapping of purpose text and stamp name image
            y += fm.getHeight();
        }

        String name = pdfStampInformation.getName();
        if (name != null) {
            boolean nameWiderThanImg = (img.getWidth() - 10) < g2d.getFontMetrics().stringWidth(name);
            if (nameWiderThanImg) {
                List<String> nameSplit = splitName(name);
                fm = this.setNewFontMetrics(pdfStampInformation, stampHeight, nameSplit);
                g2d.drawString(nameSplit.get(0), space, y += fm.getHeight());
                g2d.drawString(nameSplit.get(1), space, y += fm.getHeight());
            } else {
                g2d.drawString(name, space, y += fm.getHeight());
            }
        }

        if (purpose != null) {
            // secondly, purpose text is drawn at the first line
            //the string is drawn upwards in respect of (x, y) coordinates
            g2d.drawString(purpose, space, fm.getHeight());
        }

        String position = pdfStampInformation.getPosition();
        String company = pdfStampInformation.getCompany();
        if (position != null) {
            // thirdly, the position and company is drawn at the third line
            if (company == null) {
                g2d.drawString(position, space, y += fm.getHeight());
            } else {
                g2d.drawString(position + ", " + company, space, y += fm.getHeight());
            }
        } else {
            if (company != null) {
                g2d.drawString(company, space, y += fm.getHeight());
            }
        }

        LocalDate signDate = pdfStampInformation.getDate();
        if (signDate != null) {
            // The date is drawn at the last line
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = signDate.format(dateFormat);
            g2d.drawString(formattedDate, space, y + fm.getHeight());
        }
    }

    private FontMetrics setStampFontMetrics(TextFont detailsFont) {
        g2d.setFont(detailsFont);
        g2d.setColor(detailsFont.getColor());
        return g2d.getFontMetrics();
    }

    private FontMetrics setNewFontMetrics(PdfStampInformation pdfStampInformation, int stampHeight, List<String> name) {
        int fontSize = this.textFontService.resizeFont(pdfStampInformation, stampHeight, name);
        TextFont detailsFont = pdfStampInformation.getTextFont(fontSize);
        return this.setStampFontMetrics(detailsFont);
    }

    private void drawOutline(int[] imageSize) {
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0,0, imageSize[0], imageSize[1]);
    }

    private static List<String> splitName(String name) {
        String[] nameSplit = name.split(" ");
        String lastName = nameSplit[nameSplit.length - 1];
        if (nameSplit.length > 2) {
            StringBuilder firstName = new StringBuilder();
            for (int i = 0; i < nameSplit.length - 1; i++) {
                firstName.append(nameSplit[i]).append(' ');
            }
            return Arrays.asList(firstName.toString(), lastName);
        }

        String firstName = nameSplit[0];

        return Arrays.asList(firstName, lastName);
    }

    private void createImage(int[] imageSize, RenderingMode mode) {
        if (img != null) {
            Image resizedImage = img.getScaledInstance(-1, imageSize[1], Image.SCALE_SMOOTH);
            int scaledWidth = resizedImage.getWidth(null);
            img = new BufferedImage(scaledWidth, imageSize[1], img.getType());
            g2d = img.createGraphics();
            g2d.drawImage(resizedImage, 0, 0, null);
        } else {
            int imageType = this.getImageType(mode);
            img = new BufferedImage(imageSize[0], imageSize[1], imageType);
            g2d = img.createGraphics();
/*            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.ORANGE);
            g2d.drawRect(0,0, imageSize[0], imageSize[1]);*/
            if (mode == RenderingMode.NOT_TRANSPARENT) {
                g2d.setColor(Color.RED);
                g2d.fillRect(0, 0, imageSize[0], imageSize[1]);
            }
        }
    }

    private int[] getStampImageSize(PdfStampInformation pdfStampInformation, int fontSize, PDDocument doc) {
        TextFont detailsFont = pdfStampInformation.getTextFont(fontSize);
        String purpose = pdfStampInformation.getPurpose();
        String company = pdfStampInformation.getCompany();
        String position = pdfStampInformation.getPosition();
        String date = pdfStampInformation.getDateText();
        String signerName = pdfStampInformation.getName();

        int width = 0;
        int height = 0;

        ArrayList<Integer> lineSizes = new ArrayList<>();

        if (position != null) {
            if (company == null) {
                lineSizes.add(detailsFont.getStringWidth(position));
            } else {
                lineSizes.add(detailsFont.getStringWidth(position + ", " + company));

            }
            height += detailsFont.getHeight();
        } else {
            if (company != null) {
                lineSizes.add(detailsFont.getStringWidth(company));
                height += detailsFont.getHeight();
            }
        }

        if (purpose != null) {
            lineSizes.add(detailsFont.getStringWidth(purpose));
            height += detailsFont.getHeight();
        }

        if (date != null) {
            lineSizes.add(detailsFont.getStringWidth(date));
            height += detailsFont.getHeight();
        }

        if (signerName != null) {
            lineSizes.add(detailsFont.getStringWidth(signerName));
            height += detailsFont.getHeight();
        } else if (pdfStampInformation.getNameImage() != null) {
            int[] nameResolution = this.getNameImageSize(pdfStampInformation, doc);
            lineSizes.add(nameResolution[0]);
            height += pdfStampInformation.getNAME_HEIGHT_COEFFICIENT() * nameResolution[1];
        }
        for (Integer size : lineSizes) {
            if (size > width) {
                width = size;
            }
        }

        int space = getSpace(pdfStampInformation, detailsFont);
        width += space * 2; // add extra space per two letters
        height += space; // add extra space

        return new int[]{width, height};
    }

    public int getStampHeight(PdfStampInformation pdfStampInformation, PDDocument doc) {
        int pageIndex = pdfStampInformation.getPage();
        float percentageKoef = pdfStampInformation.getStampSizePercentage() / 100;
        int maxStampHeight = (int) (this.pdfDocumentService.getPageHeight(doc, pageIndex) * percentageKoef);
        return maxStampHeight * ImageUtils.GetQualityScaleFactory();
    }

    private int getSpace(PdfStampInformation pdfStampInformation, TextFont font) {
        String signerName = pdfStampInformation.getName();
        StampNameImage nameImage = pdfStampInformation.getNameImage();

        int space = font.getSpaceWidth();
        if (signerName == null &&
                nameImage != null &&
                nameImage.getSpaceWidth() > space) {
            space = nameImage.getSpaceWidth();
        }
        return space;
    }

    private int[] getNameImageSize(PdfStampInformation pdfStampInformation, PDDocument doc) {
        float currentWidth = pdfStampInformation.getNameImage().getWidth(null);
        float currentHeight = pdfStampInformation.getNameImage().getHeight(null);
        int imageHeight = this.getNameImageHeight(pdfStampInformation, doc);

        return new int[] {getNameImageWidth(imageHeight, currentWidth, currentHeight), imageHeight};
    }

    private int getNameImageWidth(int maxStampHeight, float currentWidth, float currentHeight) {
        return (int) ((currentWidth / currentHeight) * maxStampHeight);
    }

    private int getNameImageHeight(PdfStampInformation pdfStampInformation, PDDocument doc) {
        int pageIndex = pdfStampInformation.getPage();
        float percentageKoef = pdfStampInformation.getStampNameImageSize() / 100;
        int maxImageHeight = (int) (this.pdfDocumentService.getPageHeight(doc, pageIndex) * percentageKoef);
        return maxImageHeight * ImageUtils.GetQualityScaleFactory();
    }

    public float getZoomedValue(float value) {
        return value / ImageUtils.GetQualityScaleFactory();
    }

    private int getImageType(RenderingMode renderingMode) {
        switch (renderingMode) {
            case TRANSPARENT:
                return BufferedImage.TYPE_INT_ARGB;
            case NOT_TRANSPARENT:
                return BufferedImage.TYPE_INT_RGB;
            default:
                throw new IllegalArgumentException("Rendering mode not supported");
        }
    }
}
