package ShapeTalk.Chat;

import java.io.Serializable;

public class SharingListMessage extends Message implements Serializable {

	public SharingListMessage(User iFrom, String[] iSharingList) {
		super("", iFrom);
		_share = iSharingList;

	}

	public String[] GetShareList() {
		return _share;
	}

	String[] _share;

}
