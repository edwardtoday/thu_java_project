package ShapeTalk.Chat;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 * frmChan.java
 * 
 * Frame of the channel viewer.
 * 
 * @author Q
 * 
 */
public class frmChan extends javax.swing.JFrame {

	public frmChan() {
		initComponents();
		msgArea.setEditable(false);
		setSize(500, 400);
		this.requestFocus();
	}

	/**
	 * Add received line.
	 * 
	 * @param iMsg
	 */
	public void AddRecvLine(final String iMsg) {
		msgArea.setText(msgArea.getText() + iMsg + "\n");
		msgArea.scrollRectToVisible(new Rectangle(0, msgArea.getHeight() - 20,
				1, 1));
	}

	/**
	 * Clear message receiving area.
	 */
	public void ClearRecvArea() {
		msgArea.setText("");
	}

	/**
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		Manager.GetInstance().Quit();
	}

	/**
	 * Send message.
	 */
	public void SendMsg() {
		Manager.GetInstance().SendChanMessage(typeBox.getText());
		typeBox.setText("");
	}

	/**
	 * Update nickname list.
	 * 
	 * @param iUsers
	 */
	public void UpdateNickList(final User[] iUsers) {
		if (nickList.getModel() == null
				|| !(nickList.getModel() instanceof NickListModel)) {
			nickList.setModel(new NickListModel(iUsers));
		}
		((NickListModel) nickList.getModel()).Update(iUsers);

	}

	private void btClearActionPerformed(final java.awt.event.ActionEvent evt) {
		msgArea.setText("");
	}

	private void btRecvFilesActionPerformed(final java.awt.event.ActionEvent evt) {
		Manager.GetInstance().ShowRecvFiles();
	}

	private void btShareActionPerformed(final java.awt.event.ActionEvent evt) {
		Manager.GetInstance().SetMyShare(btShare.isSelected());

	}

	/**
	 * Initialize frame components.
	 */
	private void initComponents() {
		nickPopup = new javax.swing.JPopupMenu();
		ppStartConv = new javax.swing.JMenuItem();
		ppShowShare = new javax.swing.JMenuItem();
		panSplit = new javax.swing.JSplitPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		nickList = new javax.swing.JList();
		panRight = new javax.swing.JPanel();
		TextScroller = new javax.swing.JScrollPane();
		msgArea = new javax.swing.JTextArea();
		typeBox = new javax.swing.JTextField();
		toolbar = new javax.swing.JToolBar();
		btShare = new javax.swing.JToggleButton();
		tbSendFile = new javax.swing.JButton();
		btRecvFiles = new javax.swing.JButton();
		btClear = new javax.swing.JButton();

		nickPopup.setInvoker(nickList);
		ppStartConv.setText("Private Conversation");
		ppStartConv.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				ppStartConvActionPerformed(evt);
			}
		});

		nickPopup.add(ppStartConv);

		ppShowShare.setText("Show Share");
		ppShowShare.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				ppShowShareActionPerformed(evt);
			}
		});

		nickPopup.add(ppShowShare);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("ShapeTalk.Chat Channel view");
		setName("frmMain");
		panSplit.setDividerLocation(100);
		panSplit.setPreferredSize(new java.awt.Dimension(200, 25));
		nickList.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(final java.awt.event.MouseEvent evt) {
				nickListMouseReleased(evt);
			}
		});

		jScrollPane1.setViewportView(nickList);

		panSplit.setLeftComponent(jScrollPane1);

		panRight.setLayout(new java.awt.BorderLayout());

		TextScroller.setAutoscrolls(true);
		msgArea.setBackground(new java.awt.Color(229, 229, 249));
		msgArea.setColumns(20);
		msgArea.setRows(5);
		msgArea.setEditable(false);
		TextScroller.setViewportView(msgArea);

		panRight.add(TextScroller, java.awt.BorderLayout.CENTER);

		typeBox.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(final java.awt.event.KeyEvent evt) {
				typeBoxKeyPressed(evt);
			}
		});

		panRight.add(typeBox, java.awt.BorderLayout.SOUTH);

		panSplit.setRightComponent(panRight);

		getContentPane().add(panSplit, java.awt.BorderLayout.CENTER);

		btShare.setText("Directory Sharing");
		btShare.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				btShareActionPerformed(evt);
			}
		});

		toolbar.add(btShare);

		tbSendFile.setText("Send File");
		tbSendFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				tbSendFileActionPerformed(evt);
			}
		});

		toolbar.add(tbSendFile);

		btRecvFiles.setText("Received Files");
		btRecvFiles.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				btRecvFilesActionPerformed(evt);
			}
		});

		toolbar.add(btRecvFiles);

		btClear.setText("Clear buffer");
		btClear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				btClearActionPerformed(evt);
			}
		});

		toolbar.add(btClear);

		getContentPane().add(toolbar, java.awt.BorderLayout.NORTH);

		pack();
	}

	/**
	 * Show nickname list.
	 * 
	 * @param evt
	 */
	private void nickListMouseReleased(final java.awt.event.MouseEvent evt) {
		if (evt.getButton() != MouseEvent.BUTTON1) {
			nickPopup.show(nickList, evt.getX(), evt.getY());
		}

	}

	private void ppShowShareActionPerformed(final java.awt.event.ActionEvent evt) {
		if (nickList.getSelectedValue() != null
				&& nickList.getSelectedValue() instanceof User) {
			frmUserShare.ForUser((User) nickList.getSelectedValue()).Show();
		}

	}

	private void ppStartConvActionPerformed(final java.awt.event.ActionEvent evt) {
		if (nickList.getSelectedValue() != null
				&& nickList.getSelectedValue() instanceof User) {
			Manager.GetInstance().StartPrivateConversation(
					(User) nickList.getSelectedValue());
		}

	}

	private void tbSendFileActionPerformed(final java.awt.event.ActionEvent evt) {
		Manager.GetInstance().SendFileToAll();
	}

	private void typeBoxKeyPressed(final java.awt.event.KeyEvent evt) {
		if (evt.getKeyChar() == '\n') {
			SendMsg();
		}
	}

	private javax.swing.JButton btClear;
	private javax.swing.JButton btRecvFiles;
	private javax.swing.JToggleButton btShare;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextArea msgArea;
	private javax.swing.JList nickList;
	private javax.swing.JPopupMenu nickPopup;
	private javax.swing.JPanel panRight;
	private javax.swing.JSplitPane panSplit;
	private javax.swing.JMenuItem ppShowShare;
	private javax.swing.JMenuItem ppStartConv;
	private javax.swing.JButton tbSendFile;
	private javax.swing.JScrollPane TextScroller;
	private javax.swing.JToolBar toolbar;
	private javax.swing.JTextField typeBox;

}

class NickListModel extends javax.swing.AbstractListModel {
	public NickListModel(final User[] iList) {
		_nicklist = iList;
	}

	public Object getElementAt(final int i) {
		return _nicklist[i];
	}

	public int getSize() {
		return _nicklist.length;
	}

	public void Update(final User[] iList) {
		fireIntervalRemoved(this, 0, getSize());
		_nicklist = iList;
		fireIntervalAdded(this, 0, getSize());
	}

	User[] _nicklist;
}