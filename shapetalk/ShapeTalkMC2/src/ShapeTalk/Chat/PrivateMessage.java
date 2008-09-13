package ShapeTalk.Chat;

import java.io.Serializable;

/**
 * PrivateMessage.java
 * 
 * PrivateMessage is text message sent by the user to another user (instead all
 * the users on the current channel).
 * 
 * @author Q
 */
public class PrivateMessage extends Message implements Serializable {
	public PrivateMessage(final String iMsg, final User iFrom, final User iTo) {
		super(iMsg, iFrom);
		_to = iTo;
	}

	public User GetTo() {
		return _to;
	}

	private final User _to;

}
