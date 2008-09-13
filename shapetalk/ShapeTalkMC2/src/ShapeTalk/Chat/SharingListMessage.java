package ShapeTalk.Chat;

import java.io.Serializable;

/**
 * SharingListMessage.java
 * 
 * SharingListMessage is not really a message but a particular structure that
 * tells everyone files that the user is currently sharing. It could be imagined
 * such a service message of this kind: “Hey this is my share list: xxx yyy
 * etc.”
 * 
 * @author Q
 * 
 */
public class SharingListMessage extends Message implements Serializable {

	public SharingListMessage(final User iFrom, final String[] iSharingList) {
		super("", iFrom);
		_share = iSharingList;

	}

	public String[] GetShareList() {
		return _share;
	}

	String[] _share;

}
