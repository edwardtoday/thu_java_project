package ShapeTalk.Chat;

import java.io.Serializable;

/**
 * Message.java
 * 
 * The Message class.
 * 
 * @author Q
 */
public abstract class Message implements Serializable {
	protected Message(final String iMsg, final User iFrom) {
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
