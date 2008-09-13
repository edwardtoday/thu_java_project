package ShapeTalk.DrawingBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * FreeShape.java
 * 
 * Parent of PolyLine and Eraser.
 * 
 * @author Q
 * 
 */
public abstract class FreeShape implements IShape {

	protected FreeShape() {
		pointsSet = new PointsSet(50);
	}

	protected FreeShape(final Color c, final BasicStroke s, final int x,
			final int y) {
		this();
		color = c;
		stroke = s;
		pointsSet.addPoint(x, y);
	}

	/**
	 * This method is only used by PolyLine in this application, so it can be
	 * put in the implementation of PolyLine instead of here. However,
	 * considering further extension by adding subclasses to the FreeShape
	 * class, i choose to put it here.
	 * 
	 * @see ShapeTalk.DrawingBoard.IShape#getShapeData()
	 */
	public String getShapeData() {
		final float si = stroke.getLineWidth();
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

	/**
	 * Get the most south-east point.
	 */
	public Point getSouthEastMostPoint() {
		final int[][] points = pointsSet.getPoints();
		if (points == null) {
			return new Point(0, 0);
		}
		int mx = 0, my = 0;
		for (int i = 0; i < points[0].length; i++) {
			if (points[0][i] > mx) {
				mx = points[0][i];
			}
			if (points[1][i] > my) {
				my = points[1][i];
			}
		}
		return new Point(mx, my);
	}

	/**
	 * Add point to pointSets.
	 * 
	 * @see ShapeTalk.DrawingBoard.IShape#processCursorEvent(java.awt.event.MouseEvent,
	 *      int)
	 * 
	 * @see ShapeTalk.DrawingBoard.FreeShape#pointsSet
	 */
	public void processCursorEvent(final MouseEvent e, final int t) {
		if (t != IShape.CURSOR_DRAGGED) {
			return;
		}
		pointsSet.addPoint(e.getX(), e.getY());
	}

	/**
	 * This method is only used by PolyLine in this application, so it can be
	 * put in the implementation of PolyLine instead of here. However,
	 * considering further extension by adding subclasses to the FreeShape
	 * class, i choose to put it here.
	 * 
	 * @see ShapeTalk.DrawingBoard.IShape#setShapeData(java.lang.String)
	 */
	public void setShapeData(final String data) throws Exception {
		final String splits[] = data.split(":");
		color = new Color(Integer.parseInt(splits[0]));
		stroke = new BasicStroke(Float.parseFloat(splits[1]));
		for (int i = 2; i < splits.length; i += 2) {
			pointsSet.addPoint(Integer.parseInt(splits[i]), Integer
					.parseInt(splits[i + 1]));
		}
	}

	/**
	 * Color of the free shape.
	 */
	protected Color color;

	/**
	 * A set of points that constructs the shape.
	 */
	protected PointsSet pointsSet;

	/**
	 * Stroke of the shape.
	 */
	protected BasicStroke stroke;

}
