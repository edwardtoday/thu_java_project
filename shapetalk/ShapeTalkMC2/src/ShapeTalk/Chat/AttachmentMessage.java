package ShapeTalk.Chat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Arrays;

public class AttachmentMessage extends Message implements Serializable {
	public AttachmentMessage(User iFrom, File iFile, boolean iReq)
			throws Exception {
		super(iFile.getName(), iFrom);
		_filename = iFile.getName();
		_filelength = (int) iFile.length();
		_filecontent = new byte[_filelength];
		_requested = iReq;
		final FileInputStream fIn = new FileInputStream(iFile);
		fIn.read(_filecontent);
		fIn.close();
		_checksum = CalcDigest();
	}

	public boolean CheckDigest() throws Exception {
		return Arrays.equals(_checksum, CalcDigest());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AttachmentMessage))
			return false;
		final AttachmentMessage tMsg = (AttachmentMessage) obj;
		return GetSender().equals(tMsg.GetSender())
				&& _filename.equals(tMsg._filename);
	}

	public byte[] GetBytes() {
		return _filecontent;
	}

	public String GetContent() {
		final char[] chArr = new char[_filecontent.length];
		for (int i = 0; i < _filecontent.length; i++) {
			chArr[i] = (char) _filecontent[i];
		}
		return new String(chArr);
	}

	public String GetFileName() {
		return _filename;
	}

	public int GetLength() {
		return _filelength;
	}

	public boolean IsRequested() {
		return _requested;
	}

	public void SaveToFile(String iPath) throws Exception {
		final FileOutputStream fOut = new FileOutputStream(iPath);
		fOut.write(_filecontent);
		fOut.close();
	}

	public void SetFileName(String iName) {
		_filename = iName;
	}

	@Override
	public String toString() {
		return "[" + GetSender() + "] " + _filename + " ( " + _filelength
				+ " bytes)";
	}

	private byte[] CalcDigest() throws Exception {
		final MessageDigest md = MessageDigest.getInstance("MD5");
		return md.digest(_filecontent);
	}

	private final byte[] _checksum;

	private final byte[] _filecontent;

	private final int _filelength;

	private String _filename;

	private final boolean _requested; // The file is sent by the user, or
										// requested by
	// another user share
}
