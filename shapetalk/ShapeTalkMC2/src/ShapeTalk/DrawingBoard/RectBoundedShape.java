package ShapeTalk.DrawingBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.MouseEvent;

public abstract class RectBoundedShape implements IShape {

	protected RectBoundedShape() {
	}

	protected RectBoundedShape(Color c, BasicStroke s, int x, int y, int f) {
		color = c;
		stroke = s;
		startX = endX = x;
		startY = endY = y;
		filled = f;
	}

	public String getShapeData() {
//		int si = 0;
//		for (int i = 0; i < DrawingBoard.STROKES.length; i++) {
//			if (stroke == DrawingBoard.STROKES[i]) {
//				si = i;
//				break;
//			}
//		}
		float si=stroke.getLineWidth();
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

	public void processCursorEvent(MouseEvent e, int t) {
		if (t != IShape.CURSOR_DRAGGED)
			return;
		final int x = e.getX();
		final int y = e.getY();
		if (e.isShiftDown()) {
			regulateShape(x, y);
		} else {
			endX = x;
			endY = y;
		}
	}

	public void setShapeData(String data) throws Exception {
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
	 */
	protected void regulateShape(int x, int y) {
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

	protected Color color;

	protected int startX, startY, endX, endY;

	protected BasicStroke stroke;

	protected int filled;

}
