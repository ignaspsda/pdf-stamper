package com.stamp.pdf.model;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Specifies text font
 *
 */
@SuppressWarnings("serial")
public class TextFont extends Font
{
	private Color color;
	private FontMetrics fm;
	

	public TextFont(String name, int style, int size, Color color)
	{
		super(name, style, size);
		this.color = color;
		initFontMetrics();
	}

	public TextFont(String name, int style, Color color)
	{
		super(name, style, 48);
		this.color = color;
		initFontMetrics();
	}
	
	private void initFontMetrics()
	{
		Canvas c = new Canvas();
        fm = c.getFontMetrics(this);
		
	}
	
	public Color getColor()
	{
		return this.color;
	}
	
	public int getStringWidth(String str)
	{
		return fm.stringWidth(str);
	}
	
	public int getSpaceWidth()
	{
		return getStringWidth("a");
	}
	
	public int getHeight()
	{
		return fm.getHeight();
	}
}
