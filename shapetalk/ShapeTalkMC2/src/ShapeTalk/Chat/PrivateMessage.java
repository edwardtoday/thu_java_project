package ShapeTalk.Chat;

import java.io.Serializable;

public class PrivateMessage extends Message implements Serializable {
	public PrivateMessage(String iMsg, User iFrom, User iTo) {
		super(iMsg, iFrom);
		_to = iTo;
	}

	public User GetTo() {
		return _to;
	}

	private final User _to;

}
