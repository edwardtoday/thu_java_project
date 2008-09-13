package ShapeTalk.DrawingBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Diamond.java
 * 
 * Code of the Diamond tool.
 * 
 * @author Q
 * 
 */
public class Diamond extends RectBoundedShape {

	public Diamond() {
		super();
		xS = new int[4];
		yS = new int[4];
	}

	/**
	 * @param c
	 * @param s
	 * @param x
	 * @param y
	 * @param f
	 */
	public Diamond(final Color c, final BasicStroke s, final int x,
			final int y, final int f) {
		super(c, s, x, y, f);
		xS = new int[4];
		yS = new int[4];
	}

	/**
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
		xS[0] = x + w / 2;
		yS[0] = y;
		xS[1] = x + w;
		yS[1] = y + h / 2;
		xS[2] = x + w / 2;
		yS[2] = y + h;
		xS[3] = x;
		yS[3] = y + h / 2;
		if (filled == 0) {
			g.drawPolygon(xS, yS, 4);
		} else {
			g.fillPolygon(xS, yS, 4);
		}
	}

	/**
	 * X positions of the diamond.
	 */
	private final int[] xS;
	/**
	 * Y positions of the diamond.
	 */
	private final int[] yS;

}
