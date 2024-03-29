package ShapeTalk.DrawingBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

/**
 * Line.java
 * 
 * Code of the Line tool.
 * 
 * @author Q
 * 
 */
public class Line extends RectBoundedShape {

	public Line() {
		super();
	}

	public Line(final Color c, final BasicStroke s, final int x, final int y,
			final int f) {
		super(c, s, x, y, f);
	}

	/**
	 * Draw the line.
	 * 
	 * @see ShapeTalk.DrawingBoard.IShape#draw(java.awt.Graphics2D)
	 */
	public void draw(final Graphics2D g) {
		g.setColor(color);
		g.setStroke(stroke);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawLine(startX, startY, endX, endY);
	}

	/**
	 * Line object doesn't follow the cursor event processing specifed by its
	 * superclass GeometryShape. The difference lies in the processing when
	 * shift mask is present. Line will not always regulate its bounding
	 * rectangle to a square. There will be three cases: the bounding rectangle
	 * is either square, a horizontal line or a vertical line, depending the
	 * slope of the line.
	 * 
	 * tan(30) = 0.577; tan(60) = 1.155;
	 * 
	 * @see ShapeTalk.DrawingBoard.RectBoundedShape#processCursorEvent(java.awt.event.MouseEvent,
	 *      int)
	 */
	@Override
	public void processCursorEvent(final MouseEvent e, final int t) {
		if (t != IShape.CURSOR_DRAGGED) {
			return;
		}
		final int x = e.getX();
		final int y = e.getY();
		if (e.isShiftDown()) {
			if (x - startX == 0) { // vertical
				endX = startX;
				endY = y;
			} else {
				final float slope = Math.abs((float) (y - startY)
						/ (x - startX));
				if (slope < 0.577) { // horizontal
					endX = x;
					endY = startY;
				} else if (slope < 1.155) { // deg45
					regulateShape(x, y);
				} else { // vertical
					endX = startX;
					endY = y;
				}
			}
		} else {
			endX = x;
			endY = y;
		}
	}

}
