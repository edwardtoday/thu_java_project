package ShapeTalk.DrawingBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.MouseEvent;

/**
 * RectBoundedShape.java
 * 
 * Parent of all the rectangle bounded shape (,like rectangle, diamond, etc).
 * 
 * @author Q
 */
public abstract class RectBoundedShape implements IShape {

	protected RectBoundedShape() {
	}

	/**
	 * @param c
	 * @param s
	 * @param x
	 * @param y
	 * @param f
	 */
	protected RectBoundedShape(final Color c, final BasicStroke s, final int x,
			final int y, final int f) {
		color = c;
		stroke = s;
		startX = endX = x;
		startY = endY = y;
		filled = f;
	}

	/**
	 * @see ShapeTalk.DrawingBoard.IShape#getShapeData()
	 */
	public String getShapeData() {
		final float si = stroke.getLineWidth();
		final StringBuffer buffer = new StringBuffer();
		buffer.append(color.getRGB());
		buffer.append(":");
		buffer.append(si);
		buffer.append(":");
		buffer.append(startX);
		buffer.append(":");
		buffer.append(startY);
		buffer.append(":");
		buffer.append(endX);
		buffer.append(":");
		buffer.append(endY);
		buffer.append(":");
		buffer.append(filled);
		return buffer.toString();
	}

	/**
	 * @see ShapeTalk.DrawingBoard.IShape#processCursorEvent(java.awt.event.MouseEvent,
	 *      int)
	 */
	public void processCursorEvent(final MouseEvent e, final int t) {
		if (t != IShape.CURSOR_DRAGGED) {
			return;
		}
		final int x = e.getX();
		final int y = e.getY();
		if (e.isShiftDown()) {
			regulateShape(x, y);
		} else {
			endX = x;
			endY = y;
		}
	}

	/**
	 * @see ShapeTalk.DrawingBoard.IShape#setShapeData(java.lang.String)
	 */
	public void setShapeData(final String data) throws Exception {
		final String[] splits = data.split(":");
		color = new Color(Integer.parseInt(splits[0]));
		stroke = new BasicStroke(Float.parseFloat(splits[1]));
		startX = Integer.parseInt(splits[2]);
		startY = Integer.parseInt(splits[3]);
		endX = Integer.parseInt(splits[4]);
		endY = Integer.parseInt(splits[5]);
		filled = Integer.parseInt(splits[6]);
	}

	/**
	 * Regulate the bounding rectangle to a square with ending point coordinate
	 * derived from the specified x, y.
	 * 
	 * @param x
	 * @param y
	 */
	protected void regulateShape(final int x, final int y) {
		final int w = x - startX;
		final int h = y - startY;
		final int s = Math.min(Math.abs(w), Math.abs(h));
		if (s == 0) {
			endX = startX;
			endY = startY;
		} else {
			endX = startX + s * (w / Math.abs(w));
			endY = startY + s * (h / Math.abs(h));
		}
	}

	/**
	 * Shape color.
	 */
	protected Color color;

	/**
	 * Whether shape is filled.
	 */
	protected int filled;

	/**
	 * Position data.
	 */
	protected int startX, startY, endX, endY;

	/**
	 * Stroke data.
	 */
	protected BasicStroke stroke;

}
