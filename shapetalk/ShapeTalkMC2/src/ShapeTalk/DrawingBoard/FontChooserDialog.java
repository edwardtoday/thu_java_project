package ShapeTalk.DrawingBoard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * FontChooserDialog.java
 * 
 * Code of the font chooser dialog.
 * 
 * @author Q
 * 
 */
public class FontChooserDialog extends JDialog {
	/**
	 * Font List
	 * 
	 * @author Q
	 */
	public class FontList extends JPanel {
		public FontList(final String[] values) {
			setLayout(null);
			jl = new JList(values);
			sp = new JScrollPane(jl);
			jt = new JLabel();
			jt.setBackground(Color.white);
			jt.setForeground(Color.black);
			jt.setOpaque(true);
			jt.setBorder(new JTextField().getBorder());
			jt.setFont(getFont());
			jl.setBounds(0, 0, 100, 1000);
			jl.setBackground(Color.white);
			jl.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(final ListSelectionEvent e) {
					jt.setText((String) jl.getSelectedValue());
					si = (String) jl.getSelectedValue();
					showSample();
				}
			});
			add(sp);
			add(jt);
		}

		public String getSelectedValue() {
			return si;
		}

		/**
		 * @see java.awt.Component#setBounds(int, int, int, int)
		 */
		@Override
		public void setBounds(final int x, final int y, final int w, final int h) {
			super.setBounds(x, y, w, h);
			sp.setBounds(0, y + 12, w, h - 23);
			sp.revalidate();
			jt.setBounds(0, 0, w, 20);
		}

		public void setSelectedItem(final String s) {
			jl.setSelectedValue(s, true);
		}

		JList jl;

		JLabel jt;

		String si = "   ";

		JScrollPane sp;

	}

	static JLabel Sample = new JLabel();

	/**
	 * 
	 * @param parent
	 * @param s
	 * @param font
	 */
	public static Font showDialog(final Frame parent, final String s,
			final Font font) {
		final FontChooserDialog fd = new FontChooserDialog(parent, true, font);
		if (s != null) {
			fd.setTitle(s);
		}
		fd.setVisible(true);
		Font fo = null;
		if (fd.ob) {
			fo = FontChooserDialog.Sample.getFont();
		}
		fd.dispose();
		return fo;
	}

	/**
	 * @param parent
	 * @param modal
	 * @param font
	 */
	private FontChooserDialog(final Frame parent, final boolean modal, Font font) {
		super(parent, modal);
		initAll();
		setTitle("Font   Choosr");
		if (font == null) {
			font = FontChooserDialog.Sample.getFont();
		}
		FontList.setSelectedItem(font.getName());
		SizeList.setSelectedItem(font.getSize() + "");
		StyleList.setSelectedItem(styleList[font.getStyle()]);

	}

	/**
	 * Add buttons.
	 */
	private void addButtons() {
		final JButton ok = new JButton("Ok");
		ok.setMargin(new Insets(0, 0, 0, 0));
		final JButton ca = new JButton("Cancel");
		ca.setMargin(new Insets(0, 0, 0, 0));
		ok.setBounds(260, 350, 70, 20);
		ok.setFont(new Font("   ", 1, 11));
		ca.setBounds(340, 350, 70, 20);
		ca.setFont(new Font("   ", 1, 12));
		getContentPane().add(ok);
		getContentPane().add(ca);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setVisible(false);
				ob = true;
			}
		});
		ca.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setVisible(false);
				ob = false;
			}
		});
	}

	/**
	 * Add font list.
	 */
	private void addLists() {
		FontList = new FontList(GraphicsEnvironment
				.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		StyleList = new FontList(styleList);
		SizeList = new FontList(sizeList);
		FontList.setBounds(10, 10, 260, 295);
		StyleList.setBounds(280, 10, 80, 295);
		SizeList.setBounds(370, 10, 40, 295);
		getContentPane().add(FontList);
		getContentPane().add(StyleList);
		getContentPane().add(SizeList);
	}

	/**
	 * Initialize font chooser dialog.
	 */
	private void initAll() {
		getContentPane().setLayout(null);
		setBounds(50, 50, 425, 400);
		addLists();
		addButtons();
		FontChooserDialog.Sample.setBounds(10, 320, 415, 25);
		FontChooserDialog.Sample.setForeground(Color.black);
		getContentPane().add(FontChooserDialog.Sample);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final java.awt.event.WindowEvent e) {
				setVisible(false);
			}
		});
	}

	/**
	 * Show font preview.
	 */
	private void showSample() {
		int g = 0;
		try {
			g = Integer.parseInt(SizeList.getSelectedValue());
		} catch (final NumberFormatException nfe) {
		}
		final String st = StyleList.getSelectedValue();
		int s = Font.PLAIN;
		if (st.equalsIgnoreCase("Bold")) {
			s = Font.BOLD;
		}
		if (st.equalsIgnoreCase("Italic")) {
			s = Font.ITALIC;
		}
		FontChooserDialog.Sample.setFont(new Font(FontList.getSelectedValue(),
				s, g));
		FontChooserDialog.Sample
				.setText("The   quick   brown   fox   jumped   over   the   lazy   dog.");
	}

	/**
	 * List of fonts.
	 */
	FontList FontList;

	boolean ob = false;

	String[] sizeList = new String[] { "3", "4", "5", "6", "7", "8", "9", "10",
			"11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "22",
			"24", "27", "30", "34", "39", "45", "51", "60" };

	FontList SizeList;

	String[] styleList = new String[] { "Plain", "Bold", "Italic" };

	FontList StyleList;
}