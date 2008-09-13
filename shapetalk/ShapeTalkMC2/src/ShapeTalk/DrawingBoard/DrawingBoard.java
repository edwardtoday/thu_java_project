package ShapeTalk.DrawingBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * DrawingBoard.java
 * 
 * The main drawing canvas class of ShapeTalk.
 * 
 * @author Q
 * 
 */
public class DrawingBoard extends JPanel implements MouseListener,
		MouseMotionListener {

	/**
	 * List of shapes to redraw with "redo" command.
	 */
	public static ArrayList redos = new ArrayList(50);
	/**
	 * List of current shapes on the drawing board.
	 */
	public static ArrayList shapes;
	public static final int TOOL_DIAMOND = 3;
	public static final int TOOL_ERASER = 5;
	public static final int TOOL_LINE = 0;
	public static final int TOOL_OVAL = 2;
	public static final int TOOL_PENCIL = 4;
	public static final int TOOL_RECT = 1;
	public static final int TOOL_ROUNDRECT = 7;
	public static final int TOOL_SELE = 8;
	public static final int TOOL_TEXT = 6;

	/**
	 * Decide whether the next shape is draw filled or not.
	 */
	private static int fill = 0;

	/**
	 * Fill color.
	 */
	private static Color fillColor = Color.WHITE;

	/**
	 * Font of the TEXT tool.
	 */
	private static Font font = new Font("Arial", Font.PLAIN, 18);

	/**
	 * String of the TEXT tool.
	 */
	private static String string2draw = "set your own string";

	/**
	 * Stroke color.
	 */
	private static Color strokeColor = Color.BLACK;

	/**
	 * Stroke index.
	 */
	private static int strokeIndex = 5;

	/**
	 * Current tool.
	 */
	private static int tool;

	/**
	 * Constructs the drawing board.
	 */
	public DrawingBoard() {
		DrawingBoard.shapes = new ArrayList();
		DrawingBoard.tool = DrawingBoard.TOOL_LINE;
		currentShape = null;
		DrawingBoard.strokeIndex = 0;
		// DrawingBoard.eraserIndex = 0;

		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setOpaque(true);
		setForeground(Color.black);
		setBackground(Color.white);

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/**
	 * Clear all shapes.
	 */
	public void clearBoard() {
		System.out.print(DrawingBoard.shapes.get(0));
		DrawingBoard.shapes.clear();
		repaint();
	}

	/**
	 * Getter of fillColor.
	 */
	public int getFill() {
		return DrawingBoard.fill;
	}

	/**
	 * Getter of font.
	 * 
	 * @see java.awt.Component#getFont()
	 */
	@Override
	public Font getFont() {
		return DrawingBoard.font;
	}

	/**
	 * Get all current shapes.
	 */
	public String getShapes() {
		final int size = DrawingBoard.shapes.size();
		final StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < size; i++) {
			final IShape shape = (IShape) DrawingBoard.shapes.get(i);
			buffer.append("\n");
			buffer.append(shape.getClass().getName());
			buffer.append("\t");
			buffer.append(shape.getShapeData());
		}
		return buffer.toString();
	}

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(final MouseEvent e) {
	}

	/**
	 * When mouse dragged, show preview of the shape to draw at current cursor
	 * position.
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(final MouseEvent e) {
		if (currentShape != null) {
			currentShape.processCursorEvent(e, IShape.CURSOR_DRAGGED);
			repaint();
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(final MouseEvent e) {
	}

	/**
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(final MouseEvent e) {
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(final MouseEvent e) {
	}

	/**
	 * When mouse pressed, store the cursor position and set the shape info.
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(final MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (DrawingBoard.fill == 0) {
				setForeground(DrawingBoard.strokeColor);
			} else {
				setForeground(DrawingBoard.fillColor);
			}

			switch (DrawingBoard.tool) {
			case TOOL_LINE:
				setForeground(DrawingBoard.strokeColor);
				currentShape = new Line(getForeground(), new BasicStroke(
						DrawingBoard.strokeIndex), e.getX(), e.getY(),
						DrawingBoard.fill);
				break;
			case TOOL_RECT:
				currentShape = new Rect(getForeground(), new BasicStroke(
						DrawingBoard.strokeIndex), e.getX(), e.getY(),
						DrawingBoard.fill);
				break;
			case TOOL_OVAL:
				currentShape = new Oval(getForeground(), new BasicStroke(
						DrawingBoard.strokeIndex), e.getX(), e.getY(),
						DrawingBoard.fill);
				break;
			case TOOL_DIAMOND:
				currentShape = new Diamond(getForeground(), new BasicStroke(
						DrawingBoard.strokeIndex), e.getX(), e.getY(),
						DrawingBoard.fill);
				break;
			case TOOL_PENCIL:
				setForeground(DrawingBoard.strokeColor);
				currentShape = new PolyLine(getForeground(), new BasicStroke(
						DrawingBoard.strokeIndex), e.getX(), e.getY());
				break;
			case TOOL_ERASER:
				currentShape = new Eraser(this, new BasicStroke(
						DrawingBoard.strokeIndex), e.getX(), e.getY());
				break;
			case TOOL_TEXT:
				currentShape = new Text(DrawingBoard.string2draw,
						getForeground(), e.getX(), e.getY(), DrawingBoard.font);
				break;
			case TOOL_ROUNDRECT:
				currentShape = new RoundRect(getForeground(), new BasicStroke(
						DrawingBoard.strokeIndex), e.getX(), e.getY(),
						DrawingBoard.fill);
				break;
			case TOOL_SELE:

				break;
			}
			DrawingBoard.shapes.add(currentShape);
			repaint();
		} else if (e.getButton() == MouseEvent.BUTTON3 && currentShape != null) {
			currentShape.processCursorEvent(e, IShape.RIGHT_PRESSED);
			repaint();
		}
	}

	/**
	 * When mouse released, draw the shape and update the drawing board.
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(final MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && currentShape != null) {
			currentShape.processCursorEvent(e, IShape.LEFT_RELEASED);
			currentShape = null;
			repaint();
		}
	}

	/**
	 * Setter of fill.
	 * 
	 * @param f
	 * @see ShapeTalk.DrawingBoard.DrawingBoard#fill
	 */
	public void setFill(final boolean f) {
		DrawingBoard.fill = f ? 1 : 0;
	}

	/**
	 * Setter of fillColor.
	 * 
	 * @param c
	 */
	public void setFillColor(final Color c) {
		if (c != null) {
			DrawingBoard.fillColor = c;
		}
	}

	/**
	 * Setter of font.
	 * 
	 * @see javax.swing.JComponent#setFont(java.awt.Font)
	 */
	@Override
	public void setFont(final Font f) {
		if (f != null) {
			DrawingBoard.font = f;
		}
	}

	/**
	 * Rebuild the shapes list.
	 * 
	 * @param list
	 * @throws Exception
	 */
	public void setShapes(final ArrayList list) throws Exception {
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
				} else {
					throw new Exception("Invalid Shape Data!");
				}
				shape.setShapeData(split2[1]);
				list.set(i, shape);
			}
			DrawingBoard.shapes = list;
			repaint();
		} catch (final Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Setter of string2draw.
	 * 
	 * @param s
	 */
	public void setString(final String s) {
		DrawingBoard.string2draw = s;
	}

	/**
	 * Setter of strokeColor.s
	 * 
	 * @param c
	 */
	public void setStrokeColor(final Color c) {
		if (c != null) {
			DrawingBoard.strokeColor = c;
		}
	}

	/**
	 * Setter of strokeIndex.
	 * 
	 * @param i
	 */
	public void setStrokeIndex(final int i) {
		DrawingBoard.strokeIndex = i;
	}

	/**
	 * Setter of current tool.
	 * 
	 * @param t
	 */
	public void setTool(final int t) {
		if (t < DrawingBoard.TOOL_LINE || t > DrawingBoard.TOOL_SELE) {
			throw new IllegalArgumentException("Invaild Tool Specified!");
		}
		DrawingBoard.tool = t;
	}

	/**
	 * Paint the shapes on the drawing board.
	 * 
	 * Override the paintComponent method.
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		final int size = DrawingBoard.shapes.size();
		final Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		for (int i = 0; i < size; i++) {
			((IShape) DrawingBoard.shapes.get(i)).draw(g2d);
		}
	}

	/**
	 * Current shape.
	 */
	private IShape currentShape;
}
