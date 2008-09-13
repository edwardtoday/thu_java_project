package ShapeTalk;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Simple web browser to view online javadoc.
 * 
 * @author Q
 */
public class WebBrowser extends JFrame {
	/**
	 * Application test main method.
	 * 
	 * @param args
	 */
	public static void main(final String args[]) {
		final WebBrowser application = new WebBrowser();

		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Set up GUI
	 */
	public WebBrowser() {
		super("ShapeTalk Online Javadoc - QING Pei");

		final Container container = getContentPane();

		// create enterField and register its listener
		// enterField = new
		// JTextField("http://edwardtoday.net9.org/doc/index.html");
		// enterField.setVisible(false)
		// ; enterField.addActionListener(

		// new ActionListener() {

		// get document specified by user
		// public void actionPerformed(ActionEvent event) {
		// getThePage(event.getActionCommand());
		// }

		// } // end anonymous inner class

		// ); // end call to addActionListener

		// container.add(enterField, BorderLayout.NORTH);

		// create contentsArea and register HyperlinkEvent listener
		contentsArea = new JEditorPane();
		contentsArea.setEditable(false);

		contentsArea.addHyperlinkListener(

		new HyperlinkListener() {

			/**
			 * 
			 * if user clicked hyperlink, go to specified page
			 * 
			 * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
			 */
			public void hyperlinkUpdate(final HyperlinkEvent event) {
				if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					getThePage(event.getURL().toString());
				}
			}

		}

		);

		container.add(new JScrollPane(contentsArea), BorderLayout.CENTER);

		setSize(800, 600);
		setVisible(true);

		getThePage("http://edwardtoday.net9.org/doc/index.html");
	}

	/**
	 * Load document; change mouse cursor to indicate status
	 * 
	 * @param location
	 */
	private void getThePage(final String location) {
		// change mouse cursor to WAIT_CURSOR
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// load document into contentsArea and display location in
		// enterField
		try {
			contentsArea.setPage(location);
			// enterField.setText(location);
		}

		// process problems loading document
		catch (final IOException ioException) {
			JOptionPane.showMessageDialog(this,
					"Error   retrieving   specified   URL", "Bad   URL",
					JOptionPane.ERROR_MESSAGE);
		}

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	// private JTextField enterField;
	/**
	 * Panel to show the javadoc.
	 */
	private final JEditorPane contentsArea;

}
