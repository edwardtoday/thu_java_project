package ShapeTalk.Chat;

import java.awt.event.MouseEvent;
import java.util.TreeMap;

import javax.swing.JOptionPane;

/**
 * frmUserShare.java
 * 
 * User sharing frame.
 * 
 * @author Q
 * 
 */
public class frmUserShare extends javax.swing.JFrame {

	private static TreeMap _userMap = new TreeMap();

	public static frmUserShare ForUser(final User iUser) {
		if (!frmUserShare._userMap.containsKey(iUser)) {
			frmUserShare._userMap.put(iUser, new frmUserShare(iUser));
		}
		final frmUserShare retFrm = (frmUserShare) frmUserShare._userMap
				.get(iUser);
		retFrm.UpdateShare();
		return retFrm;
	}

	private frmUserShare(final User iUser) {
		_user = iUser;
		initComponents();
		setTitle(iUser.GetName() + " Share list");
		setSize(400, 300);
	}

	/**
	 * Add to cache.
	 * 
	 * @param iMsg
	 */
	public void AddToCache(final AttachmentMessage iMsg) {
		_filecache.put(iMsg.GetFileName(), iMsg);
		if (ltFileList.getSelectedValue() != null
				&& ltFileList.getSelectedValue().equals(iMsg.GetFileName())) {
			tbContent.setText(iMsg.GetContent());
		}
	}

	public void Show() {
		setVisible(true);
		requestFocus();
	}

	/**
	 * Initialize frame components.
	 */
	private void initComponents() {
		mnuPopup = new javax.swing.JPopupMenu();
		mnuSaveas = new javax.swing.JMenuItem();
		panSplit = new javax.swing.JSplitPane();
		scrollFileList = new javax.swing.JScrollPane();
		ltFileList = new javax.swing.JList();
		scrollContent = new javax.swing.JScrollPane();
		tbContent = new javax.swing.JTextArea();

		mnuSaveas.setLabel("Save as...");
		mnuSaveas.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuSaveasActionPerformed(evt);
			}
		});

		mnuPopup.add(mnuSaveas);

		getContentPane().setLayout(new java.awt.GridLayout(1, 0));

		setTitle("Share list");
		setName("frmUserShare");
		panSplit.setDividerLocation(140);
		ltFileList.setFont(new java.awt.Font("Tahoma", 0, 10));
		ltFileList.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(final java.awt.event.MouseEvent evt) {
				ltFileListMouseClicked(evt);
			}
		});

		scrollFileList.setViewportView(ltFileList);

		panSplit.setLeftComponent(scrollFileList);

		tbContent.setBackground(new java.awt.Color(204, 204, 204));
		tbContent.setColumns(20);
		tbContent.setRows(5);
		scrollContent.setViewportView(tbContent);

		panSplit.setRightComponent(scrollContent);

		getContentPane().add(panSplit);

	}

	/**
	 * File choosen
	 * 
	 * @param evt
	 */
	private void ltFileListMouseClicked(final java.awt.event.MouseEvent evt) {
		if (evt.getButton() != MouseEvent.BUTTON1) {
			mnuPopup.show(ltFileList, evt.getX(), evt.getY());
		}

		if (ltFileList.getSelectedValue() == null) {
			return;
		}
		if (!_filecache.containsKey(ltFileList.getSelectedValue())) {
			final ServiceMessage newMsg = new ServiceMessage(Manager
					.GetInstance().GetMe(), _user,
					ServiceMessage.CODE_ASK_FILE, (String) ltFileList
							.getSelectedValue());
			Manager.GetInstance().GetDispatcher().DispatchToAll(newMsg);
		} else {
			tbContent.setText(((AttachmentMessage) _filecache.get(ltFileList
					.getSelectedValue())).GetContent());
		}
	}

	/**
	 * Save as..
	 * 
	 * @param evt
	 */
	private void mnuSaveasActionPerformed(final java.awt.event.ActionEvent evt) {
		if (ltFileList.getSelectedIndex() >= 0) {
			if (!_filecache.containsKey(ltFileList.getSelectedValue())) {
				return;
			}

			final java.io.File oFile = FileDialog.SaveFileDialog();

			if (oFile == null) {
				return;
			}
			try {
				final AttachmentMessage curSel = (AttachmentMessage) _filecache
						.get(ltFileList.getSelectedValue());
				curSel.SaveToFile(oFile.getPath());
			} catch (final Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}

		}
	}

	/**
	 * Update sharing tree.
	 * 
	 */
	private boolean UpdateShare() {
		if (Manager.GetInstance().GetUserShare(_user) == null) {
			JOptionPane.showMessageDialog(this, "User has no active share",
					"Error", JOptionPane.ERROR_MESSAGE);
			setVisible(false);
			return false;
		}
		ltFileList.setModel(new StringListModel(Manager.GetInstance()
				.GetUserShare(_user)));
		return true;
	}

	private final TreeMap _filecache = new TreeMap();

	private final User _user;

	/**
	 * Sharing file list.
	 */
	private javax.swing.JList ltFileList;

	private javax.swing.JPopupMenu mnuPopup;

	private javax.swing.JMenuItem mnuSaveas;

	private javax.swing.JSplitPane panSplit;

	private javax.swing.JScrollPane scrollContent;

	private javax.swing.JScrollPane scrollFileList;

	/**
	 * File preview area.
	 */
	private javax.swing.JTextArea tbContent;

}

class StringListModel extends javax.swing.AbstractListModel {
	public StringListModel(final String[] iList) {
		_list = iList;
	}

	public Object getElementAt(final int i) {
		return _list[i];
	}

	public int getSize() {
		return _list.length;
	}

	String[] _list;
}
