package ShapeTalk.Chat;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

public class FileSharing {

	public FileSharing() {
	}

	public FileSharing(String iShareFld) {
		ShareDir(iShareFld);
	}

	public String GetFullFilePath(String iFile) {
		if (_BaseShareFolder == null)
			return "";
		return _BaseShareFolder.getAbsolutePath() + iFile;
	}

	public String[] GetSharedFiles() {
		final String[] outArr = new String[_SharedFiles.size()];
		for (int i = 0; i < _SharedFiles.size(); i++) {
			outArr[i] = ((File) _SharedFiles.get(i)).getAbsolutePath()
					.substring(_BaseShareFolder.getAbsolutePath().length());
		}

		Arrays.sort(outArr);
		return outArr;
	}

	public void ShareDir(String iPath) {
		_BaseShareFolder = new File(iPath);
		_SharedFiles.clear();
		if (!_BaseShareFolder.exists())
			return;
		AddShareDir(_BaseShareFolder);
	}

	public void Unshare() {
		_BaseShareFolder = null;
		_SharedFiles.clear();
	}

	private void AddShareDir(File iDir) {
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

	private File _BaseShareFolder;

	private final LinkedList _SharedFiles = new LinkedList();

}
