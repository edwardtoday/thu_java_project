package ShapeTalk.Chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.WindowConstants;

public class FileDialog {
	private static File _selfile;

	public static File DirFileDialog() {
		return FileDialog.GenericFileDialog(JFileChooser.OPEN_DIALOG,
				JFileChooser.DIRECTORIES_ONLY);
	}

	public static File OpenFileDialog() {
		return FileDialog.GenericFileDialog(JFileChooser.OPEN_DIALOG);
	}

	public static File SaveFileDialog() {
		return FileDialog.GenericFileDialog(JFileChooser.SAVE_DIALOG);
	}

	private static File GenericFileDialog(int iType) {
		return FileDialog.GenericFileDialog(iType, JFileChooser.FILES_ONLY);
	}

	private static File GenericFileDialog(int iType, int iSelMode) {
		FileDialog._selfile = null;
		final JDialog fDiag = new JDialog();
		fDiag.setModal(true);

		final JFileChooser fc = new JFileChooser();
		fc.setDialogType(iType);

		fc.setFileSelectionMode(iSelMode);
		fc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getActionCommand().equals("ApproveSelection")) {
					FileDialog._selfile = fc.getSelectedFile();
				}
				fDiag.setVisible(false);

			}

		});
		fDiag.getContentPane().add(fc);
		fDiag.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		fDiag.setSize(400, 300);
		fDiag.setVisible(true);
		return fc.getSelectedFile();

	}

}
