package ShapeTalk.Chat;

import java.io.Serializable;

public abstract class Message implements Serializable {
	/*
	 * public byte[] toByteArray(){ try { ByteArrayOutputStream bStream=new
	 * ByteArrayOutputStream(); ObjectOutputStream dStream=new
	 * ObjectOutputStream(bStream); dStream.writeObject(_from);
	 * dStream.writeUTF(_text); dStream.close(); return bStream.toByteArray(); }
	 * catch (IOException ex) { ex.printStackTrace(); return new byte[0]; } }
	 */
	protected Message(String iMsg, User iFrom) {
		_text = iMsg;
		_from = iFrom;
	}

	public boolean DontEncrypt() {
		return false;
	}

	public User GetSender() {
		return _from;
	}

	public String GetText() {
		return _text;
	}

	protected User _from;

	protected String _text;

}
