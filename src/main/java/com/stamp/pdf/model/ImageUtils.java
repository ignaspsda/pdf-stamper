package com.stamp.pdf.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {
    private static int SCALE_FACTORY = 3;

    public static int GetQualityScaleFactory() {
        return SCALE_FACTORY;
    }

    public static BufferedImage convertToTransparent(BufferedImage image, int color) {
        BufferedImage transparent = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                int argb = image.getRGB(x, y);
                if ((argb & 0x00FFFFFF) == color) {
                    transparent.setRGB(x, y, 0);
                } else {
                    transparent.setRGB(x, y, argb);
                }
            }
        }
        return transparent;
    }

    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     *
     * @param srcImg - source image to scale
     * @param w      - desired width
     * @param h      - desired height
     * @return - the new resized image
     */
    public static BufferedImage getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public static void setDefaultGraphicsConfiguration(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }
}
