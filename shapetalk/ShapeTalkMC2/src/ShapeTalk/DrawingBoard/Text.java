package ShapeTalk.DrawingBoard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

/**
 * Text.java
 * 
 * Code of the Text tool.
 * 
 * @author Q
 */
public class Text implements IShape {

	public Text() {
	}

	/**
	 * @param s
	 * @param c
	 * @param x
	 * @param y
	 * @param f
	 */
	public Text(final String s, final Color c, final int x, final int y,
			final Font f) {
		this();
		string = s;
		color = c;
		X = x;
		Y = y;
		font = f;
	}

	/**
	 * Draw the text.
	 * 
	 * @see ShapeTalk.DrawingBoard.IShape#draw(java.awt.Graphics2D)
	 */
	public void draw(final Graphics2D g) {
		g.setColor(color);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(font);
		g.drawString(string, X, Y);
	}

	/**
	 * Get Text shape data.
	 * 
	 * @see ShapeTalk.DrawingBoard.IShape#getShapeData()
	 */
	public String getShapeData() {
		final String si = string;
		final StringBuffer buffer = new StringBuffer();
		buffer.append(color.getRGB());
		buffer.append(":");
		buffer.append(si);
		buffer.append(":");
		buffer.append(X);
		buffer.append(":");
		buffer.append(Y);
		buffer.append(":");
		buffer.append(font.getFontName());
		buffer.append(":");
		buffer.append(font.getStyle());
		buffer.append(":");
		buffer.append(font.getSize());
		return buffer.toString();
	}

	/**
	 * @see ShapeTalk.DrawingBoard.IShape#processCursorEvent(java.awt.event.MouseEvent,
	 *      int)
	 */
	public void processCursorEvent(final MouseEvent e, final int t) {
		X = e.getX();
		Y = e.getY();
	}

	/**
	 * Set Text shape data.
	 * 
	 * @see ShapeTalk.DrawingBoard.IShape#setShapeData(java.lang.String)
	 */
	public void setShapeData(final String data) throws Exception {
		final String splits[] = data.split(":");
		color = new Color(Integer.parseInt(splits[0]));
		string = splits[1];
		X = Integer.parseInt(splits[2]);
		Y = Integer.parseInt(splits[3]);
		font = new Font(splits[4], Integer.parseInt(splits[5]), Integer
				.parseInt(splits[6]));
	}

	/**
	 * Color of the Text shape.
	 */
	private Color color;

	/**
	 * Font of the Text shape.
	 */
	private Font font;

	/**
	 * String of the Text shape.
	 */
	private String string;

	/**
	 * Position of the Text shape.
	 */
	private int X, Y;

}
