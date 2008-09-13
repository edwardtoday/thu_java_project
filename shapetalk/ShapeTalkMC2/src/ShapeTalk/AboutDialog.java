package ShapeTalk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;

public class AboutDialog extends JDialog {

	public AboutDialog(JFrame frame) {
		setTitle("About");
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();// 得到屏幕的大小
		final int MARGIN = 50;
		this.setBounds((MARGIN / 2), (MARGIN / 2),
				(int) ((screen.getWidth() - MARGIN)), (int) ((screen
						.getHeight()
						- MARGIN - 20)));
		final JEditorPane docEditorPane = new JEditorPane();

		try {
			final File doc = new File("/doc/index.html");
			final URL url = doc.toURL();
			System.out.println(url);
			docEditorPane.setEditable(false);

			docEditorPane
					.setPage(new URL("http://code.google.com/p/shapetalk/"));
			getContentPane().add(docEditorPane, BorderLayout.CENTER);
			this.show();
		} catch (final MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}