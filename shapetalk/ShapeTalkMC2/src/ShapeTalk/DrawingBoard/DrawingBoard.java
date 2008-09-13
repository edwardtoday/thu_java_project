package ShapeTalk.DrawingBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class DrawingBoard extends JPanel implements MouseListener,
		MouseMotionListener {

	public static final Stroke[] ERASER_STROKES = new Stroke[] {
			new BasicStroke(15.0f), new BasicStroke(20.0f),
			new BasicStroke(30.0f), new BasicStroke(50.0f),
			new BasicStroke(100.0f) };
	public static final Stroke[] STROKES = new Stroke[] {
			new BasicStroke(1.0f), new BasicStroke(2.0f),
			new BasicStroke(5.0f), new BasicStroke(7.5f),
			new BasicStroke(10.0f) };
	public static final int TOOL_DIAMOND = 3;
	public static final int TOOL_ERASER = 5;
	public static final int TOOL_LINE = 0;
	public static final int TOOL_OVAL = 2;
	public static final int TOOL_PENCIL = 4;
	public static final int TOOL_RECT = 1;
	public static final int TOOL_TEXT = 6;
	public static final int TOOL_ROUNDRECT = 7;
	public static final int TOOL_SELE = 8;

	public DrawingBoard() {
		shapes = new ArrayList();
		tool = DrawingBoard.TOOL_LINE;
		currentShape = null;
		strokeIndex = 0;
		eraserIndex = 0;

		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setOpaque(true);
		setForeground(Color.black);
		setBackground(Color.white);

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void clearBoard() {
		shapes.clear();
		repaint();
	}

	public String getShapes() {
		final int size = shapes.size();
		final StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < size; i++) {
			final IShape shape = (IShape) shapes.get(i);
			buffer.append("\n");
			buffer.append(shape.getClass().getName());
			buffer.append("\t");
			buffer.append(shape.getShapeData());
		}
		return buffer.toString();
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		if (currentShape != null) {
			currentShape.processCursorEvent(e, IShape.CURSOR_DRAGGED);
			repaint();
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (fill == 0)
				this.setForeground(strokeColor);
			else
				this.setForeground(fillColor);

			switch (tool) {
			case TOOL_LINE:
				this.setForeground(strokeColor);
				currentShape = new Line(getForeground(), new BasicStroke(
						strokeIndex), e.getX(), e.getY(), fill);
				break;
			case TOOL_RECT:
				currentShape = new Rect(getForeground(), new BasicStroke(
						strokeIndex), e.getX(), e.getY(), fill);
				break;
			case TOOL_OVAL:
				currentShape = new Oval(getForeground(), new BasicStroke(
						strokeIndex), e.getX(), e.getY(), fill);
				break;
			case TOOL_DIAMOND:
				currentShape = new Diamond(getForeground(), new BasicStroke(
						strokeIndex), e.getX(), e.getY(), fill);
				break;
			case TOOL_PENCIL:
				this.setForeground(strokeColor);
				currentShape = new PolyLine(getForeground(), new BasicStroke(
						strokeIndex), e.getX(), e.getY());
				break;
			case TOOL_ERASER:
				currentShape = new Eraser(this,
						DrawingBoard.ERASER_STROKES[eraserIndex], e.getX(), e
								.getY());
				break;
			case TOOL_TEXT:
				currentShape = new Text(string2draw, getForeground(), e.getX(),
						e.getY(), font);
				break;
			case TOOL_ROUNDRECT:
				currentShape = new RoundRect(getForeground(), new BasicStroke(
						strokeIndex), e.getX(), e.getY(), fill);
				break;
			case TOOL_SELE:

				break;
			}
			shapes.add(currentShape);
			repaint();
		} else if (e.getButton() == MouseEvent.BUTTON3 && currentShape != null) {
			currentShape.processCursorEvent(e, IShape.RIGHT_PRESSED);
			repaint();
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && currentShape != null) {
			currentShape.processCursorEvent(e, IShape.LEFT_RELEASED);
			currentShape = null;
			repaint();
		}
	}

	public void setEraserIndex(int i) {
		if (i < 0 || i > 4) {
			throw new IllegalArgumentException("Invaild Size Specified!");
		}
		eraserIndex = i;
	}

	public void setShapes(ArrayList list) throws Exception {
		try {
			final int size = list.size();
			for (int i = 0; i < size; i++) {
				final String[] split2 = ((String) list.get(i)).split("\t");
				IShape shape;
				if (split2[0].equals("ShapeTalk.DrawingBoard.Line")) {
					shape = new Line();
				} else if (split2[0].equals("ShapeTalk.DrawingBoard.Rect")) {
					shape = new Rect();
				} else if (split2[0].equals("ShapeTalk.DrawingBoard.RoundRect")) {
					shape = new RoundRect();
				} else if (split2[0].equals("ShapeTalk.DrawingBoard.Oval")) {
					shape = new Oval();
				} else if (split2[0].equals("ShapeTalk.DrawingBoard.Diamond")) {
					shape = new Diamond();
				} else if (split2[0].equals("ShapeTalk.DrawingBoard.PolyLine")) {
					shape = new PolyLine();
				} else if (split2[0].equals("ShapeTalk.DrawingBoard.Eraser")) {
					shape = new Eraser(this);
				} else if (split2[0].equals("ShapeTalk.DrawingBoard.Text")) {
					shape = new Text();
				} else
					throw new Exception("Invalid Shape Data!");
				shape.setShapeData(split2[1]);
				list.set(i, shape);
			}
			shapes = list;
			repaint();
		} catch (final Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void setStrokeIndex(int i) {
		// if (i < 0 || i > 4) {
		// throw new IllegalArgumentException("Invaild Weight Specified!");
		// }
		strokeIndex = i;
	}

	public void setTool(int t) {
		if (t < DrawingBoard.TOOL_LINE || t > DrawingBoard.TOOL_SELE) {
			throw new IllegalArgumentException("Invaild Tool Specified!");
		}
		tool = t;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final int size = shapes.size();
		final Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		for (int i = 0; i < size; i++) {
			((IShape) shapes.get(i)).draw(g2d);
		}
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font f) {
		if (f != null)
			font = f;
	}

	public int getFill() {
		return fill;
	}

	public void setFill(boolean f) {
		fill = f ? 1 : 0;
	}

	public void setStrokeColor(Color c) {
		if (c != null)
			strokeColor = c;
	}

	public void setFillColor(Color c) {
		if (c != null)
			fillColor = c;
	}

	private IShape currentShape;

	private ArrayList shapes;

	private static int strokeIndex, eraserIndex;

	private static Font font = new Font("Arial", Font.PLAIN, 18);
	private static String string2draw = "set your own string";

	private static int tool;
	private static int fill = 0;
	private static Color strokeColor = Color.BLACK, fillColor = Color.WHITE;
}
