package ShapeTalk.Chat;

import javax.swing.JOptionPane;

/**
 * frmRecvFiles.java
 * 
 * Received files frame.
 * 
 * @author Q
 * 
 */
public class frmRecvFiles extends javax.swing.JFrame {

	public frmRecvFiles(final AttachmentMessage[] iArr) {
		initComponents();
		_files = iArr;
		ltFiles.setModel(new FileListModel(_files));
		setVisible(true);
	}

	/**
	 * Initialize frame components.
	 */
	private void initComponents() {
		mnuPopup = new javax.swing.JPopupMenu();
		mnuSaveas = new javax.swing.JMenuItem();
		panSplit = new javax.swing.JSplitPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		ltFiles = new javax.swing.JList();
		scrollArea = new javax.swing.JScrollPane();
		tbFileContent = new javax.swing.JTextArea();

		mnuSaveas.setLabel("Save as...");
		mnuSaveas.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				mnuSaveasActionPerformed(evt);
			}
		});

		mnuPopup.add(mnuSaveas);

		getContentPane().setLayout(
				new javax.swing.BoxLayout(getContentPane(),
						javax.swing.BoxLayout.X_AXIS));

		setTitle("Received Files");
		setName("frmRecvFiles");
		panSplit.setDividerLocation(100);
		panSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		ltFiles.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(final java.awt.event.MouseEvent evt) {
				ltFilesMouseClicked(evt);
			}
		});

		jScrollPane1.setViewportView(ltFiles);

		panSplit.setTopComponent(jScrollPane1);

		tbFileContent.setBackground(new java.awt.Color(204, 204, 204));
		tbFileContent.setColumns(20);
		tbFileContent.setRows(5);
		scrollArea.setViewportView(tbFileContent);

		panSplit.setRightComponent(scrollArea);

		getContentPane().add(panSplit);

		pack();
	}

	/**
	 * Preview file as plain text.
	 * 
	 * @param evt
	 */
	private void ltFilesMouseClicked(final java.awt.event.MouseEvent evt) {
		if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
			if (ltFiles.getSelectedIndex() >= 0) {
				final AttachmentMessage curSel = (AttachmentMessage) ltFiles
						.getSelectedValue();
				tbFileContent.setText(curSel.GetContent());
			}
		} else {
			mnuPopup.show(this, evt.getX(), evt.getY());
		}
	}

	/**
	 * Save file as...
	 * 
	 * @param evt
	 */
	private void mnuSaveasActionPerformed(final java.awt.event.ActionEvent evt) {
		if (ltFiles.getSelectedIndex() >= 0) {
			final AttachmentMessage curSel = (AttachmentMessage) ltFiles
					.getSelectedValue();
			final java.io.File oFile = FileDialog.SaveFileDialog();

			if (oFile == null) {
				return;
			}
			try {
				curSel.SaveToFile(oFile.getPath());
			} catch (final Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}

		}
	}

	private final AttachmentMessage[] _files;

	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JList ltFiles;
	private javax.swing.JPopupMenu mnuPopup;

	private javax.swing.JMenuItem mnuSaveas;

	private javax.swing.JSplitPane panSplit;

	private javax.swing.JScrollPane scrollArea;

	private javax.swing.JTextArea tbFileContent;

}

class FileListModel extends javax.swing.AbstractListModel {
	public FileListModel(final AttachmentMessage[] iList) {
		_list = iList;
	}

	public Object getElementAt(final int i) {
		return _list[i];
	}

	public int getSize() {
		return _list.length;
	}

	AttachmentMessage[] _list;
}