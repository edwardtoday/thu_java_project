package ShapeTalk.DrawingBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * RoundRect.java
 * 
 * Code of the RoundRect tool.
 * 
 * @author Q
 * 
 */
public class RoundRect extends RectBoundedShape {

	public RoundRect() {
		super();
	}

	/**
	 * @param c
	 * @param s
	 * @param x
	 * @param y
	 * @param f
	 */
	public RoundRect(final Color c, final BasicStroke s, final int x,
			final int y, final int f) {
		super(c, s, x, y, f);
	}

	/**
	 * Draw the round rectangle.
	 * 
	 * @see ShapeTalk.DrawingBoard.IShape#draw(java.awt.Graphics2D)
	 */
	public void draw(final Graphics2D g) {
		g.setColor(color);
		g.setStroke(stroke);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int x, y, w, h;
		if (startX > endX) {
			x = endX;
			w = startX - endX;
		} else {
			x = startX;
			w = endX - startX;
		}
		if (startY > endY) {
			y = endY;
			h = startY - endY;
		} else {
			y = startY;
			h = endY - startY;
		}
		if (filled == 0) {
			g.drawRoundRect(x, y, w, h, (w / 5), (h / 5));
		} else {
			g.fillRoundRect(x, y, w, h, (w / 5), (h / 5));
		}
	}

}
