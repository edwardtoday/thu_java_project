package ShapeTalk.DrawingBoard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

public class Diamond extends RectBoundedShape {

	public Diamond() {
		super();
		xS = new int[4];
		yS = new int[4];
	}

	public Diamond(Color c, Stroke s, int x, int y) {
		super(c, s, x, y);
		xS = new int[4];
		yS = new int[4];
	}

	public void draw(Graphics2D g) {
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
		g.drawPolygon(xS, yS, 4);
	}

	private final int[] xS;

	private final int[] yS;

}
