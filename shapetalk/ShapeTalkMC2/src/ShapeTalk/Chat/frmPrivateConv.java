package ShapeTalk.Chat;

import java.awt.Rectangle;

/**
 * frmPrivateConve.java
 * 
 * Private conversation frame.
 * 
 * @author Q
 * 
 */
public class frmPrivateConv extends javax.swing.JFrame {
	/**
	 * @param iRef
	 */
	public frmPrivateConv(final PrivateConversation iRef) {
		_refConv = iRef;
		initComponents();
		setSize(300, 300);
		setVisible(true);
		requestFocus();
		setTitle(_refConv.GetTo().GetName());
		typeBox.requestFocus();

	}

	/**
	 * @param iMsg
	 */
	public void AddRecvLine(final String iMsg) {
		msgArea.setText(msgArea.getText() + iMsg + "\n");
		msgArea.scrollRectToVisible(new Rectangle(0, msgArea.getHeight() - 20,
				1, 1));
	}

	/**
	 * 
	 */
	public void ClearRecvArea() {
		msgArea.setText("");
	}

	/**
	 * 
	 */
	public void SendMsg() {
		if (typeBox.getText().equals("/clear")
				|| typeBox.getText().equals("/cls")) {
			ClearRecvArea();
		} else {
			_refConv.SendMessage(typeBox.getText());
		}
		typeBox.setText("");
	}

	/**
	 * Initialize frame components.
	 */
	private void initComponents() {
		typeBox = new javax.swing.JTextField();
		jScroller = new javax.swing.JScrollPane();
		msgArea = new javax.swing.JTextArea();

		setTitle("Private Conversation");
		setName("frmPrivateConv");
		typeBox.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(final java.awt.event.KeyEvent evt) {
				typeBoxKeyTyped(evt);
			}
		});

		getContentPane().add(typeBox, java.awt.BorderLayout.SOUTH);

		msgArea.setBackground(new java.awt.Color(244, 241, 241));
		msgArea.setColumns(20);
		msgArea.setEditable(false);
		msgArea.setRows(5);
		jScroller.setViewportView(msgArea);

		getContentPane().add(jScroller, java.awt.BorderLayout.CENTER);

		pack();
	}

	private void typeBoxKeyTyped(final java.awt.event.KeyEvent evt) {
		if (evt.getKeyChar() == '\n') {
			SendMsg();
		}
	}

	private final PrivateConversation _refConv;

	private javax.swing.JScrollPane jScroller;

	/**
	 * Message area.
	 */
	private javax.swing.JTextArea msgArea;

	/**
	 * Type box.
	 */
	private javax.swing.JTextField typeBox;

}
