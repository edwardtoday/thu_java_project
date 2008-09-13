package ShapeTalk.DrawingBoard;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * IShape.java
 * 
 * Interface that all the shapes should implement.
 * 
 * @author Q
 * 
 */
public interface IShape {

	/** Corresponding to mouse dragged event type */
	public static final int CURSOR_DRAGGED = 2;

	/** Corresponding to mouse released event type */
	public static final int LEFT_RELEASED = 1;

	/** Corresponding to mouse pressed event type */
	public static final int RIGHT_PRESSED = 0;

	/**
	 * To be called by the UI to draw out the shape.
	 * 
	 * @param g
	 *            the Graphics2D context being passed for drawing.
	 */
	public void draw(Graphics2D g);

	/**
	 * Called when saving model to file.
	 */
	public String getShapeData();

	/**
	 * Code for processing draw cursor events in the drawing process.
	 * 
	 * @param evt
	 *            the MouseEvent being detected.
	 * @param type
	 *            the event type. can take values PRESSED, RELEASED, and
	 *            DRAGGED.
	 */
	public void processCursorEvent(MouseEvent evt, int type);

	/**
	 * Called when loading model from file.
	 */
	public void setShapeData(String data) throws Exception;

}
