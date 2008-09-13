/**
 * ShapeTalk.java
 * @author QING Pei
 * @version 1.0 Sep 4, 2008
 *
 */
package ShapeTalk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import ShapeTalk.Chat.Channel;
import ShapeTalk.Chat.Manager;
import ShapeTalk.DrawingBoard.ColorPane;
import ShapeTalk.DrawingBoard.DrawingBoard;
import ShapeTalk.DrawingBoard.FontChooserDialog;
import ShapeTalk.DrawingBoard.IShape;

/**
 * @author Q
 * 
 *         Main object of the application: ShapeTalk.
 */
/**
 * @author Q
 *
 */
/**
 * @author Q
 * 
 */
public class ShapeTalk implements WindowListener, MouseListener,
		MouseMotionListener, ItemListener {

	/**
	 * drawingBoard is the main drawing canvas.
	 */
	private static DrawingBoard drawingBoard;

	/**
	 * Decide if the mouse position is shown in status bar
	 */
	private static boolean showPosition = false;

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(final String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					final ShapeTalk window = new ShapeTalk();
					window.bg_frame.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application
	 */
	public ShapeTalk() {
		createContents();
	}

	/**
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(final ItemEvent e) {
	}

	/**
	 * @param evt
	 * 
	 *            Set current tool to LINE
	 */
	public void lineButtonActionPerformed(final ActionEvent evt) {
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_LINE);
	}

	/**
	 * @param evt
	 * 
	 *            Load image from .jdw file
	 */
	public void loadImageMenuItemActionPerformed(final ActionEvent evt) {

		try {
			if (filechooser1 == null) {
				filechooser1 = new JFileChooser();
				filechooser1.setFileFilter(shapeTalkDrawFilter);
				filechooser1.setMultiSelectionEnabled(false);
				filechooser1.setAcceptAllFileFilterUsed(false);
			}
			final int retVal = filechooser1.showOpenDialog(bg_frame);
			if (retVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			final BufferedReader br = new BufferedReader(new FileReader(
					filechooser1.getSelectedFile()));
			int rgb = Integer.parseInt(br.readLine());
			final Color fgcolor = new Color(rgb);
			ShapeTalk.drawingBoard.setForeground(fgcolor);
			strokeColorButton.setBackground(fgcolor);
			rgb = Integer.parseInt(br.readLine());
			final Color bgcolor = new Color(rgb);
			ShapeTalk.drawingBoard.setBackground(bgcolor);
			fillColorButton.setBackground(bgcolor);
			// weightCombo.setSelectedIndex(Integer.parseInt(br.readLine()));
			weightSlider.setValue(Integer.parseInt(br.readLine()));
			weightSlider.setValue(Integer.parseInt(br.readLine()));
			// eraserSlider.setValue(Integer.parseInt(br.readLine()));
			final ArrayList list = new ArrayList();
			String str;
			while ((str = br.readLine()) != null) {
				list.add(str);
			}
			ShapeTalk.drawingBoard.setShapes(list);
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(bg_frame, e, "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 
	 * @see MouseListener#mouseClicked(MouseEvent)
	 */
	public void mouseClicked(final MouseEvent e) {

	}

	/**
	 * 
	 * When showPosition = true, show mouse position in status bar if
	 * mouseDragged.
	 * 
	 * @see MouseMotionListener#mouseDragged(MouseEvent )
	 */
	public void mouseDragged(final MouseEvent e) {
		if (ShapeTalk.showPosition) {
			mousePositionLabel.setText("X=" + e.getX() + " Y=" + e.getY());
		} else {
			mousePositionLabel.setText("X=" + " Y=");
		}
	}

	/**
	 * If mouse cursor enters drawingBoard, set showPosition to true.
	 * 
	 * @see MouseListener#mouseEntered(MouseEvent)
	 */
	public void mouseEntered(final MouseEvent e) {
		if (e.getSource().equals(ShapeTalk.drawingBoard)) {
			ShapeTalk.showPosition = true;
		}
	}

	/**
	 * 
	 * If mouse cursor exits drawingBoard, set showPosition to false.
	 * 
	 * @see MouseListener#mouseExited(MouseEvent)
	 */
	public void mouseExited(final MouseEvent e) {
		if (e.getSource().equals(ShapeTalk.drawingBoard)) {
			ShapeTalk.showPosition = false;
			mousePositionLabel.setText("X=" + " Y=");
		}
	}

	/**
	 * 
	 * When showPosition = true, show mouse position in status bar if
	 * mouseMoved.
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(final MouseEvent e) {
		if (ShapeTalk.showPosition) {
			mousePositionLabel.setText("X=" + e.getX() + " Y=" + e.getY());
		} else {
			mousePositionLabel.setText("X=" + " Y=");
		}
	}

	/**
	 * 
	 * @see MouseListener#mousePressed(MouseEvent)
	 */
	public void mousePressed(final MouseEvent m) {
	}

	/**
	 * If mouseReleased in drawingBoard, update the shapesComboBox.
	 * 
	 * @see MouseListener#mouseReleased(MouseEvent)
	 */
	public void mouseReleased(final MouseEvent m) {
		updateShapesComboBox();
	}

	/**
	 * @param evt
	 * 
	 *            Update the channel list in starting conference panel
	 */
	public void updateChanListActionPerformed(final ActionEvent evt) {
		UpdateChanList();
	}

	/**
	 * Write temp file and update shapes ComboBox.
	 */
	public void updateShapesComboBox() {
		String[] shapes = new String[ShapeTalk.drawingBoard.shapes.size()];
		int index = 0;
		// save temp file
		try {
			final Color fgcolor = ShapeTalk.drawingBoard.getForeground();
			final Color bgcolor = ShapeTalk.drawingBoard.getBackground();
			tempFile = File.createTempFile("shapetalktemp", "jdw");
			final PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
			pw.println(fgcolor.getRGB());
			pw.println(bgcolor.getRGB());
			pw.println(weightSlider.getValue());
			pw.print(weightSlider.getValue());
			pw.print(ShapeTalk.drawingBoard.getShapes());
			pw.close();
		} catch (final java.io.IOException ioe) {
			JOptionPane.showMessageDialog(bg_frame, ioe,
					"Error accessing temp file!", JOptionPane.ERROR_MESSAGE);
		}

		// load from temp file
		try {
			final BufferedReader br = new BufferedReader(new FileReader(
					tempFile));
			int spaceHolder = Integer.parseInt(br.readLine());
			spaceHolder = Integer.parseInt(br.readLine());
			spaceHolder = Integer.parseInt(br.readLine());
			spaceHolder = Integer.parseInt(br.readLine());
			final ArrayList list = new ArrayList();
			String str;
			while ((str = br.readLine()) != null) {
				list.add(str);
			}

			try {
				final int size = list.size();
				for (int i = 0; i < size; i++) {
					final String[] split2 = ((String) list.get(i)).split("\t");
					final IShape shape;
					if (split2[0].equals("ShapeTalk.DrawingBoard.Line")) {
					} else if (split2[0].equals("ShapeTalk.DrawingBoard.Rect")) {
					} else if (split2[0]
							.equals("ShapeTalk.DrawingBoard.RoundRect")) {
					} else if (split2[0].equals("ShapeTalk.DrawingBoard.Oval")) {
					} else if (split2[0]
							.equals("ShapeTalk.DrawingBoard.Diamond")) {
					} else if (split2[0]
							.equals("ShapeTalk.DrawingBoard.PolyLine")) {
					} else if (split2[0]
							.equals("ShapeTalk.DrawingBoard.Eraser")) {
					} else if (split2[0].equals("ShapeTalk.DrawingBoard.Text")) {
					} else {
					}
					shapes[index] = split2[0];
					index++;
					System.out.println(split2[0]);
				}
			} catch (final Exception e) {
				e.printStackTrace();
				throw e;
			}
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(bg_frame, e,
					"Error accessing temp file!", JOptionPane.ERROR_MESSAGE);
		}
		shapes = new String[] { "1", "2", "3" };
		final String[] patternExamples = { "dd MMMMM yyyy", "dd.MM.yy",
				"MM/dd/yy", "yyyy.MM.dd G 'at' hh:mm:ss z", "EEE, MMM d, ''yy",
				"h:mm a", "H:mm:ss:SSS", "K:mm a,z",
				"yyyy.MMMMM.dd GGG hh:mm aaa" };
		shapesComboBox = new JComboBox(patternExamples);
		shapesComboBox.setEditable(false);
	}

	/**
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @see WindowListener#windowClosed(WindowEvent)
	 */
	public void windowClosed(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @see WindowListener#windowClosing(WindowEvent)
	 */
	public void windowClosing(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @see WindowListener#windowDeactivated(WindowEvent )
	 */
	public void windowDeactivated(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @see WindowListener#windowDeiconified(WindowEvent )
	 */
	public void windowDeiconified(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @see WindowListener#windowIconified(WindowEvent)
	 */
	public void windowIconified(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @see WindowListener#windowOpened(WindowEvent)
	 */
	public void windowOpened(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * OK button clicked, start new conference
	 * 
	 * @param evt
	 */
	private void btOKActionPerformed(final ActionEvent evt) {
		messageLabel.setForeground(Color.RED);
		messageLabel.setText("Wait... Checking nick and channel availability");
		if (GetNick().length() == 0) {
			messageLabel.setForeground(Color.RED);
			messageLabel.setText("Nick cannot be empty");
			return;
		}

		if (listChannelList.getSelectedValue() == null
				&& !GetCreateNewChannel()) {
			messageLabel.setForeground(Color.RED);
			messageLabel
					.setText("You must select a channel to join or create a new one");
			return;
		}

		boolean NickAvail = true, ChanAvail = true;

		NickAvail = Manager.GetInstance().TrySetNick(GetNick());
		if (GetCreateNewChannel()) {
			ChanAvail = Manager.GetInstance()
					.IsChannelFree(GetNewChannelName());
		}

		if (!NickAvail) {
			messageLabel.setForeground(Color.RED);
			messageLabel.setText("Nick already taken");
			return;
		}

		if (GetCreateNewChannel()) {
			if (ChanAvail) {
				Channel.CreateNew(GetNewChannelName(), GetNewChannelKey());
			} else {
				messageLabel.setForeground(Color.RED);
				messageLabel.setText("Channel already exists");
			}
		} else {
			final String selChan = (String) listChannelList.getSelectedValue();
			try {
				Channel.JoinExisting(selChan, GetSelChannelKey());

				messageLabel.setForeground(Color.BLACK);
				messageLabel.setText("Join requested");
				// System.out.println("Join requested");
				synchronized (Manager.GetInstance().WaitForJoinAck) {
					Manager.GetInstance().WaitForJoinAck
							.wait(Manager.DefaultOperTimeout);
				}
				if (Manager.GetInstance().GetCurrentChannel() == null) {
					messageLabel.setForeground(Color.RED);
					messageLabel
							.setText("Timeout waiting welcome acknowledgement (probably due to a WRONG KEY)");
				}
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
		}
		confPanel.setVisible(false);
		messageLabel.setForeground(Color.BLACK);
		messageLabel.setText("Conference started.");
	}

	/**
	 * 
	 * Open dialog to set a new drawing font.
	 */
	private void changeFontButtonActionPerformed() {
		Font font = ShapeTalk.drawingBoard.getFont();
		font = FontChooserDialog.showDialog(bg_frame, null, font);
		ShapeTalk.drawingBoard.setFont(font);
		string2draw.setFont(font);
	}

	/**
	 * Set filled status of drawingBoard.
	 * 
	 * @see ShapeTalk.DrawingBoard#fill
	 * 
	 * @param e
	 */
	private void checkBoxFillStateChanged(final ItemEvent e) {
		ShapeTalk.drawingBoard.setFill(checkBoxFill.isSelected());
	}

	/**
	 * Clear all shapes
	 * 
	 * @param evt
	 */
	private void clearButtonActionPerformed(final ActionEvent evt) {
		ShapeTalk.drawingBoard.clearBoard();
	}

	/**
	 * Create a new drawingboard
	 */
	private void create_drawingboard() {

		ShapeTalk.drawingBoard = new DrawingBoard();
		ShapeTalk.drawingBoard.setSize(new Dimension(1600, 1200));
		ShapeTalk.drawingBoard.setDoubleBuffered(true);
		ShapeTalk.drawingBoard.addMouseListener(this);
		ShapeTalk.drawingBoard.addMouseMotionListener(this);

		centerPane = new JScrollPane();
		centerPane.setToolTipText("");
		centerPane.setDoubleBuffered(true);
		centerPane.setAutoscrolls(true);
		centerPane.setViewportBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));
		centerPane.setViewportView(ShapeTalk.drawingBoard);

		bg_frame.getContentPane().add(centerPane, BorderLayout.CENTER);

	}

	/**
	 * create a conference panel
	 */
	private void createConfPane() {

		confPanel = new JPanel();
		confPanel.setMaximumSize(new Dimension(500, 900));
		confPanel.setMinimumSize(new Dimension(0, 400));
		confPanel.setPreferredSize(new Dimension(250, 600));
		confPanel.setVisible(false);
		bg_frame.getContentPane().add(confPanel, BorderLayout.EAST);
		confPanel.setBorder(new TitledBorder(null, "Conference Panel",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, null));
	}

	/**
	 * Initialize the contents of the application frame
	 */
	private void createContents() {

		/**
		 * create background frame
		 */
		bg_frame = new JFrame();
		bg_frame.setTitle("Shape Talk");
		bg_frame.getContentPane().setFocusCycleRoot(true);
		bg_frame.getContentPane().setLayout(new BorderLayout());
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		margin = 30;
		bg_frame.setBounds((int) (margin / 2), (int) (margin / 2),
				(int) (screen.getWidth() - margin), (int) (screen.getHeight()
						- margin - 20));
		bg_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createMenu();
		createStatusbar();
		createConfPane();
		createToolPane();

		create_drawingboard();
	}

	/**
	 * create main menu
	 */
	private void createMenu() {
		mainMenu = new JMenuBar();
		bg_frame.setJMenuBar(mainMenu);
		{
			// File
			fileMenu = new JMenu();
			fileMenu.setActionCommand("File");
			fileMenu.setText("File");
			mainMenu.add(fileMenu);
			{
				openImageMenuItem = new JMenuItem();
				openImageMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						loadImageMenuItemActionPerformed(e);
					}
				});
				openImageMenuItem.setText("Open Image");
				fileMenu.add(openImageMenuItem);
				saveImageMenuItem = new JMenuItem();
				saveImageMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						saveImageMenuItemActionPerformed(e);
					}
				});
				saveImageMenuItem.setText("Save Image");
				fileMenu.add(saveImageMenuItem);

				saveAsPNGMenuItem = new JMenuItem();
				saveAsPNGMenuItem.setText("SaveAsPNG");
				saveAsPNGMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						saveAsPNGMenuItemActionPerformed(e);
					}
				});
				fileMenu.add(saveAsPNGMenuItem);

				fileMenu.addSeparator();
				quitMenuItem = new JMenuItem();
				quitMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						bg_frame.dispose();
					}
				});
				quitMenuItem.setText("Quit");
				fileMenu.add(quitMenuItem);
			}
			// Edit
			editMenu = new JMenu();
			editMenu.setActionCommand("Edit");
			editMenu.setText("Menu");
			mainMenu.add(editMenu);
			{
				undoMenuItem = new JMenuItem();
				undoMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						undoMenuItemActionPerformed();
					}
				});
				undoMenuItem.setText("Undo");
				editMenu.add(undoMenuItem);
				redoMenuItem = new JMenuItem();
				redoMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						redoMenuItemActionPerformed();
					}
				});
				redoMenuItem.setText("Redo");
				editMenu.add(redoMenuItem);
			}
			// Conference
			conferenceMenu = new JMenu();
			conferenceMenu.setText("Conference");
			mainMenu.add(conferenceMenu);
			{
				startConfMenuItem = new JMenuItem();
				startConfMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						// startConfMenuItem.setEnabled(false);
						startConf();
					}
				});
				startConfMenuItem.setText("Start Conference");
				conferenceMenu.add(startConfMenuItem);
				// Stop Conference
			}
			helpMenuItem = new JMenu();
			helpMenuItem.setText("Help");
			mainMenu.add(helpMenuItem);
			{
				viewDocMenuItem = new JMenuItem();
				viewDocMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						new WebBrowser();
						return;
					}
				});
				viewDocMenuItem.setText("View Online Doc");
				helpMenuItem.add(viewDocMenuItem);
			}
		}
	}

	/**
	 * Create status bar. Contains tool description and shows mouse cursor
	 * position.
	 */
	private void createStatusbar() {
		statusBar = new JPanel();
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		statusBar.setLayout(flowLayout);
		statusBar.setName("");
		statusBar.setPreferredSize(new Dimension(0, 24));
		bg_frame.getContentPane().add(statusBar, BorderLayout.SOUTH);

		messageLabel = new JLabel();
		messageLabel.setForeground(Color.BLACK);
		messageLabel.setText("");
		statusBar.add(messageLabel);

		mousePositionLabel = new JLabel();
		mousePositionLabel.setName("");
		mousePositionLabel.setPreferredSize(new Dimension(150, 17));
		mousePositionLabel.setText("");
		statusBar.add(mousePositionLabel);
	}

	/**
	 * Create tool panel
	 */
	private void createToolPane() {
		toolsGroup = new ButtonGroup();

		ctrlPanel = new JPanel();
		bg_frame.getContentPane().add(ctrlPanel, BorderLayout.WEST);
		toolSettingsPane = new JPanel();

		colorPanel = new JPanel();
		colorPanel.setLayout(new BorderLayout());
		strokeColorButton = new JButton();
		strokeColorButton.setName("fgButton");
		fillColorButton = new JButton();
		fillColorButton.setName("bgButton");

		// eraserSlider = new JSlider(0, 100, 0);

		strokeSettingsPanel = new JPanel();
		strokeSettingsPane = new JPanel();
		// weightCombo = new JComboBox();
		weightSlider = new JSlider(1, 30, 2);
		toolsPanel = new JPanel();
		toolButtonsPane = new JPanel();
		toolsPanel.setBorder(new TitledBorder("Tools"));
		toolButtonsPane.setLayout(new GridLayout(5, 2, 5, 5));

		seleButton = new JToggleButton();
		seleButton.setEnabled(false);
		toolsGroup.add(seleButton);
		seleButton.setFont(new Font("Dialog", 0, 10));
		seleButton.setSelected(true);
		seleButton.setText("Select");
		seleButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				messageLabel.setForeground(Color.BLACK);
				messageLabel.setText("Select shapes and do transformations.");
				seleButtonActionPerformed(evt);
			}
		});
		toolButtonsPane.add(seleButton);

		lineButton = new JToggleButton();
		toolsGroup.add(lineButton);
		lineButton.setFont(new Font("Dialog", 0, 10));
		lineButton.setSelected(true);
		lineButton.setText("Line");
		lineButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				messageLabel.setForeground(Color.BLACK);
				messageLabel.setText("Drag to draw lines.");
				lineButtonActionPerformed(evt);
			}
		});
		toolButtonsPane.add(lineButton);

		rectButton = new JToggleButton();
		toolsGroup.add(rectButton);
		rectButton.setFont(new Font("Dialog", 0, 10));
		rectButton.setText("Rect");
		rectButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				messageLabel.setForeground(Color.BLACK);
				messageLabel.setText("Drag to draw rectangles.");
				rectButtonActionPerformed(evt);
			}
		});
		toolButtonsPane.add(rectButton);

		roundrectButton = new JToggleButton();
		toolsGroup.add(roundrectButton);
		roundrectButton.setFont(new Font("Dialog", 0, 10));
		roundrectButton.setText("Round Rect");
		roundrectButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				messageLabel.setForeground(Color.BLACK);
				messageLabel.setText("Drag to draw round rectangles.");
				roundrectButtonActionPerformed(evt);
			}
		});
		toolButtonsPane.add(roundrectButton);

		ovalButton = new JToggleButton();
		toolsGroup.add(ovalButton);
		ovalButton.setFont(new Font("Dialog", 0, 10));
		ovalButton.setText("Oval");
		ovalButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				messageLabel.setForeground(Color.BLACK);
				messageLabel.setText("Drag to draw ovals.");
				ovalButtonActionPerformed(evt);
			}
		});

		toolButtonsPane.add(ovalButton);
		diamondButton = new JToggleButton();
		toolsGroup.add(diamondButton);
		diamondButton.setFont(new Font("Dialog", 0, 10));
		diamondButton.setText("Diamond");
		diamondButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				messageLabel.setForeground(Color.BLACK);
				messageLabel.setText("Drag to draw diamonds.");
				diamondButtonActionPerformed(evt);
			}
		});

		toolButtonsPane.add(diamondButton);
		pencilButton = new JToggleButton();
		toolsGroup.add(pencilButton);
		pencilButton.setFont(new Font("Dialog", 0, 10));
		pencilButton.setText("Pencil");
		pencilButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				messageLabel.setForeground(Color.BLACK);
				messageLabel.setText("Drag to draw freely.");
				pencilButtonActionPerformed(evt);
			}
		});

		toolButtonsPane.add(pencilButton);
		eraserButton = new JToggleButton();
		toolsGroup.add(eraserButton);
		eraserButton.setFont(new Font("Dialog", 0, 10));
		eraserButton.setText("Eraser");
		eraserButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				messageLabel.setForeground(Color.BLACK);
				messageLabel.setText("Drag to erase.");
				eraserButtonActionPerformed(evt);
			}
		});
		toolButtonsPane.add(eraserButton);
		textButton = new JToggleButton();
		toolsGroup.add(textButton);
		textButton.setFont(new Font("Dialog", 0, 10));
		textButton.setText("Text");
		textButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				messageLabel.setForeground(Color.BLACK);
				messageLabel.setText("Click to draw text.");
				textButtonActionPerformed(evt);
			}
		});

		toolButtonsPane.add(textButton);
		clearButton = new JButton();
		clearButton.setFont(new Font("Dialog", 1, 10));
		clearButton.setText("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				clearButtonActionPerformed(evt);
			}
		});

		toolButtonsPane.add(clearButton);
		toolsPanel.add(toolButtonsPane);
		toolSettingsPane.add(toolsPanel);

		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowHeights = new int[] { 0, 7 };
		ctrlPanel.setLayout(gridBagLayout);

		ctrlPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
		toolSettingsPane.setLayout(new BoxLayout(toolSettingsPane,
				BoxLayout.Y_AXIS));
		{
			messageLabel.setForeground(Color.BLACK);
			messageLabel.setText("Select shapes");
			messageLabel.setForeground(Color.BLACK);
			messageLabel.setText("Drag to draw lines.");
		}
		{
			colorPanel.setBorder(new TitledBorder("Color Settings"));
			colorPanelLeft = new JPanel();
			colorPanelLeft.setLayout(new BorderLayout());
			colorPanel.add("West", colorPanelLeft);
			colorPane = new ColorPane(35);
			colorPane.setLayout(null);
			colorPane.setName("colorPane");
			colorPanelLeft.add("North", colorPane);

			checkBoxFill = new JCheckBox();
			checkBoxFill.addItemListener(new ItemListener() {
				public void itemStateChanged(final ItemEvent e) {
					checkBoxFillStateChanged(e);
				}
			});
			checkBoxFill.setText("Fill");
			colorPanelLeft.add("South", checkBoxFill);

			colorPanelRight = new JPanel();
			colorPanelRight.setLayout(new BorderLayout());
			colorPanel.add("East", colorPanelRight);
			strokeColorButton.setText("Set Stroke Color");
			strokeColorButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent evt) {
					strokeColorButtonActionPerformed(evt);
				}
			});
			colorPanelRight.add(strokeColorButton);

			fillColorButton.setText("Set Fill Color");
			fillColorButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent evt) {
					fillColorButtonActionPerformed(evt);
				}
			});
			colorPanelRight.add(fillColorButton, BorderLayout.SOUTH);

			toolSettingsPane.add(colorPanel);
		}
		{
			strokeSettingsPanel.setBorder(new TitledBorder("Stroke Setttings"));
			strokeSettingsPane.setLayout(new BorderLayout(0, 3));

			weightSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(final ChangeEvent e) {
					weightSliderStateChanged(e);
				}
			});
			weightSlider.setBorder(new TitledBorder(null, "Stroke Size",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			strokeSettingsPane.add(weightSlider, BorderLayout.NORTH);

			strokeSettingsPanel.add(strokeSettingsPane);

			toolSettingsPane.add(strokeSettingsPanel);
		}
		{
			shapesPane = new JPanel();
			toolSettingsPane.add(shapesPane);
			shapesPane.setLayout(new BorderLayout());
			shapesPane.setBorder(new TitledBorder(null, "Shapes",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));

			shapesComboBox = new JComboBox();
			shapesPane.add(shapesComboBox, BorderLayout.NORTH);

			shapesEditPane = new JPanel();
			shapesEditPane.setLayout(new GridLayout(2, 0));
			shapesPane.add(shapesEditPane, BorderLayout.SOUTH);

			deleteShapeButton = new JButton();
			deleteShapeButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					int index = shapesComboBox.getSelectedIndex();
					index = 0;
					ShapeTalk.drawingBoard.shapes.remove(index);
					ShapeTalk.drawingBoard.repaint();
				}
			});
			deleteShapeButton.setText("Delete");
			shapesEditPane.add(deleteShapeButton);

			scaleButton = new JButton();
			scaleButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
				}
			});
			scaleButton.setText("Scale");
			shapesEditPane.add(scaleButton);

			rotateButton = new JButton();
			rotateButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
				}
			});
			rotateButton.setText("Rotate");
			shapesEditPane.add(rotateButton);

			shearButton = new JButton();
			shearButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
				}
			});
			shearButton.setText("Shear");
			shapesEditPane.add(shearButton);
		}
		{
			fontSettingsPane = new JPanel();
			fontSettingsPane.setLayout(new BorderLayout());
			fontSettingsPane.setBorder(new TitledBorder("Font Settings"));

			toolSettingsPane.add(fontSettingsPane);

			changeFont = new JButton();
			changeFont.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					changeFontButtonActionPerformed();
				}
			});

			changeFont.setText("Change Font");
			fontSettingsPane.add(changeFont, BorderLayout.NORTH);

			string2draw = new JTextField("set your own string");
			string2draw.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					string2drawChanged(e);
				}
			});
			fontSettingsPane.add(string2draw, BorderLayout.SOUTH);
		}
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		ctrlPanel.add(toolSettingsPane, gridBagConstraints);
	}

	/**
	 * Set tool to DIAMOND.
	 * 
	 * @param evt
	 */
	private void diamondButtonActionPerformed(final ActionEvent evt) {
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_DIAMOND);
	}

	/**
	 * Set tool to ERASER
	 * 
	 * @param evt
	 */
	private void eraserButtonActionPerformed(final ActionEvent evt) {
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_ERASER);
	}

	/**
	 * 
	 * Show JColorChooser dialog to change fillColor.
	 * 
	 * @param evt
	 */
	private void fillColorButtonActionPerformed(final ActionEvent evt) {
		final Color color = JColorChooser.showDialog(bg_frame,
				"Change Fill Color", colorPane.getFillColor());
		if (color != null) {
			colorPane.setFillColor(color);
			ShapeTalk.drawingBoard.setFillColor(color);
		}
	}

	/**
	 * @return if(name of the new channel to be created) > 0.
	 */
	private boolean GetCreateNewChannel() {
		return textFieldNewChannelName.getText().length() > 0;
	}

	/**
	 * @return key of the new channel to be created.
	 */
	private String GetNewChannelKey() {

		return textFieldInputKey.getText();
	}

	/**
	 * @return name of the new channel to be created.
	 */
	private String GetNewChannelName() {
		return textFieldNewChannelName.getText();
	}

	/**
	 * @return nickname the user inputs
	 */
	private String GetNick() {
		return textFieldNickname.getText();
	}

	/**
	 * @return new channel key the user inputs
	 */
	private String GetSelChannelKey() {
		return textFieldSetKey.getText();
	}

	/**
	 * Initialize conference panel components
	 */
	private void initConfPaneComponents() {
		textFieldNickname = new JTextField();
		textFieldNewChannelName = new JTextField();
		textFieldSetKey = new JTextField();
		textFieldInputKey = new JTextField();
		labelNickName = new JLabel();
		labelJoinChannel = new JLabel();
		scrollPaneChannelList = new JScrollPane();
		listChannelList = new JList();
		labelCreateChannel = new JLabel();
		labelName = new JLabel();
		labelInputKey = new JLabel();
		buttonOK = new JButton();
		labelUpdateList = new JButton();
		labelSetKey = new JLabel();

		confPanel.setLayout(null);

		confPanel.add(textFieldNickname);
		textFieldNickname.setBounds(100, 20, 120, 20);

		labelNickName.setFont(new Font("Arial", 0, 12));
		labelNickName.setText("Nickname:");
		confPanel.add(labelNickName);
		labelNickName.setBounds(10, 20, 90, 20);

		labelJoinChannel.setFont(new Font("Tahoma", 1, 11));
		labelJoinChannel.setText("Join a channel...");
		confPanel.add(labelJoinChannel);
		labelJoinChannel.setBounds(10, 60, 89, 14);

		scrollPaneChannelList.setViewportView(listChannelList);

		confPanel.add(scrollPaneChannelList);
		scrollPaneChannelList.setBounds(20, 80, 220, 110);

		labelCreateChannel.setFont(new Font("Tahoma", 1, 11));
		labelCreateChannel.setText("... Or create a new channel");
		confPanel.add(labelCreateChannel);
		labelCreateChannel.setBounds(10, 230, 200, 14);

		labelName.setFont(new Font("Arial", 0, 12));
		labelName.setText("Name:");
		confPanel.add(labelName);
		labelName.setBounds(10, 250, 60, 20);

		textFieldNewChannelName.addKeyListener(new KeyAdapter() {
			public void keyTyped(final KeyEvent evt) {
				tbNewChanKeyTyped(evt);
			}
		});

		confPanel.add(textFieldNewChannelName);
		textFieldNewChannelName.setBounds(80, 250, 70, 20);

		labelInputKey.setFont(new Font("Arial", 0, 12));
		labelInputKey.setText("Key:");
		confPanel.add(labelInputKey);
		labelInputKey.setBounds(172, 250, 40, 20);

		textFieldInputKey.setEnabled(false);
		confPanel.add(textFieldInputKey);
		textFieldInputKey.setBounds(210, 250, 40, 20);

		buttonOK.setText("OK");
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				btOKActionPerformed(evt);
			}
		});

		confPanel.add(buttonOK);
		buttonOK.setBounds(10, 280, 220, 23);

		labelUpdateList.setText("Update List");
		labelUpdateList.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				updateChanListActionPerformed(evt);
			}
		});

		confPanel.add(labelUpdateList);
		labelUpdateList.setBounds(120, 60, 119, 20);

		confPanel.add(textFieldSetKey);
		textFieldSetKey.setBounds(170, 200, 80, 20);

		labelSetKey.setFont(new Font("Arial", 0, 12));
		labelSetKey.setText("Join using key:");
		confPanel.add(labelSetKey);
		labelSetKey.setBounds(50, 200, 100, 20);

	}

	/**
	 * 
	 * Set tool to OVAL
	 * 
	 * @param evt
	 */
	private void ovalButtonActionPerformed(final ActionEvent evt) {
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_OVAL);
	}

	/**
	 * Set tool to PENCIL
	 * 
	 * @param evt
	 */
	private void pencilButtonActionPerformed(final ActionEvent evt) {
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_PENCIL);
	}

	/**
	 * Set tool to RECTANGLE
	 * 
	 * @param evt
	 */
	private void rectButtonActionPerformed(final ActionEvent evt) {
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_RECT);
	}

	/**
	 * Redo last move.
	 */
	private void redoMenuItemActionPerformed() {
		if (ShapeTalk.drawingBoard.redos.size() > 0) {
			ShapeTalk.drawingBoard.shapes.add(ShapeTalk.drawingBoard.shapes
					.get(ShapeTalk.drawingBoard.redos.size() - 1));
			ShapeTalk.drawingBoard.redos.remove(ShapeTalk.drawingBoard.redos
					.size() - 1);
			ShapeTalk.drawingBoard.repaint();
		}
	}

	/**
	 * Set tool to ROUNDRECTANGLE
	 * 
	 * @param evt
	 */
	private void roundrectButtonActionPerformed(final ActionEvent evt) {
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_ROUNDRECT);
	}

	/**
	 * Save current image as a PNG file to disk.
	 * 
	 * @param evt
	 */
	private void saveAsPNGMenuItemActionPerformed(final ActionEvent evt) {
		try {
			if (filechooser2 == null) {
				filechooser2 = new JFileChooser();
				filechooser2.setFileFilter(pngFilter);
				filechooser2.setMultiSelectionEnabled(false);
				filechooser2.setAcceptAllFileFilterUsed(false);
			}
			final int retVal = filechooser2.showSaveDialog(bg_frame);
			if (retVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File file = filechooser2.getSelectedFile();
			if (!file.getName().toLowerCase().endsWith(".png")) {
				file = new File(file.getAbsolutePath() + ".png");
			}
			final Dimension d = ShapeTalk.drawingBoard.getSize();
			final BufferedImage image = new BufferedImage(d.width, d.height,
					BufferedImage.TYPE_INT_ARGB);
			final Graphics2D g = image.createGraphics();
			ShapeTalk.drawingBoard.paint(g);
			g.dispose();
			ImageIO.write(image, "png", file);
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(bg_frame, e, "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Save image as ShapeTalk file format.
	 * 
	 * @param evt
	 */
	private void saveImageMenuItemActionPerformed(final ActionEvent evt) {
		try {
			if (filechooser1 == null) {
				filechooser1 = new JFileChooser();
				filechooser1.setFileFilter(shapeTalkDrawFilter);
				filechooser1.setMultiSelectionEnabled(false);
				filechooser1.setAcceptAllFileFilterUsed(false);
			}
			final int retVal = filechooser1.showSaveDialog(bg_frame);
			if (retVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File file = filechooser1.getSelectedFile();
			if (!file.getName().toLowerCase().endsWith(".jdw")) {
				file = new File(file.getAbsolutePath() + ".jdw");
			}
			final Color fgcolor = ShapeTalk.drawingBoard.getForeground();
			final Color bgcolor = ShapeTalk.drawingBoard.getBackground();
			final PrintWriter pw = new PrintWriter(new FileWriter(file));
			pw.println(fgcolor.getRGB());
			pw.println(bgcolor.getRGB());
			// pw.println(weightCombo.getSelectedIndex());
			pw.println(weightSlider.getValue());
			pw.print(weightSlider.getValue());
			// pw.println(eraserSlider.getValue());
			pw.print(ShapeTalk.drawingBoard.getShapes());
			pw.close();
		} catch (final java.io.IOException ioe) {
			JOptionPane.showMessageDialog(bg_frame, ioe, "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Set tool to SELETION
	 * 
	 * @param evt
	 */
	private void seleButtonActionPerformed(final ActionEvent evt) {
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_SELE);
	}

	/**
	 * Initialize new conference.
	 */
	private void startConf() {
		Manager.GetInstance();

		confPanel.setVisible(true);
		initConfPaneComponents();
	}

	/**
	 * Change the string to draw.
	 * 
	 * @param e
	 */
	private void string2drawChanged(final ActionEvent e) {
		ShapeTalk.drawingBoard.setString(string2draw.getText());
	}

	/**
	 * Show ColorChooser dialog to set stroke color.
	 * 
	 * @param evt
	 */
	private void strokeColorButtonActionPerformed(final ActionEvent evt) {
		final Color color = JColorChooser.showDialog(bg_frame,
				"Change Stroke Color", colorPane.getStrokeColor());
		if (color != null) {
			ShapeTalk.drawingBoard.setStrokeColor(color);
			colorPane.setStrokeColor(color);
		}
	}

	/**
	 * Get the new channel name.
	 * 
	 * @param evt
	 */
	private void tbNewChanKeyTyped(final KeyEvent evt) {
		textFieldInputKey
				.setEnabled(textFieldNewChannelName.getText().length() > 0);
	}

	/**
	 * Set tool to TEXT.
	 * 
	 * @param evt
	 */
	private void textButtonActionPerformed(final ActionEvent evt) {
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_TEXT);
	}

	/**
	 * Undo last move.
	 */
	private void undoMenuItemActionPerformed() {
		if (ShapeTalk.drawingBoard.shapes.size() > 0) {
			ShapeTalk.drawingBoard.redos.add(ShapeTalk.drawingBoard.shapes
					.get(ShapeTalk.drawingBoard.shapes.size() - 1));
			ShapeTalk.drawingBoard.shapes.remove(ShapeTalk.drawingBoard.shapes
					.size() - 1);
			ShapeTalk.drawingBoard.repaint();
		}
	}

	/**
	 * Update channel list.
	 */
	private void UpdateChanList() {
		listChannelList.setModel(new AbstractListModel() {
			public Object getElementAt(final int i) {
				return strings[i];
			}

			public int getSize() {
				return strings.length;
			}

			String[] strings = Manager.GetInstance().GetAvailableChannels();
		});

	}

	/**
	 * Set new stroke index.
	 * 
	 * @see ShapeTalk.DrawingBoard#strokeIndex
	 * 
	 * @param e
	 */
	private void weightSliderStateChanged(final ChangeEvent e) {
		ShapeTalk.drawingBoard.setStrokeIndex(weightSlider.getValue());
	}

	/**
	 * FileFilter to check if the input file has a extension of "png".
	 */
	public FileFilter pngFilter = new FileFilter() {
		@Override
		public boolean accept(final java.io.File f) {
			if (f.isDirectory()) {
				return true;
			}
			if (f.getName().endsWith(".png")) {
				return true;
			}
			return false;
		}

		@Override
		public String getDescription() {
			return "PNG image file (*.png)";
		}
	};

	/**
	 * FileFilter to check if the input file is the ShapeTalk format.
	 */
	public FileFilter shapeTalkDrawFilter = new FileFilter() {
		@Override
		public boolean accept(final java.io.File f) {
			if (f.isDirectory()) {
				return true;
			}
			if (f.getName().endsWith(".jdw")) {
				return true;
			}
			return false;
		}

		@Override
		public String getDescription() {
			return "shapeTalkDraw Model File (*.jdw)";
		}
	};

	/**
	 * Background frame. Top level component of the application.
	 */
	private JFrame bg_frame;
	/**
	 * OK button of the start conference panel.
	 */
	private JButton buttonOK;
	/**
	 * Container of the drawingBoard.
	 */
	private JScrollPane centerPane;
	/**
	 * "Change Font" button.
	 */
	private JButton changeFont;
	/**
	 * CheckBox to let the user define if the shapes are to filled.
	 */
	private JCheckBox checkBoxFill;
	/**
	 * "Clear all shapes" button.
	 */
	private JButton clearButton;
	/**
	 * ColorChooser instance.
	 */
	private JColorChooser colorChooser;
	/**
	 * ColorChooser Dialog.
	 */
	private JDialog colorChooserDialog;
	/**
	 * Container of color settings related objects.
	 */
	private ColorPane colorPane;
	/**
	 * Panel that hold the colorShow and "change color" buttons.
	 */
	private JPanel colorPanel;
	/**
	 * Left part of the color panel.
	 */
	private JPanel colorPanelLeft;
	/**
	 * Right part of the color panel.
	 */
	private JPanel colorPanelRight;
	/**
	 * "Conference" menu.
	 */
	private JMenu conferenceMenu;
	/**
	 * "Start new conference" panel.
	 */
	private JPanel confPanel;
	/**
	 * Container of the controllers.
	 */
	private JPanel ctrlPanel;
	/**
	 * Index of the current selected shape.
	 * 
	 * @see ShapeTalk.DrawingBoard#shapes
	 */
	private int currentSelectedShapeIndex;
	/**
	 * DELETE button
	 */
	private JButton deleteShapeButton;
	/**
	 * DIAMOND tool button.
	 */
	private JToggleButton diamondButton;
	/**
	 * "Edit" menu.
	 */
	private JMenu editMenu;
	/**
	 * ERASER tool button.
	 */
	private JToggleButton eraserButton;
	/**
	 * Filechoosers used when save or open image.
	 */
	private JFileChooser filechooser1, filechooser2;
	/**
	 * "File" menu.
	 */
	private JMenu fileMenu;
	/**
	 * "Change fill color" button.
	 */
	private JButton fillColorButton;
	/**
	 * Font preview label.
	 */
	private JLabel fontPreview;
	/**
	 * Container of font settings components.
	 */
	private JPanel fontSettingsPane;
	/**
	 * "Help" menu item.
	 */
	private JMenu helpMenuItem;
	/**
	 * "Create new channel" label.
	 */
	private JLabel labelCreateChannel;
	/**
	 * "Input key" label.
	 */
	private JLabel labelInputKey;
	/**
	 * "Join channel" label.
	 */
	private JLabel labelJoinChannel;
	/**
	 * "Channel name" label.
	 */
	private JLabel labelName;
	/**
	 * "Input nickname" label.
	 */
	private JLabel labelNickName;
	/**
	 * "Set channel key" label.
	 */
	private JLabel labelSetKey;
	/**
	 * "Update channel list" button.
	 */
	private JButton labelUpdateList;
	/**
	 * LINE tool button.
	 */
	private JToggleButton lineButton;
	/**
	 * Channel list.s
	 */
	private JList listChannelList;
	/**
	 * Main menu bar.
	 */
	private JMenuBar mainMenu;
	/**
	 * margin from the application frame to screen borders.
	 */
	private int margin;
	/**
	 * Application message label.
	 */
	private JLabel messageLabel;
	/**
	 * Mouse cursor position label.
	 */
	private JLabel mousePositionLabel;
	/**
	 * "Open Image" menu item.
	 */
	private JMenuItem openImageMenuItem;
	/**
	 * OVAL tool button.
	 */
	private JToggleButton ovalButton;
	/**
	 * PENCIL tool button.
	 */
	private JToggleButton pencilButton;
	/**
	 * "Quit" menu item.
	 */
	private JMenuItem quitMenuItem;
	/**
	 * RECTANGLE tool button.
	 */
	private JToggleButton rectButton;
	/**
	 * "Redo" menu item.
	 */
	private JMenuItem redoMenuItem;
	/**
	 * ROTATE button.
	 */
	private JButton rotateButton;
	/**
	 * ROUND_RECTANGLE tool button.
	 */
	private JToggleButton roundrectButton;
	/**
	 * "Save as PNG" menu item.
	 */
	private JMenuItem saveAsPNGMenuItem;
	/**
	 * "Save Image" menu item.
	 */
	private JMenuItem saveImageMenuItem;
	/**
	 * SCALE button.
	 */
	private JButton scaleButton;
	/**
	 * Dimension of the screen.
	 */
	private Dimension screen;
	/**
	 * Container of the channel list.
	 */
	private JScrollPane scrollPaneChannelList;
	/**
	 * SELECTION tool button.
	 */
	private JToggleButton seleButton;
	/**
	 * ComboBox which shows all the shapes on the drawingBoard.
	 */
	private JComboBox shapesComboBox;
	/**
	 * Container of the shape editing components.
	 */
	private JPanel shapesEditPane;
	/**
	 * Container of shape selection components.
	 */
	private JPanel shapesPane;

	/**
	 * SHEAR button.
	 */
	private JButton shearButton;

	/**
	 * "Start conference" menu item.
	 */
	private JMenuItem startConfMenuItem;

	/**
	 * Status bar.
	 */
	private JPanel statusBar;

	/**
	 * Text field to set the string that the TEXT tool draws.
	 */
	private JTextField string2draw;

	/**
	 * "Change stroke color" button.
	 */
	private JButton strokeColorButton;

	/**
	 * Container of the stroke settings component.
	 */
	private JPanel strokeSettingsPane;

	/**
	 * Container of the stroke slider.
	 */
	private JPanel strokeSettingsPanel;

	/**
	 * Temporary file that stores current shapes.
	 * 
	 * May be useful to "Crash Recovery" function.
	 */
	private File tempFile;

	/**
	 * TEXT tool button.
	 */
	private JToggleButton textButton;

	/**
	 * Text field to input join channel key.
	 */
	private JTextField textFieldInputKey;

	/**
	 * Text field to input new channel name.
	 */
	private JTextField textFieldNewChannelName;

	/**
	 * Text field to input nickname.
	 */
	private JTextField textFieldNickname;

	/**
	 * Text field to input new channel key.
	 */
	private JTextField textFieldSetKey;

	/**
	 * Container of the tool buttons.
	 */
	private JPanel toolButtonsPane;

	/**
	 * Container of the tool settings components.
	 */
	private JPanel toolSettingsPane;

	/**
	 * Button group of the toggle tool buttons.
	 */
	private ButtonGroup toolsGroup;

	/**
	 * Container of all the tool-related components.
	 */
	private JPanel toolsPanel;

	/**
	 * "Undo" menu item.
	 */
	private JMenuItem undoMenuItem;

	/**
	 * "View Doc" menu item.
	 */
	private JMenuItem viewDocMenuItem;

	/**
	 * Slider to set stroke index.
	 */
	private JSlider weightSlider;
}
