package ShapeTalk.DrawingBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class RoundRect extends RectBoundedShape {

	public RoundRect() {
		super();
	}

	public RoundRect(Color c, BasicStroke s, int x, int y, int f) {
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
			g.drawRoundRect(x, y, w, h, (int) (w / 5), (int) (h / 5));
		else
			g.fillRoundRect(x, y, w, h, (int) (w / 5), (int) (h / 5));
	}

}
