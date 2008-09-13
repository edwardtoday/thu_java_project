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
import javax.swing.DefaultComboBoxModel;
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
import ShapeTalk.DrawingBoard.IShape;
import ShapeTalk.DrawingBoard.NwFontChooserS;

public class ShapeTalk implements WindowListener, MouseListener,
		MouseMotionListener, ItemListener {

	public static DrawingBoard drawingBoard;

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

	public void bgButtonActionPerformed(ActionEvent evt) {// GEN-
		// FIRST
		// :
		// event_bgButtonActionPerformed
		final Color color = JColorChooser.showDialog(bg_frame,
				"Change Fill Color", colorPane.getFillColor());
		if (color != null) {
			colorPane.setFillColor(color);
			ShapeTalk.drawingBoard.setFillColor(color);
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

		if (listChannelList.getSelectedValue() == null
				&& !GetCreateNewChannel()) {
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
			final String selChan = (String) listChannelList.getSelectedValue();
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

	public void changeFontButtonActionPerformed() {
		Font font = ShapeTalk.drawingBoard.getFont();
		font = NwFontChooserS.showDialog(bg_frame, null, font);
		ShapeTalk.drawingBoard.setFont(font);
		string2draw.setFont(font);
	}

	public void checkBoxFillStateChanged(ItemEvent e) {
		ShapeTalk.drawingBoard.setFill(checkBoxFill.isSelected());
	}

	public void clearButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_clearButtonActionPerformed
		ShapeTalk.drawingBoard.clearBoard();
	}// GEN-LAST:event_clearButtonActionPerformed

	public void create_drawingboard() {

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

	public void createConfPane() {

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
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();// 得到屏幕的大小
		final int MARGIN = 30;
		bg_frame.setBounds((int) (MARGIN / 2), (int) (MARGIN / 2),
				(int) ((screen.getWidth() - MARGIN)), (int) ((screen
						.getHeight()
						- MARGIN - 20)));
		// bg_frame.setBounds(100, 100, 1043, 610);
		bg_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createMenu();
		createStatusbar();
		createConfPane();
		createToolPane();

		create_drawingboard();
	}

	public void createMenu() {
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
			// Edit
			editMenu = new JMenu();
			editMenu.setActionCommand("Edit");
			editMenu.setText("Menu");
			mainMenu.add(editMenu);
			{
				// Undo
				final JMenuItem undoMenuItem = new JMenuItem();
				undoMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						undoMenuItemActionPerformed();
					}
				});
				undoMenuItem.setText("Undo");
				editMenu.add(undoMenuItem);
				// Redo
				final JMenuItem redoMenuItem = new JMenuItem();
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
				// Start Conference
				final JMenuItem startConfMenuItem = new JMenuItem();
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
			// Help
			final JMenu helpMenuItem = new JMenu();
			helpMenuItem.setText("Help");
			mainMenu.add(helpMenuItem);
			{
				// About
				final JMenuItem viewDocMenuItem = new JMenuItem();
				viewDocMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						new AboutDialog(bg_frame);
						return;
					}
				});
				viewDocMenuItem.setText("View Doc");
				helpMenuItem.add(viewDocMenuItem);
			}
		}
	}

	public void createStatusbar() {
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
	}

	public void createToolPane() {
		toolsGroup = new ButtonGroup();

		ctrlPanel = new JPanel();
		bg_frame.getContentPane().add(ctrlPanel, BorderLayout.WEST);
		toolSettingsPane = new JPanel();

		colorPanel = new JPanel();
		colorPanel.setLayout(new BorderLayout());
		fgButton = new JButton();
		fgButton.setName("fgButton");
		bgButton = new JButton();
		bgButton.setName("bgButton");

		// eraserSlider = new JSlider(0, 100, 0);

		strokeSettingsPanel = new JPanel();
		strokeSettingsPane = new JPanel();
		// weightCombo = new JComboBox();
		weightSlider = new JSlider(1, 30, 2);

		eraserCombo = new JComboBox();
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
			public void actionPerformed(ActionEvent evt) {
				currentToolLabel.setForeground(Color.BLACK);
				currentToolLabel
						.setText("Select shapes and do transformations.");
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
			public void actionPerformed(ActionEvent evt) {
				currentToolLabel.setForeground(Color.BLACK);
				currentToolLabel.setText("Drag to draw lines.");
				lineButtonActionPerformed(evt);
			}
		});
		toolButtonsPane.add(lineButton);

		rectButton = new JToggleButton();
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
		toolButtonsPane.add(rectButton);

		roundrectButton = new JToggleButton();
		toolsGroup.add(roundrectButton);
		roundrectButton.setFont(new Font("Dialog", 0, 10));
		roundrectButton.setText("Round Rect");
		roundrectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				currentToolLabel.setForeground(Color.BLACK);
				currentToolLabel.setText("Drag to draw round rectangles.");
				roundrectButtonActionPerformed(evt);
			}
		});
		toolButtonsPane.add(roundrectButton);

		ovalButton = new JToggleButton();
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

		toolButtonsPane.add(ovalButton);
		diamondButton = new JToggleButton();
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

		toolButtonsPane.add(diamondButton);
		pencilButton = new JToggleButton();
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

		toolButtonsPane.add(pencilButton);
		eraserButton = new JToggleButton();
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
		toolButtonsPane.add(eraserButton);
		textButton = new JToggleButton();
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

		toolButtonsPane.add(textButton);
		clearButton = new JButton();
		clearButton.setFont(new Font("Dialog", 1, 10));
		clearButton.setText("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
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
			currentToolLabel.setForeground(Color.BLACK);
			currentToolLabel.setText("Select shapes");
			currentToolLabel.setForeground(Color.BLACK);
			currentToolLabel.setText("Drag to draw lines.");
		}
		{
			colorPanel.setBorder(new TitledBorder("Color Settings"));
			final JPanel colorPanelLeft = new JPanel();
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

			final JPanel colorPanelRight = new JPanel();
			colorPanelRight.setLayout(new BorderLayout());
			colorPanel.add("East", colorPanelRight);
			fgButton.setText("Set Stroke Color");
			fgButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					fgButtonActionPerformed(evt);
				}
			});
			colorPanelRight.add(fgButton);

			bgButton.setText("Set Fill Color");
			bgButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					bgButtonActionPerformed(evt);
				}
			});
			colorPanelRight.add(bgButton, BorderLayout.SOUTH);

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

			// eraserSlider.setBorder(new TitledBorder(null, "Eraser Size",
			// TitledBorder.DEFAULT_JUSTIFICATION,
			// TitledBorder.DEFAULT_POSITION, null, null));
			// eraserSlider.addChangeListener(new ChangeListener() {
			// public void stateChanged(final ChangeEvent e) {
			// eraserSliderStateChanged(e);
			// }
			// });
			// strokeSettingsPane.add(eraserSlider, BorderLayout.SOUTH);

			// weightCombo.setFont(new Font("Dialog", 0, 10));
			// weightCombo.setModel(new DefaultComboBoxModel(new String[] {
			// "Stroke Weight 1.0px", "Stroke Weight 2.0px",
			// "Stroke Weight 5.0px", "Stroke Weight 7.5px",
			// "Stroke Weight 10.0px" }));
			// weightCombo.addActionListener(new ActionListener() {
			// public void actionPerformed(ActionEvent evt) {
			// weightComboActionPerformed(evt);
			// }
			// });
			//
			// strokeSettingsPane.add(weightCombo, BorderLayout.NORTH);

			eraserCombo.setFont(new Font("Dialog", 0, 10));
			eraserCombo.setModel(new DefaultComboBoxModel(new String[] {
					"Eraser Size 15px", "Eraser Size 20px", "Eraser Size 30px",
					"Eraser Size 50px", "Eraser Size 100px" }));
			eraserCombo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					eraserComboActionPerformed(evt);
				}
			});
			strokeSettingsPane.add(eraserCombo, BorderLayout.SOUTH);

			strokeSettingsPanel.add(strokeSettingsPane);

			toolSettingsPane.add(strokeSettingsPanel);
		}
		{
			final JPanel shapesPane = new JPanel();
			toolSettingsPane.add(shapesPane);
			shapesPane.setLayout(new BorderLayout());
			shapesPane.setBorder(new TitledBorder(null, "Shapes",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));

			shapesComboBox = new JComboBox();
			shapesPane.add(shapesComboBox, BorderLayout.NORTH);

			final JPanel shapesEditPane = new JPanel();
			shapesEditPane.setLayout(new GridLayout(2, 0));
			shapesPane.add(shapesEditPane, BorderLayout.SOUTH);

			final JButton deleteShapeButton = new JButton();
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

			final JButton scaleButton = new JButton();
			scaleButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
				}
			});
			scaleButton.setText("Scale");
			shapesEditPane.add(scaleButton);

			final JButton rotateButton = new JButton();
			rotateButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
				}
			});
			rotateButton.setText("Rotate");
			shapesEditPane.add(rotateButton);

			final JButton shearButton = new JButton();
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

			final JButton changeFont = new JButton();
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

	public void diamondButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_diamondButtonActionPerformed
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_DIAMOND);
	}// GEN-LAST:event_diamondButtonActionPerformed

	public void eraserButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_eraserButtonActionPerformed
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_ERASER);
	}// GEN-LAST:event_eraserButtonActionPerformed

	public void eraserComboActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_eraserComboActionPerformed
		ShapeTalk.drawingBoard.setEraserIndex(eraserCombo.getSelectedIndex());
	}// GEN-LAST:event_eraserComboActionPerformed

	public void fgButtonActionPerformed(ActionEvent evt) {// GEN-
		// FIRST
		// :
		// event_fgButtonActionPerformed
		final Color color = JColorChooser.showDialog(bg_frame,
				"Change Stroke Color", colorPane.getStrokeColor());
		if (color != null) {
			ShapeTalk.drawingBoard.setStrokeColor(color);
			colorPane.setStrokeColor(color);
		}
	}// GEN-LAST:event_fgButtonActionPerformed

	public boolean GetCreateNewChannel() {
		return textFieldNewChannelName.getText().length() > 0;
	}

	public String GetNewChannelKey() {

		return textFieldInputKey.getText();
	}

	public String GetNewChannelName() {
		return textFieldNewChannelName.getText();
	}

	public String GetNick() {
		return textFieldNickname.getText();
	}

	// public void eraserSliderStateChanged(ChangeEvent evt) {// GEN
	// // -
	// // FIRST
	// // :
	// // event_eraserComboActionPerformed
	// drawingBoard.setEraserIndex(eraserSlider.getValue());
	// }// GEN-LAST:event_eraserComboActionPerformed

	public String GetSelChannelKey() {
		return textFieldSetKey.getText();
	}

	public void initConfPaneComponents() {
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
			public void keyTyped(KeyEvent evt) {
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
			public void actionPerformed(ActionEvent evt) {
				btOKActionPerformed(evt);
			}
		});

		confPanel.add(buttonOK);
		buttonOK.setBounds(10, 280, 220, 23);

		labelUpdateList.setText("Update List");
		labelUpdateList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButton1ActionPerformed(evt);
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
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_LINE);
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
			ShapeTalk.drawingBoard.setForeground(fgcolor);
			fgButton.setBackground(fgcolor);
			rgb = Integer.parseInt(br.readLine());
			final Color bgcolor = new Color(rgb);
			ShapeTalk.drawingBoard.setBackground(bgcolor);
			bgButton.setBackground(bgcolor);
			// weightCombo.setSelectedIndex(Integer.parseInt(br.readLine()));
			weightSlider.setValue(Integer.parseInt(br.readLine()));
			eraserCombo.setSelectedIndex(Integer.parseInt(br.readLine()));
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
	}// GEN-LAST:event_loadButtonActionPerformed

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
		if (e.getSource().equals(ShapeTalk.drawingBoard)) {
			ShapeTalk.showPosition = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MouseListener#mouseExited(MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		if (e.getSource().equals(ShapeTalk.drawingBoard)) {
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
		updateShapesComboBox();
	}

	public void ovalButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_ovalButtonActionPerformed
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_OVAL);
	}// GEN-LAST:event_ovalButtonActionPerformed

	// End of variables declaration//GEN-END:variables

	public void pencilButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_pencilButtonActionPerformed
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_PENCIL);
	}// GEN-LAST:event_pencilButtonActionPerformed

	/*
	 * (non-Javadoc)
	 * 
	 * @see MouseMotionListener#mouseMoved(MouseEvent)
	 */

	public void rectButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_rectButtonActionPerformed
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_RECT);
	}// GEN-LAST:event_rectButtonActionPerformed

	public void redoMenuItemActionPerformed() {
		if (ShapeTalk.drawingBoard.redos.size() > 0) {
			ShapeTalk.drawingBoard.shapes.add(ShapeTalk.drawingBoard.shapes
					.get(ShapeTalk.drawingBoard.redos.size() - 1));
			ShapeTalk.drawingBoard.redos.remove(ShapeTalk.drawingBoard.redos
					.size() - 1);
			ShapeTalk.drawingBoard.repaint();
		}
	}

	public void roundrectButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_rectButtonActionPerformed
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_ROUNDRECT);
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
			final Color fgcolor = ShapeTalk.drawingBoard.getForeground();
			final Color bgcolor = ShapeTalk.drawingBoard.getBackground();
			final PrintWriter pw = new PrintWriter(new FileWriter(file));
			pw.println(fgcolor.getRGB());
			pw.println(bgcolor.getRGB());
			// pw.println(weightCombo.getSelectedIndex());
			pw.println(weightSlider.getValue());
			pw.print(eraserCombo.getSelectedIndex());
			// pw.println(eraserSlider.getValue());
			pw.print(ShapeTalk.drawingBoard.getShapes());
			pw.close();
		} catch (final java.io.IOException ioe) {
			JOptionPane.showMessageDialog(bg_frame, ioe, "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_saveButtonActionPerformed

	public void seleButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_lineButtonActionPerformed
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_SELE);
	}// GEN-LAST:event_lineButtonActionPerformed

	public void startConf() {
		Manager.GetInstance();

		confPanel.setVisible(true);
		initConfPaneComponents();
	}

	public void string2drawChanged(ActionEvent e) {
		ShapeTalk.drawingBoard.setString(string2draw.getText());
	}

	public void tbNewChanKeyTyped(KeyEvent evt) {// GEN-FIRST:
		// event_tbNewChanKeyTyped
		textFieldInputKey
				.setEnabled(textFieldNewChannelName.getText().length() > 0);
	}// GEN-LAST:event_tbNewChanKeyTyped

	public void textButtonActionPerformed(ActionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_textButtonActionPerformed
		ShapeTalk.drawingBoard.setTool(DrawingBoard.TOOL_TEXT);
	}// GEN-LAST:event_textButtonActionPerformed

	public void undoMenuItemActionPerformed() {
		if (ShapeTalk.drawingBoard.shapes.size() > 0) {
			ShapeTalk.drawingBoard.redos.add(ShapeTalk.drawingBoard.shapes
					.get(ShapeTalk.drawingBoard.shapes.size() - 1));
			ShapeTalk.drawingBoard.shapes.remove(ShapeTalk.drawingBoard.shapes
					.size() - 1);
			ShapeTalk.drawingBoard.repaint();
		}
	}

	public void UpdateChanList() {
		listChannelList.setModel(new AbstractListModel() {
			public Object getElementAt(int i) {
				return strings[i];
			}

			public int getSize() {
				return strings.length;
			}

			String[] strings = Manager.GetInstance().GetAvailableChannels();
		});

	}

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
			pw.print(eraserCombo.getSelectedIndex());
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

			// drawingBoard.setShapes(list);
			try {
				final int size = list.size();
				for (int i = 0; i < size; i++) {
					final String[] split2 = ((String) list.get(i)).split("\t");
					final IShape shape;
					if (split2[0].equals("ShapeTalk.DrawingBoard.Line")) {
						// shape = new Line();
					} else if (split2[0].equals("ShapeTalk.DrawingBoard.Rect")) {
						// shape = new Rect();
					} else if (split2[0]
							.equals("ShapeTalk.DrawingBoard.RoundRect")) {
						// shape = new RoundRect();
					} else if (split2[0].equals("ShapeTalk.DrawingBoard.Oval")) {
						// shape = new Oval();
					} else if (split2[0]
							.equals("ShapeTalk.DrawingBoard.Diamond")) {
						// shape = new Diamond();
					} else if (split2[0]
							.equals("ShapeTalk.DrawingBoard.PolyLine")) {
						// shape = new PolyLine();
					} else if (split2[0]
							.equals("ShapeTalk.DrawingBoard.Eraser")) {
						// shape = new Eraser(this);
					} else if (split2[0].equals("ShapeTalk.DrawingBoard.Text")) {
						// shape = new Text();
					} else {
						// throw new Exception("Invalid Shape Data!");
					}
					// shape.setShapeData(split2[1]);
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

	public void weightSliderStateChanged(ChangeEvent e) {
		ShapeTalk.drawingBoard.setStrokeIndex(weightSlider.getValue());
	}

	// public void weightComboActionPerformed(ActionEvent evt) {// GEN
	// -
	// FIRST
	// :
	// event_weightComboActionPerformed
	// drawingBoard.setStrokeIndex(weightCombo.getSelectedIndex());
	// }// GEN-LAST:event_weightComboActionPerformed

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
	public JButton buttonOK;

	public JScrollPane centerPane;
	public JButton clearButton;
	public JColorChooser colorChooser;
	public JDialog colorChooserDialog;
	public ColorPane colorPane;
	public JPanel colorPanel;
	public JMenu conferenceMenu;
	public JPanel confPanel;

	public JPanel ctrlPanel;

	public int currentSelectedShapeIndex;

	public JToggleButton diamondButton;

	public JToggleButton eraserButton;

	public JComboBox eraserCombo;
	// public JSlider eraserSlider;

	public JButton fgButton;

	public JFileChooser filechooser1, filechooser2;

	public JMenu fileMenu;

	public JPanel fontSettingsPane;

	public JLabel labelCreateChannel;

	public JLabel labelInputKey;

	public JLabel labelJoinChannel;

	public JLabel labelName;

	public JLabel labelNickName;

	public JLabel labelSetKey;

	public JButton labelUpdateList;

	public JToggleButton lineButton;

	public JList listChannelList;
	public JMenuBar mainMenu;

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

	public JToggleButton roundrectButton;

	public JScrollPane scrollPaneChannelList;

	public JToggleButton seleButton;

	public JComboBox shapesComboBox;

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
	public JTextField string2draw;
	public JPanel strokeSettingsPane;
	public JPanel strokeSettingsPanel;
	public JToggleButton textButton;
	public JTextField textFieldInputKey;
	public JTextField textFieldNewChannelName;
	public JTextField textFieldNickname;
	public JTextField textFieldSetKey;
	public JPanel toolButtonsPane;
	public JPanel toolSettingsPane;
	public ButtonGroup toolsGroup;
	public JPanel toolsPanel;
	public JComboBox weightCombo;
	public JSlider weightSlider;
	private File tempFile;
	JCheckBox checkBoxFill;
	JMenu editMenu;
	JLabel fontPreview;
}
