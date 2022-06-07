package com.stamp.pdf.model;

import com.stamp.pdf.enums.StampPosition;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
public class PdfStampInformation {
    private final float NAME_IMAGE_SIZE_COEFFICIENT = 0.8f;
    private final float NAME_HEIGHT_COEFFICIENT = 0.75f;

    private String purpose;
    private String position;
    private String company;
    private LocalDate date;
    private Date signDate;
    private TextFont textFont;
    private StampNameImage nameImage;
    private PageCoordinates coordinates;
    private RenderingMode renderingMode;
    private int page;
    private String name;
    private StampPosition stampPosition;

    private float stampSizePercentage = 3;
    private StampImage stampImage;

    /**
     * Creates PDFStampInformation
     *
     * @param purpose       - purpose( can be null)
     * @param nameImage     image of name (cannot be null)
     * @param position      - position of creator, for instance, the secretary, CEO.( can be null)
     * @param company       - company of creator( can be null)
     * @param date          - date, which would be displayed in format yyyy-MM-dd HH:mm:ss ( can be null)
     * @param textFont      - the font of all text in stamp except the name, because it is as separate image
     * @param coordinates   - coordinates where to put stamp, can be null
     * @param renderingMode - the rendering mode of stamp: TRANSPARENT or NOT TRANSPARENT
     * @param page          - page number on which stamp should be placed. Count stats from 1
     */
    public PdfStampInformation(String purpose,
                               StampNameImage nameImage,
                               String position,
                               String company,
                               LocalDate date,
                               TextFont textFont,
                               PageCoordinates coordinates,
                               RenderingMode renderingMode,
                               int page) {

        if (page <= 0) {
            throw new IllegalArgumentException("page counts from 1");
        }
        this.purpose = purpose;
        this.position = position;
        this.company = company;

        if (textFont != null)
            this.textFont = textFont;

        this.nameImage = nameImage;
        this.coordinates = coordinates;
        this.renderingMode = renderingMode;
        this.page = page;
        if (date != null)
            this.date = date;
    }

    public PdfStampInformation(String purpose,
                               StampNameImage nameImage,
                               String position,
                               String company,
                               LocalDate date,
                               TextFont textFont,
                               PageCoordinates coordinates,
                               RenderingMode renderingMode,
                               int page,
                               StampPosition stampPosition) {
        this(purpose, nameImage, position, company, date, textFont, coordinates, renderingMode, page);
        this.stampPosition = stampPosition;
    }

    public PdfStampInformation(String purpose,
                               String name,
                               String position,
                               String company,
                               LocalDate date,
                               TextFont textFont,
                               PageCoordinates coordinates,
                               RenderingMode renderingMode,
                               int page) {
        this(purpose, (StampNameImage) null, position, company, date, textFont, coordinates, renderingMode, page);
        this.name = name;
    }

    public PdfStampInformation(String purpose,
                               String name,
                               String position,
                               String company,
                               LocalDate date,
                               TextFont textFont,
                               PageCoordinates coordinates,
                               RenderingMode renderingMode,
                               int page,
                               StampPosition stampPosition) {
        this(purpose, (StampNameImage) null, position, company, date, textFont, coordinates, renderingMode, page);
        this.name = name;
        this.stampPosition = stampPosition;
    }

    /**
     * Creates PDFStampInformation on first page
     *
     * @param purpose       - purpose( can be null)
     * @param nameImage     image of name (cannot be null)
     * @param position      - position of creator, for instance, the secretary, CEO.( can be null)
     * @param company       - company of creator( can be null)
     * @param date          - date, which would be displayed in format yyyy-MM-dd HH:mm:ss ( can be null)
     * @param textFont      - the font of all text in stamp except the name, because it is as separate image
     * @param coordinates   - coordinates where to put stamp, can be null
     * @param renderingMode - the rendering mode of stamp: TRANSPARENT or NOT TRANSPARENT
     */
    public PdfStampInformation(String purpose,
                               StampNameImage nameImage,
                               String position,
                               String company,
                               LocalDate date,
                               TextFont textFont,
                               PageCoordinates coordinates,
                               RenderingMode renderingMode) {
        this(purpose, nameImage, position, company, date, textFont, coordinates, renderingMode, 1);
    }

    public PdfStampInformation(String purpose,
                               String name,
                               String position,
                               String company,
                               LocalDate date,
                               TextFont textFont,
                               PageCoordinates coordinates,
                               RenderingMode renderingMode) {
        this(purpose, name, position, company, date, textFont, coordinates, renderingMode, 1);
    }

    public PdfStampInformation(String purpose,
                               String name,
                               String position,
                               String company,
                               LocalDate date,
                               TextFont textFont,
                               RenderingMode renderingMode,
                               StampImage stampImage,
                               StampPosition stampPosition) {
        this.purpose = purpose;
        this.name = name;
        this.position = position;
        this.company = company;
        if (date != null) {
            this.date = date;
        }
        this.textFont = textFont;
        this.renderingMode = renderingMode;
        this.stampImage = stampImage;
        this.stampPosition = stampPosition;
    }

    public PdfStampInformation(StampImage stampImage, int page, StampPosition stampPosition, String signDate) throws ParseException {
        this.stampPosition = stampPosition;
        this.page = page;
        this.stampImage = stampImage;

        if (signDate != null) {
            this.signDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(signDate);
        }
    }

    public RenderingMode getRenderingMode() {
        if (renderingMode == null) {
            return RenderingMode.NOT_TRANSPARENT;
        }
        return renderingMode;
    }

    public float getStampNameImageSize() {
        return stampSizePercentage * this.NAME_IMAGE_SIZE_COEFFICIENT;
    }

    public TextFont getTextFont(int fontSize) {
        return new TextFont(textFont.getName(), textFont.getStyle(), fontSize, textFont.getColor());
    }

    public String getPurpose() {
        return getNotEmpty(purpose);
    }

    public String getPosition() {
        return getNotEmpty(position);
    }

    public String getCompany() {
        return getNotEmpty(company);
    }

    private String getNotEmpty(String str) {
        if (isNotEmpty(str))
            return str;
        else
            return null;
    }

    public String getDateText() {
        if (date == null)
            return null;

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return date.format(dateFormat);

    }

    private boolean isNotEmpty(String str) {
        return str != null && !str.equals("");
    }
}
