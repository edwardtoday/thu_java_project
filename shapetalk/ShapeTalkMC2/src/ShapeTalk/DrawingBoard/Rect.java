package ShapeTalk.DrawingBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Rect extends RectBoundedShape {

	public Rect() {
		super();
	}

	public Rect(Color c, BasicStroke s, int x, int y, int f) {
		super(c, s, x, y, f);
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
		if (filled == 0)
			g.drawRect(x, y, w, h);
		else
			g.fillRect(x, y, w, h);

	}

}
