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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
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
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import ShapeTalk.Chat.Channel;
import ShapeTalk.Chat.Manager;
import ShapeTalk.DrawingBoard.DrawingBoard;

public class ShapeTalk implements WindowListener, ActionListener,
		MouseListener, MouseMotionListener, ItemListener {

	public static boolean showPosition = false;

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		// if (ae.getActionCommand().equals("OK")) {
		// if (colorToChange == changeStrokeColor) {
		// strokeColorChooser.setForeground(colorChooser.getColor());
		// strokeColor = colorChooser.getColor();
		// colorToChange = -1;
		// } else if (colorToChange == changeFillColor) {
		// fillColorChooser.setBackground(colorChooser.getColor());
		// fillColor = colorChooser.getColor();
		// colorToChange = -1;
		// }
		// }
		// if (ae.getActionCommand().equals("Cancel"))
		// colorChooserDialog.dispose();
	}

	public void bgButtonActionPerformed(ActionEvent evt) {// GEN-
		// FIRST
		// :
		// event_bgButtonActionPerformed
		final Color color = JColorChooser.showDialog(bg_frame,
				"Change Board Background Color", drawingBoard.getBackground());
		if (color != null) {
			drawingBoard.setBackground(color);
			bgButton.setBackground(color);
		}
	}// GEN-LAST:event_bgButtonActionPerformed

	public void btOKActionPerformed(ActionEvent evt) {// GEN-FIRST
		// :
		// event_btOKActionPerformed
		currentToolLabel.setForeground(Color.RED);
		currentToolLabel
				.setText("Wait... Checking nick and channel availability");
		if (GetNick().length() == 0) {
			currentToolLabel.setForeground(Color.RED);
			currentToolLabel.setText("Nick cannot be empty");
			return;
		}

		if (ltChans.getSelectedValue() == null && !GetCreateNewChannel()) {
			currentToolLabel.setForeground(Color.RED);
			currentToolLabel
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
			currentToolLabel.setForeground(Color.RED);
			currentToolLabel.setText("Nick already taken");
			return;
		}

		if (GetCreateNewChannel()) {
			if (ChanAvail) {
				Channel.CreateNew(GetNewChannelName(), GetNewChannelKey());
			} else {
				currentToolLabel.setForeground(Color.RED);
				currentToolLabel.setText("Channel already exists");
			}
		} else {
			final String selChan = (String) ltChans.getSelectedValue();
			try {
				Channel.JoinExisting(selChan, GetSelChannelKey());

				currentToolLabel.setForeground(Color.BLACK);
				currentToolLabel.setText("Join requested");
				// System.out.println("Join requested");
				synchronized (Manager.GetInstance().WaitForJoinAck) {
					Manager.GetInstance().WaitForJoinAck
							.wait(Manager.DefaultOperTimeout);
				}
				if (Manager.GetInstance().GetCurrentChannel() == null) {
					currentToolLabel.setForeground(Color.RED);
					currentToolLabel
							.setText("Timeout waiting welcome acknowledgement (probably due to a WRONG KEY)");
				}
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
		}
		confPanel.setVisible(false);
		currentToolLabel.setForeground(Color.BLACK);
		currentToolLabel.setText("Conference started.");
	}// GEN-LAST:event_btOKActionPerformed

	public void clearButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_clearButtonActionPerformed
		drawingBoard.clearBoard();
	}// GEN-LAST:event_clearButtonActionPerformed

	/**
	 * Initialize the contents of the frame
	 */
	public void createContents() {

		/**
		 * create background frame
		 */
		bg_frame = new JFrame();
		bg_frame.setTitle("Shape Talk");
		bg_frame.getContentPane().setFocusCycleRoot(true);
		bg_frame.getContentPane().setLayout(new BorderLayout());
		bg_frame.setBounds(100, 100, 1043, 610);
		bg_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/**
		 * create main menu
		 */
		mainMenu = new JMenuBar();
		bg_frame.setJMenuBar(mainMenu);
		{
			// File
			fileMenu = new JMenu();
			fileMenu.setActionCommand("File");
			fileMenu.setText("File");
			mainMenu.add(fileMenu);
			{
				// Open Image
				final JMenuItem openImageMenuItem = new JMenuItem();
				openImageMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						loadImageMenuItemActionPerformed(e);
					}
				});
				openImageMenuItem.setText("Open Image");
				fileMenu.add(openImageMenuItem);
				// Save Image
				final JMenuItem saveImageMenuItem = new JMenuItem();
				saveImageMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						saveImageMenuItemActionPerformed(e);
					}
				});
				saveImageMenuItem.setText("Save Image");
				fileMenu.add(saveImageMenuItem);

				final JMenuItem saveAsPNGMenuItem = new JMenuItem();
				saveAsPNGMenuItem.setText("SaveAsPNG");
				saveAsPNGMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						saveAsPNGMenuItemActionPerformed(e);
					}
				});
				fileMenu.add(saveAsPNGMenuItem);

				fileMenu.addSeparator();
				// Quit
				final JMenuItem quitMenuItem = new JMenuItem();
				quitMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						bg_frame.dispose();
					}
				});
				quitMenuItem.setText("Quit");
				fileMenu.add(quitMenuItem);
			}
			// Conference
			conferenceMenu = new JMenu();
			conferenceMenu.setText("Conference");
			mainMenu.add(conferenceMenu);
			{
				// Start Conference
				final JMenuItem startConfMenuItem = new JMenuItem();
				startConfMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						startConfMenuItem.setEnabled(false);
						startConf();
					}
				});
				startConfMenuItem.setText("Start Conference");
				conferenceMenu.add(startConfMenuItem);
				// Stop Conference
			}
			// Help
			final JMenu helpMenuItem = new JMenu();
			helpMenuItem.setText("Help");
			mainMenu.add(helpMenuItem);
			{
				// About
				final JMenuItem aboutMenuItem = new JMenuItem();
				aboutMenuItem.setText("About");
				helpMenuItem.add(aboutMenuItem);
			}
		}
		statusBar = new JPanel();
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		statusBar.setLayout(flowLayout);
		statusBar.setName("");
		statusBar.setPreferredSize(new Dimension(0, 24));
		bg_frame.getContentPane().add(statusBar, BorderLayout.SOUTH);

		currentToolLabel = new JLabel();
		currentToolLabel.setForeground(Color.BLACK);
		currentToolLabel.setText("");
		statusBar.add(currentToolLabel);

		mousePositionLabel = new JLabel();
		mousePositionLabel.setName("");
		mousePositionLabel.setPreferredSize(new Dimension(150, 17));
		mousePositionLabel.setText("");
		statusBar.add(mousePositionLabel);

		toolsGroup = new ButtonGroup();

		ctrlPanel = new JPanel();
		bg_frame.getContentPane().add(ctrlPanel, BorderLayout.WEST);
		mediumPanel1 = new JPanel();
		toolsPanel = new JPanel();
		mediumPanel2 = new JPanel();
		lineButton = new JToggleButton();
		rectButton = new JToggleButton();
		ovalButton = new JToggleButton();
		diamondButton = new JToggleButton();
		pencilButton = new JToggleButton();
		eraserButton = new JToggleButton();
		textButton = new JToggleButton();
		clearButton = new JButton();
		colorPanel = new JPanel();
		fgButton = new JButton();
		bgButton = new JButton();
		strokeSettingsPanel = new JPanel();
		mediumPanel3 = new JPanel();
		weightCombo = new JComboBox();
		eraserCombo = new JComboBox();
		dbSettingsPanel = new JPanel();
		mediumPanel4 = new JPanel();

		ctrlPanel.setLayout(new GridBagLayout());

		ctrlPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
		mediumPanel1.setLayout(new BoxLayout(mediumPanel1, BoxLayout.Y_AXIS));

		toolsPanel.setBorder(new TitledBorder("Tools"));
		mediumPanel2.setLayout(new GridLayout(4, 2, 5, 5));

		toolsGroup.add(lineButton);
		lineButton.setFont(new Font("Dialog", 0, 10));
		lineButton.setSelected(true);
		currentToolLabel.setForeground(Color.BLACK);
		currentToolLabel.setText("Drag to draw lines.");
		lineButton.setText("Line");
		lineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				currentToolLabel.setForeground(Color.BLACK);
				currentToolLabel.setText("Drag to draw lines.");
				lineButtonActionPerformed(evt);
			}
		});

		mediumPanel2.add(lineButton);

		toolsGroup.add(rectButton);
		rectButton.setFont(new Font("Dialog", 0, 10));
		rectButton.setText("Rect");
		rectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				currentToolLabel.setForeground(Color.BLACK);
				currentToolLabel.setText("Drag to draw rectangles.");
				rectButtonActionPerformed(evt);
			}
		});

		mediumPanel2.add(rectButton);

		toolsGroup.add(ovalButton);
		ovalButton.setFont(new Font("Dialog", 0, 10));
		ovalButton.setText("Oval");
		ovalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				currentToolLabel.setForeground(Color.BLACK);
				currentToolLabel.setText("Drag to draw ovals.");
				ovalButtonActionPerformed(evt);
			}
		});

		mediumPanel2.add(ovalButton);

		toolsGroup.add(diamondButton);
		diamondButton.setFont(new Font("Dialog", 0, 10));
		diamondButton.setText("Diamond");
		diamondButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				currentToolLabel.setForeground(Color.BLACK);
				currentToolLabel.setText("Drag to draw diamonds.");
				diamondButtonActionPerformed(evt);
			}
		});

		mediumPanel2.add(diamondButton);

		toolsGroup.add(pencilButton);
		pencilButton.setFont(new Font("Dialog", 0, 10));
		pencilButton.setText("Pencil");
		pencilButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				currentToolLabel.setForeground(Color.BLACK);
				currentToolLabel.setText("Drag to draw freely.");
				pencilButtonActionPerformed(evt);
			}
		});

		mediumPanel2.add(pencilButton);

		toolsGroup.add(eraserButton);
		eraserButton.setFont(new Font("Dialog", 0, 10));
		eraserButton.setText("Eraser");
		eraserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				currentToolLabel.setForeground(Color.BLACK);
				currentToolLabel.setText("Drag to erase.");
				eraserButtonActionPerformed(evt);
			}
		});

		mediumPanel2.add(eraserButton);

		toolsGroup.add(textButton);
		textButton.setFont(new Font("Dialog", 0, 10));
		textButton.setText("Text");
		textButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				currentToolLabel.setForeground(Color.BLACK);
				currentToolLabel.setText("Click to draw text.");
				textButtonActionPerformed(evt);
			}
		});

		mediumPanel2.add(textButton);

		clearButton.setFont(new Font("Dialog", 1, 10));
		clearButton.setText("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				clearButtonActionPerformed(evt);
			}
		});

		mediumPanel2.add(clearButton);

		toolsPanel.add(mediumPanel2);

		mediumPanel1.add(toolsPanel);

		colorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

		colorPanel.setBorder(new TitledBorder("Color Settings"));
		fgButton.setToolTipText("Change Drawing Color");
		fgButton.setBorder(new LineBorder(new Color(0, 0, 0)));
		fgButton.setPreferredSize(new Dimension(50, 50));
		fgButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				fgButtonActionPerformed(evt);
			}
		});

		colorPanel.add(fgButton);
		bgButton.setToolTipText("Change Board Background Color");
		bgButton.setBorder(new LineBorder(new Color(0, 0, 0)));
		bgButton.setPreferredSize(new Dimension(50, 50));
		bgButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				bgButtonActionPerformed(evt);
			}
		});

		colorPanel.add(bgButton);

		mediumPanel1.add(colorPanel);

		strokeSettingsPanel.setBorder(new TitledBorder("Stroke Setttings"));
		mediumPanel3.setLayout(new BorderLayout(0, 3));

		weightCombo.setFont(new Font("Dialog", 0, 10));
		weightCombo.setModel(new DefaultComboBoxModel(new String[] {
				"Stroke Weight 1.0px", "Stroke Weight 2.0px",
				"Stroke Weight 5.0px", "Stroke Weight 7.5px",
				"Stroke Weight 10.0px" }));
		weightCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				weightComboActionPerformed(evt);
			}
		});

		mediumPanel3.add(weightCombo, BorderLayout.NORTH);

		eraserCombo.setFont(new Font("Dialog", 0, 10));
		eraserCombo.setModel(new DefaultComboBoxModel(new String[] {
				"Eraser Size 15px", "Eraser Size 20px", "Eraser Size 30px",
				"Eraser Size 50px", "Eraser Size 100px" }));
		eraserCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				eraserComboActionPerformed(evt);
			}
		});

		mediumPanel3.add(eraserCombo, BorderLayout.SOUTH);

		strokeSettingsPanel.add(mediumPanel3);

		mediumPanel1.add(strokeSettingsPanel);

		dbSettingsPanel.setBorder(new TitledBorder("Drawing Board Settings"));
		mediumPanel4.setLayout(new BorderLayout(0, 3));

		dbSettingsPanel.add(mediumPanel4);

		mediumPanel1.add(dbSettingsPanel);

		final JButton changeCanvasSize = new JButton();
		changeCanvasSize.setText("Change canvas size");
		dbSettingsPanel.add(changeCanvasSize);

		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(10, 5, 5, 5);
		ctrlPanel.add(mediumPanel1, gridBagConstraints);

		drawingBoard = new DrawingBoard();
		drawingBoard.setSize(new Dimension(1600, 1200));
		drawingBoard.setDoubleBuffered(true);
		drawingBoard.addMouseListener(this);
		drawingBoard.addMouseMotionListener(this);

		centerPane = new JScrollPane();
		centerPane.setToolTipText("");
		centerPane.setDoubleBuffered(true);
		centerPane.setAutoscrolls(true);
		centerPane.setViewportBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));
		centerPane.setViewportView(drawingBoard);

		bg_frame.getContentPane().add(centerPane, BorderLayout.CENTER);

		fgButton.setBackground(drawingBoard.getForeground());
		bgButton.setBackground(drawingBoard.getBackground());

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

	public void diamondButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_diamondButtonActionPerformed
		drawingBoard.setTool(DrawingBoard.TOOL_DIAMOND);
	}// GEN-LAST:event_diamondButtonActionPerformed

	public void eraserButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_eraserButtonActionPerformed
		drawingBoard.setTool(DrawingBoard.TOOL_ERASER);
	}// GEN-LAST:event_eraserButtonActionPerformed

	public void eraserComboActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_eraserComboActionPerformed
		drawingBoard.setEraserIndex(eraserCombo.getSelectedIndex());
	}// GEN-LAST:event_eraserComboActionPerformed

	public void fgButtonActionPerformed(ActionEvent evt) {// GEN-
		// FIRST
		// :
		// event_fgButtonActionPerformed
		final Color color = JColorChooser.showDialog(bg_frame,
				"Change Drawing Color", drawingBoard.getForeground());
		if (color != null) {
			drawingBoard.setForeground(color);
			fgButton.setBackground(color);
		}
	}// GEN-LAST:event_fgButtonActionPerformed

	public boolean GetCreateNewChannel() {
		return tbNewChan.getText().length() > 0;
	}

	public String GetNewChannelKey() {

		return tbKey.getText();
	}

	public String GetNewChannelName() {
		return tbNewChan.getText();
	}

	public String GetNick() {
		return tbNickname.getText();
	}

	public String GetSelChannelKey() {
		return tbSelKey.getText();
	}

	public void initConfPaneComponents() {
		tbNickname = new JTextField();
		tbNewChan = new JTextField();
		tbKey = new JTextField();
		tbSelKey = new JTextField();
		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jScrollPane1 = new JScrollPane();
		ltChans = new JList();
		jLabel3 = new JLabel();
		jLabel4 = new JLabel();
		jLabel5 = new JLabel();
		btOK = new JButton();
		jButton1 = new JButton();
		jLabel7 = new JLabel();

		confPanel.setLayout(null);

		confPanel.add(tbNickname);
		tbNickname.setBounds(100, 20, 120, 20);

		jLabel1.setFont(new Font("Arial", 0, 12));
		jLabel1.setText("Nickname:");
		confPanel.add(jLabel1);
		jLabel1.setBounds(10, 20, 90, 20);

		jLabel2.setFont(new Font("Tahoma", 1, 11));
		jLabel2.setText("Join a channel...");
		confPanel.add(jLabel2);
		jLabel2.setBounds(10, 60, 89, 14);

		jScrollPane1.setViewportView(ltChans);

		confPanel.add(jScrollPane1);
		jScrollPane1.setBounds(20, 80, 220, 110);

		jLabel3.setFont(new Font("Tahoma", 1, 11));
		jLabel3.setText("... Or create a new channel");
		confPanel.add(jLabel3);
		jLabel3.setBounds(10, 230, 200, 14);

		jLabel4.setFont(new Font("Arial", 0, 12));
		jLabel4.setText("Name:");
		confPanel.add(jLabel4);
		jLabel4.setBounds(10, 250, 60, 20);

		tbNewChan.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent evt) {
				tbNewChanKeyTyped(evt);
			}
		});

		confPanel.add(tbNewChan);
		tbNewChan.setBounds(80, 250, 70, 20);

		jLabel5.setFont(new Font("Arial", 0, 12));
		jLabel5.setText("Key:");
		confPanel.add(jLabel5);
		jLabel5.setBounds(172, 250, 40, 20);

		tbKey.setEnabled(false);
		confPanel.add(tbKey);
		tbKey.setBounds(210, 250, 40, 20);

		btOK.setText("OK");
		btOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btOKActionPerformed(evt);
			}
		});

		confPanel.add(btOK);
		btOK.setBounds(10, 280, 220, 23);

		jButton1.setText("Update List");
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		confPanel.add(jButton1);
		jButton1.setBounds(120, 60, 119, 20);

		confPanel.add(tbSelKey);
		tbSelKey.setBounds(170, 200, 80, 20);

		jLabel7.setFont(new Font("Arial", 0, 12));
		jLabel7.setText("Join using key:");
		confPanel.add(jLabel7);
		jLabel7.setBounds(50, 200, 100, 20);

	}

	public void itemStateChanged(ItemEvent e) {
	}

	public void jButton1ActionPerformed(ActionEvent evt) {// GEN-
		// FIRST
		// :
		// event_jButton1ActionPerformed
		UpdateChanList();
	}// GEN-LAST:event_jButton1ActionPerformed

	public void lineButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_lineButtonActionPerformed
		drawingBoard.setTool(DrawingBoard.TOOL_LINE);
	}// GEN-LAST:event_lineButtonActionPerformed

	public void loadImageMenuItemActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_loadButtonActionPerformed
		try {
			if (filechooser1 == null) {
				filechooser1 = new JFileChooser();
				filechooser1.setFileFilter(shapeTalkDrawFilter);
				filechooser1.setMultiSelectionEnabled(false);
				filechooser1.setAcceptAllFileFilterUsed(false);
			}
			final int retVal = filechooser1.showOpenDialog(bg_frame);
			if (retVal != JFileChooser.APPROVE_OPTION)
				return;
			final BufferedReader br = new BufferedReader(new FileReader(
					filechooser1.getSelectedFile()));
			int rgb = Integer.parseInt(br.readLine());
			final Color fgcolor = new Color(rgb);
			drawingBoard.setForeground(fgcolor);
			fgButton.setBackground(fgcolor);
			rgb = Integer.parseInt(br.readLine());
			final Color bgcolor = new Color(rgb);
			drawingBoard.setBackground(bgcolor);
			bgButton.setBackground(bgcolor);
			weightCombo.setSelectedIndex(Integer.parseInt(br.readLine()));
			eraserCombo.setSelectedIndex(Integer.parseInt(br.readLine()));
			final ArrayList list = new ArrayList();
			String str;
			while ((str = br.readLine()) != null) {
				list.add(str);
			}
			drawingBoard.setShapes(list);
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(bg_frame, e, "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_loadButtonActionPerformed

	// End of variables declaration//GEN-END:variables

	/*
	 * (non-Javadoc)
	 * 
	 * @see MouseListener#mouseClicked(MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MouseMotionListener#mouseMoved(MouseEvent)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see MouseMotionListener#mouseDragged(MouseEvent )
	 */
	public void mouseDragged(MouseEvent e) {
		if (ShapeTalk.showPosition) {
			mousePositionLabel.setText("X=" + e.getX() + " Y=" + e.getY());
		} else {
			mousePositionLabel.setText("X=" + " Y=");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MouseListener#mouseEntered(MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		if (e.getSource().equals(drawingBoard)) {
			ShapeTalk.showPosition = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MouseListener#mouseExited(MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		if (e.getSource().equals(drawingBoard)) {
			ShapeTalk.showPosition = false;
			mousePositionLabel.setText("X=" + " Y=");
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (ShapeTalk.showPosition) {
			mousePositionLabel.setText("X=" + e.getX() + " Y=" + e.getY());
		} else {
			mousePositionLabel.setText("X=" + " Y=");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MouseListener#mousePressed(MouseEvent)
	 */
	public void mousePressed(MouseEvent m) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MouseListener#mouseReleased(MouseEvent)
	 */
	public void mouseReleased(MouseEvent m) {
	}

	public void ovalButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_ovalButtonActionPerformed
		drawingBoard.setTool(DrawingBoard.TOOL_OVAL);
	}// GEN-LAST:event_ovalButtonActionPerformed

	public void pencilButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_pencilButtonActionPerformed
		drawingBoard.setTool(DrawingBoard.TOOL_PENCIL);
	}// GEN-LAST:event_pencilButtonActionPerformed

	public void rectButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_rectButtonActionPerformed
		drawingBoard.setTool(DrawingBoard.TOOL_RECT);
	}// GEN-LAST:event_rectButtonActionPerformed

	public void saveAsPNGMenuItemActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_exportButtonActionPerformed
		try {
			if (filechooser2 == null) {
				filechooser2 = new JFileChooser();
				filechooser2.setFileFilter(pngFilter);
				filechooser2.setMultiSelectionEnabled(false);
				filechooser2.setAcceptAllFileFilterUsed(false);
			}
			final int retVal = filechooser2.showSaveDialog(bg_frame);
			if (retVal != JFileChooser.APPROVE_OPTION)
				return;
			File file = filechooser2.getSelectedFile();
			if (!file.getName().toLowerCase().endsWith(".png")) {
				file = new File(file.getAbsolutePath() + ".png");
			}
			final Dimension d = drawingBoard.getSize();
			final BufferedImage image = new BufferedImage(d.width, d.height,
					BufferedImage.TYPE_INT_ARGB);
			final Graphics2D g = image.createGraphics();
			drawingBoard.paint(g);
			g.dispose();
			ImageIO.write(image, "png", file);
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(bg_frame, e, "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_exportButtonActionPerformed

	public void saveImageMenuItemActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_saveButtonActionPerformed
		try {
			if (filechooser1 == null) {
				filechooser1 = new JFileChooser();
				filechooser1.setFileFilter(shapeTalkDrawFilter);
				filechooser1.setMultiSelectionEnabled(false);
				filechooser1.setAcceptAllFileFilterUsed(false);
			}
			final int retVal = filechooser1.showSaveDialog(bg_frame);
			if (retVal != JFileChooser.APPROVE_OPTION)
				return;
			File file = filechooser1.getSelectedFile();
			if (!file.getName().toLowerCase().endsWith(".jdw")) {
				file = new File(file.getAbsolutePath() + ".jdw");
			}
			final Color fgcolor = drawingBoard.getForeground();
			final Color bgcolor = drawingBoard.getBackground();
			final PrintWriter pw = new PrintWriter(new FileWriter(file));
			pw.println(fgcolor.getRGB());
			pw.println(bgcolor.getRGB());
			pw.println(weightCombo.getSelectedIndex());
			pw.print(eraserCombo.getSelectedIndex());
			pw.print(drawingBoard.getShapes());
			pw.close();
		} catch (final java.io.IOException ioe) {
			JOptionPane.showMessageDialog(bg_frame, ioe, "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_saveButtonActionPerformed

	public void startConf() {
		Manager.GetInstance();

		confPanel.setVisible(true);
		initConfPaneComponents();
	}

	public void tbNewChanKeyTyped(KeyEvent evt) {// GEN-FIRST:
		// event_tbNewChanKeyTyped
		tbKey.setEnabled(tbNewChan.getText().length() > 0);
	}// GEN-LAST:event_tbNewChanKeyTyped

	public void textButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_textButtonActionPerformed
		drawingBoard.setTool(DrawingBoard.TOOL_text);
	}// GEN-LAST:event_textButtonActionPerformed

	public void UpdateChanList() {
		ltChans.setModel(new AbstractListModel() {
			public Object getElementAt(int i) {
				return strings[i];
			}

			public int getSize() {
				return strings.length;
			}

			String[] strings = Manager.GetInstance().GetAvailableChannels();
		});

	}

	public void weightComboActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_weightComboActionPerformed
		drawingBoard.setStrokeIndex(weightCombo.getSelectedIndex());
	}// GEN-LAST:event_weightComboActionPerformed

	/*
	 * (non-Javadoc)
	 * 
	 * @see WindowListener#windowActivated(WindowEvent)
	 */
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see WindowListener#windowClosed(WindowEvent)
	 */
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see WindowListener#windowClosing(WindowEvent)
	 */
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see WindowListener#windowDeactivated(WindowEvent )
	 */
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see WindowListener#windowDeiconified(WindowEvent )
	 */
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see WindowListener#windowIconified(WindowEvent)
	 */
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see WindowListener#windowOpened(WindowEvent)
	 */
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public JFrame bg_frame;
	public JButton bgButton;
	// Variables declaration - do not modify//GEN-BEGIN:variables
	public JButton btOK;
	public JScrollPane centerPane;
	public JButton clearButton;
	public JColorChooser colorChooser;
	public JDialog colorChooserDialog;
	public JPanel colorPanel;

	public JMenu conferenceMenu;

	public JPanel confPanel;

	public JPanel ctrlPanel;

	public JPanel dbSettingsPanel;

	public JToggleButton diamondButton;

	public DrawingBoard drawingBoard;

	public JToggleButton eraserButton;

	public JComboBox eraserCombo;

	public JButton fgButton;

	public JFileChooser filechooser1, filechooser2;

	public JMenu fileMenu;

	public Color fillColor;

	public JButton jButton1;

	public JLabel jLabel1;

	public JLabel jLabel2;

	public JLabel jLabel3;

	public JLabel jLabel4;

	public JLabel jLabel5;

	public JLabel jLabel7;

	public JScrollPane jScrollPane1;

	public JToggleButton lineButton;

	public JList ltChans;

	public JMenuBar mainMenu;

	public JPanel mediumPanel1;

	public JPanel mediumPanel2;

	public JPanel mediumPanel3;

	public JPanel mediumPanel4;

	public JLabel mousePositionLabel, currentToolLabel;

	public JToggleButton ovalButton;

	public JToggleButton pencilButton;

	public FileFilter pngFilter = new FileFilter() {
		@Override
		public boolean accept(java.io.File f) {
			if (f.isDirectory())
				return true;
			if (f.getName().endsWith(".png"))
				return true;
			return false;
		}

		@Override
		public String getDescription() {
			return "PNG image file (*.png)";
		}
	};

	public JToggleButton rectButton;

	public FileFilter shapeTalkDrawFilter = new FileFilter() {
		@Override
		public boolean accept(java.io.File f) {
			if (f.isDirectory())
				return true;
			if (f.getName().endsWith(".jdw"))
				return true;
			return false;
		}

		@Override
		public String getDescription() {
			return "shapeTalkDraw Model File (*.jdw)";
		}
	};

	public JPanel statusBar;

	public Color strokeColor = Color.BLACK;

	public JPanel strokeSettingsPanel;

	public JTextField tbKey;

	public JTextField tbNewChan;

	public JTextField tbNickname;

	public JTextField tbSelKey;

	public JToggleButton textButton;

	public ButtonGroup toolsGroup;

	public JPanel toolsPanel;

	public JComboBox weightCombo;
}
