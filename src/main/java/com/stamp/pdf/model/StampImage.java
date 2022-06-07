package com.stamp.pdf.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Hashtable;

public class StampImage extends BufferedImage
{
    public StampImage(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied,
                          Hashtable<?, ?> properties)
    {
        super(cm, raster, isRasterPremultiplied, properties);
    }

    public static StampImage createInstance(byte[] imageBytes) throws IOException
    {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream( imageBytes) );
        return deepCopy(image);
    }

    private static StampImage deepCopy(BufferedImage bi)
    {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new StampImage(cm, raster, isAlphaPremultiplied, null);
    }
}
