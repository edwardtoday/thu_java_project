package ShapeTalk.DrawingBoard;

import java.awt.Color;
import java.awt.Font;

/**
 * Tool Settings objects group tool-related configuration information.
 */
public class ToolSettings {
	public static final int ftNone = 0, ftOutline = 1, ftSolid = 2;

	/**
	 * airbrushIntensity: average surface area coverage in region defined by
	 * radius per "jot"
	 */
	public int airbrushIntensity = 30;

	/**
	 * airbrushRadius: coverage radius in pixels
	 */
	public int airbrushRadius = 10;

	/**
	 * commonBackgroundColor: current tool background color
	 */
	public Color commonBackgroundColor;

	/**
	 * commonFillType: current fill type
	 * <p>
	 * One of ftNone, ftOutline, ftSolid.
	 * </p>
	 */
	public int commonFillType = ToolSettings.ftNone;

	/**
	 * commonFont: current font
	 */
	public Font commonFont;

	/**
	 * commonForegroundColor: current tool foreground color
	 */
	public Color commonForegroundColor;

	/**
	 * commonLineStyle: current line type
	 */
	public int commonLineStyle = 1;

	/**
	 * roundedRectangleCornerDiameter: the diameter of curvature of corners in a
	 * rounded rectangle
	 */
	public int roundedRectangleCornerDiameter = 16;
}
