package ShapeTalk.DrawingBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class PolyLine extends FreeShape {

	public PolyLine() {
		super();
	}

	public PolyLine(Color c, BasicStroke s, int x, int y) {
		super(c, s, x, y);
	}

	public void draw(Graphics2D g) {
		g.setColor(color);
		g.setStroke(stroke);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		final int[][] points = pointsSet.getPoints();
		if (points == null)
			return;
		final int s = points[0].length;
		if (s == 1) {
			final int x = points[0][0];
			final int y = points[1][0];
			g.drawLine(x, y, x, y);
		} else {
			g.drawPolyline(points[0], points[1], s);
		}
	}

}
