package ShapeTalk.DrawingBoard;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.JComponent;

public class Eraser extends FreeShape {

	public Eraser(JComponent board) {
		this.board = board;
	}

	public Eraser(JComponent board, BasicStroke s, int x, int y) {
		super(null, s, x, y);
		this.board = board;
	}

	public void draw(Graphics2D g) {
		g.setColor(board.getBackground());
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

	@Override
	public String getShapeData() {
		// int si = 0;
		// for (int i = 0; i < DrawingBoard.ERASER_STROKES.length; i++) {
		// if (stroke == DrawingBoard.ERASER_STROKES[i]) {
		// si = i;
		// break;
		// }
		// }
		float si = stroke.getLineWidth();
		final StringBuffer buffer = new StringBuffer();
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

	@Override
	public void setShapeData(String data) throws Exception {
		final String splits[] = data.split(":");
		stroke = new BasicStroke(Float.parseFloat(splits[0]));
		for (int i = 1; i < splits.length; i += 2) {
			pointsSet.addPoint(Integer.parseInt(splits[i]), Integer
					.parseInt(splits[i + 1]));
		}
	}

	private final JComponent board;

}
