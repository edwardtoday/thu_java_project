package ShapeTalk.DrawingBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

public class Text implements IShape {

	public Text() {
		pointsSet = new PointsSet();
	}

	public Text(Color c, BasicStroke s, int x, int y) {
		this();
		color = c;
		stroke = s;
		pointsSet.addPoint(x, y);
		currX = x;
		currY = y;
		finalized = false;
	}

	public void draw(Graphics2D g) {
		g.setColor(color);
		g.setStroke(stroke);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (finalized) { // draw a close text
			final int[][] points = pointsSet.getPoints();
			if (points == null)
				return;
			g.drawPolygon(points[0], points[1], points[0].length);
		} else { // don't draw a closed text
			final int[][] points = pointsSet.getPoints(currX, currY);
			g.drawPolyline(points[0], points[1], points[0].length);
		}
	}

	public String getShapeData() {
		float si = stroke.getLineWidth();
		final StringBuffer buffer = new StringBuffer();
		buffer.append(color.getRGB());
		buffer.append(":");
		buffer.append(si);
		final int[][] ps = pointsSet.getPoints();
		for (int i = 0; i < ps[0].length; i++) {
			buffer.append(":");
			buffer.append(ps[0][i]);
			buffer.append(":");
			buffer.append(ps[1][i]);
		}
		return buffer.toString();
	}

	public void processCursorEvent(MouseEvent e, int t) {
		currX = e.getX();
		currY = e.getY();
		if (t == IShape.RIGHT_PRESSED) {
			pointsSet.addPoint(currX, currY);
		} else if (t == IShape.LEFT_RELEASED) {
			finalized = true;
			pointsSet.addPoint(currX, currY);
		}
	}

	public void setShapeData(String data) throws Exception {
		final String splits[] = data.split(":");
		color = new Color(Integer.parseInt(splits[0]));
		stroke = new BasicStroke(Float.parseFloat(splits[1]));
		for (int i = 2; i < splits.length; i += 2) {
			pointsSet.addPoint(Integer.parseInt(splits[i]), Integer
					.parseInt(splits[i + 1]));
		}
		finalized = true;
	}

	private Color color;

	private int currX, currY;

	private boolean finalized;

	private final PointsSet pointsSet;

	private BasicStroke stroke;

}
