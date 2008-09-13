package ShapeTalk.DrawingBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

public class Text implements IShape {

	public Text() {
	}

	public Text(String s, Color c, int x, int y, Font f) {
		this();
		string = s;
		color = c;
		X = x;
		Y = y;
		font = f;
	}

	public void draw(Graphics2D g) {
		g.setColor(color);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(font);
		g.drawString(string, X, Y);
	}

	public String getShapeData() {
		String si =string;
		final StringBuffer buffer = new StringBuffer();
		buffer.append(color.getRGB());
		buffer.append(":");
		buffer.append(si);
		buffer.append(":");
		buffer.append(X);
		buffer.append(":");
		buffer.append(Y);
		buffer.append(":");
		buffer.append(font.getFontName());
		buffer.append(":");
		buffer.append(font.getStyle());
		buffer.append(":");
		buffer.append(font.getSize());
		return buffer.toString();
	}

	public void processCursorEvent(MouseEvent e, int t) {
		X = e.getX();
		Y = e.getY();
	}

	public void setShapeData(String data) throws Exception {
		final String splits[] = data.split(":");
		color = new Color(Integer.parseInt(splits[0]));
		string=splits[1];
		X=Integer.parseInt(splits[2]);
		Y=Integer.parseInt(splits[3]);
		font=new Font(splits[4],Integer.parseInt(splits[5]),Integer.parseInt(splits[6]));
	}

	private String string;

	private Color color;

	private int X, Y;

	private Font font;

}
