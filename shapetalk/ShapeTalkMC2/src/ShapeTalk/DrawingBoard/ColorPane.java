package ShapeTalk.DrawingBoard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class ColorPane extends JPanel {
	public ColorPane() {
		stroke_color = Color.BLACK;
		fill_color = Color.WHITE;

		setPreferredSize(new Dimension(size, size));
	}

	public ColorPane(int size) {
		stroke_color = Color.BLACK;
		fill_color = Color.WHITE;
		this.size = size;
		setPreferredSize(new Dimension(size, size));
	}

	public Color getFillColor() {
		return fill_color;
	}

	public Color getStrokeColor() {
		return stroke_color;
	}

	@Override
	public void paint(Graphics g) {
		getWidth();
		getHeight();

		drawButton(fill_color, g, (int) (size / 2 * .8), (int) (size / 2 * .8),
				(size / 2), (size / 2));
		drawButton(stroke_color, g, (int) (size / 2 * .2),
				(int) (size / 2 * .2), (size / 2), (size / 2));
	}

	public void setFillColor(Color c) {
		fill_color = c;
		paint(getGraphics());
	}

	public void setStrokeColor(Color c) {
		stroke_color = c;
		paint(getGraphics());
	}

	private void drawButton(Color color, Graphics g, int x, int y, int width,
			int height) {
		g.setColor(new Color(230, 230, 230));
		g.drawRect(x + 1, y + 1, width - 3, height - 3);
		g.setColor(Color.GRAY);
		g.drawLine(x + 1, y + height - 1, x + width - 1, y + height - 1);
		g.drawLine(x + width - 1, y + 1, x + width - 1, y + height - 1);
		g.setColor(color);
		g.fillRect(x + 2, y + 2, width - 4, height - 4);
	}

	private Color stroke_color, fill_color;

	int size = 45;
}