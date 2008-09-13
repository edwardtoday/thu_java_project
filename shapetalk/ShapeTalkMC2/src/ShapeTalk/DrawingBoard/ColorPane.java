package ShapeTalk.DrawingBoard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * ColorPane.java
 * 
 * Show a square with foreground/background color shown.
 * 
 * @author Q
 * 
 */
public class ColorPane extends JPanel {
	/**
	 * Constructor of ColorPane with no parameters.
	 */
	public ColorPane() {
		stroke_color = Color.BLACK;
		fill_color = Color.WHITE;

		setPreferredSize(new Dimension(size, size));
	}

	/**
	 * Constructor of ColorPane with parameter: size.
	 * 
	 * @param size
	 */
	public ColorPane(final int size) {
		stroke_color = Color.BLACK;
		fill_color = Color.WHITE;
		this.size = size;
		setPreferredSize(new Dimension(size, size));
	}

	/**
	 * Getter of fillColor.
	 */
	public Color getFillColor() {
		return fill_color;
	}

	/**
	 * Getter of strokeColor
	 * 
	 */
	public Color getStrokeColor() {
		return stroke_color;
	}

	/**
	 * Paint the panel.
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 * 
	 * @see ShapeTalk.DrawingBoard.ColorPane#drawButton(Color, Graphics, int,
	 *      int, int, int)
	 */
	@Override
	public void paint(final Graphics g) {
		getWidth();
		getHeight();

		drawButton(fill_color, g, (int) (size / 2 * .8), (int) (size / 2 * .8),
				(size / 2), (size / 2));
		drawButton(stroke_color, g, (int) (size / 2 * .2),
				(int) (size / 2 * .2), (size / 2), (size / 2));
	}

	/**
	 * Setter of fillColor.
	 * 
	 * @param c
	 */
	public void setFillColor(final Color c) {
		fill_color = c;
		paint(getGraphics());
	}

	/**
	 * Setter of StrokeColor.
	 * 
	 * @param c
	 */
	public void setStrokeColor(final Color c) {
		stroke_color = c;
		paint(getGraphics());
	}

	/**
	 * Draw a square of the size (width,height) at (x,y) filled with color.
	 * 
	 * @param color
	 * @param g
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	private void drawButton(final Color color, final Graphics g, final int x,
			final int y, final int width, final int height) {
		g.setColor(new Color(230, 230, 230));
		g.drawRect(x + 1, y + 1, width - 3, height - 3);
		g.setColor(Color.GRAY);
		g.drawLine(x + 1, y + height - 1, x + width - 1, y + height - 1);
		g.drawLine(x + width - 1, y + 1, x + width - 1, y + height - 1);
		g.setColor(color);
		g.fillRect(x + 2, y + 2, width - 4, height - 4);
	}

	/**
	 * Fill color.
	 */
	private Color fill_color;
	/**
	 * Stroke color.
	 */
	private Color stroke_color;

	/**
	 * ColorPane size.
	 */
	int size = 45;
}