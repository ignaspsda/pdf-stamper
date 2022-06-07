package com.stamp.pdf.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Creates stamp name image. The name image can be created from file or using text and specified font.
 *
 */
public class StampNameImage extends BufferedImage
{
	private static final float HEIGHT_COEFFICIENT = 1.25f;
	private TextFont font;
	
	private StampNameImage(String name, TextFont font) throws IOException
	{
		super(getTextImageSize(name, font)[0], getTextImageSize(name, font)[1], BufferedImage.TYPE_INT_ARGB);
		this.font = font;
		this.createImage(name, font);
	}
	
	private StampNameImage(int width, int height, int imageType)
	{
		super(width, height, imageType);

	}
		
	public StampNameImage(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied,
			Hashtable<?, ?> properties)
	{
		super(cm, raster, isRasterPremultiplied, properties);
	}

	/**
	 * Creates image with text using spedified font
	 * @param text - the text in the image
	 * @param font - the font of the text
	 * @return stamp name image
	 * @throws IOException
	 */
	public static StampNameImage createInstance(String text, TextFont font) throws IOException
	{
		StampNameImage image = new StampNameImage(text, font);
		return image;	
	}
		
	/**
	 * Creates stamp name image from file
	 * @param file - the file of image
	 * @return stamp name image
	 * @throws IOException
	 */
	public static StampNameImage createInstance(File file) throws IOException
	{
		BufferedImage image = ImageIO.read(file);
		
		return deepCopy(image);
	}
	
	
	/**
	 * Creates stamp name image from byte array
	 * @param file - the file of image
	 * @return stamp name image
	 * @throws IOException
	 */
	public static StampNameImage createInstance(byte[] imageBytes) throws IOException
	{
		BufferedImage image = ImageIO.read(new ByteArrayInputStream( imageBytes) );
		
		return deepCopy(image);
	}
	
	
	private static StampNameImage deepCopy(BufferedImage bi) 
	{
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new StampNameImage(cm, raster, isAlphaPremultiplied, null);
		
	}
	
	private static int getSpaceWidth( TextFont font)
	{
		if(font == null)
		{
			return 0;
		}
		
		return font.getSpaceWidth();
	}
	
	public int getSpaceWidth()
	{
		return getSpaceWidth(font);
	}
	
	private static int[] getTextImageSize(String text, TextFont font)
	{
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
       
        g2d.setFont(font);
        g2d.setColor(font.getColor());
        FontMetrics fm = g2d.getFontMetrics();
     	
        int width = fm.stringWidth(text);
        width += 2*getSpaceWidth(font);
        int height = (int) (fm.getHeight()*HEIGHT_COEFFICIENT);
        
        g2d.dispose();
        return new int[] {width, height};
	}
	
	private void createImage(String text, TextFont font) throws IOException
	{
        Graphics2D g2d = this.createGraphics();
        ImageUtils.setDefaultGraphicsConfiguration(g2d);
        	 
        g2d.setFont(font);
        g2d.setColor(font.getColor());
        FontMetrics fm = g2d.getFontMetrics();
        
        g2d.drawString(text, font.getSpaceWidth(), fm.getHeight());
        g2d.dispose();
        
	}
}
