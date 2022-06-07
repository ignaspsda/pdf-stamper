package com.stamp.pdf.model;

/**
 * Adjusts coordinates by specified proportion using following formula:
 * newCoordiantes = (x, y)*((pageWidth, pageHeight)/(proportionWidth, proportionHeight))
 * if Proportion.NONE is selected the coordinates would not be changed.
 *
 */
public class PageCoordinates
{
	private final float x;
	private final float y;
	private final Proportion proportion;

	public PageCoordinates(float x, float y, Proportion proportion)
	{
		this.x = x;
		this.y = y;
		this.proportion = proportion;
	}

	/**
	 * Adjust X coordinate to pageSize by specified proportion
	 * @param pageSize - pageSize to which the X coordinate would be adjusted
	 * @return adjusted X coordinate
	 */
	public float getX(PageSize pageSize)
	{
		float proportionWidth = pageSize.getWidth();
		if(proportion != Proportion.NONE) {
			proportionWidth = proportion.getWidth();
		}

		return x*(pageSize.getWidth()/proportionWidth);
	}

	/**
	 * Adjust Y coordinate to pageSize by specified proportion
	 * @param pageSize - pageSize to which the Y coordinate would be adjusted
	 * @return Adjusted Y coordinate
	 */
	public float getY(PageSize pageSize)
	{
		float proportionHeight = pageSize.getHeight();
		if(proportion != Proportion.NONE) {
			proportionHeight = proportion.getHeight();
		}

		return y*(pageSize.getHeight()/proportionHeight);
	}
}
