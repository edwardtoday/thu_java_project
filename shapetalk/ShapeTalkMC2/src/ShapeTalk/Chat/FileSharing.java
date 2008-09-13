package ShapeTalk.Chat;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * FileSharing.java
 * 
 * Code of the FileSharing class.
 * 
 * @author Q
 * 
 */
public class FileSharing {

	public FileSharing() {
	}

	/**
	 * @param iShareFld
	 */
	public FileSharing(final String iShareFld) {
		ShareDir(iShareFld);
	}

	/**
	 * Get full file path.
	 * 
	 * @param iFile
	 */
	public String GetFullFilePath(final String iFile) {
		if (_BaseShareFolder == null) {
			return "";
		}
		return _BaseShareFolder.getAbsolutePath() + iFile;
	}

	/**
	 * Get shared files.
	 * 
	 */
	public String[] GetSharedFiles() {
		final String[] outArr = new String[_SharedFiles.size()];
		for (int i = 0; i < _SharedFiles.size(); i++) {
			outArr[i] = ((File) _SharedFiles.get(i)).getAbsolutePath()
					.substring(_BaseShareFolder.getAbsolutePath().length());
		}

		Arrays.sort(outArr);
		return outArr;
	}

	/**
	 * Share a directory.
	 * 
	 * @param iPath
	 */
	public void ShareDir(final String iPath) {
		_BaseShareFolder = new File(iPath);
		_SharedFiles.clear();
		if (!_BaseShareFolder.exists()) {
			return;
		}
		AddShareDir(_BaseShareFolder);
	}

	/**
	 * Unshare directory.
	 */
	public void Unshare() {
		_BaseShareFolder = null;
		_SharedFiles.clear();
	}

	/**
	 * Add shared directory.
	 * 
	 * @param iDir
	 */
	private void AddShareDir(final File iDir) {
		final File[] fList = iDir.listFiles();
		for (int i = 0; i < fList.length; i++) {

			if (fList[i].isFile()) {
				if (fList[i].length() > Manager.GetInstance().GetDispatcher()
						.GetMaxFileSize()) {
					continue;
				}
				_SharedFiles.add(fList[i]);
			} else if (fList[i].isDirectory()) {
				AddShareDir(fList[i]);
			}
		}
	}

	/**
	 * Base share folder.
	 */
	private File _BaseShareFolder;

	/**
	 * List of shared files.
	 */
	private final LinkedList _SharedFiles = new LinkedList();

}
