package ShapeTalk.Chat;

public class User implements java.io.Serializable, Comparable {
	public static User Anonymous = new User();
	public static final char STATUS_ASKINGNICK = '1';
	public static final char STATUS_AUTH = '3';
	public static final char STATUS_NICKFAILED = '2';
	public static final char STATUS_NOTAUTH = '0';

	/** Creates a new instance of User */
	public User(String iName) {
		_name = iName;
	}

	// Creates an anonymous user
	private User() {
		_anonymous = true;
		_name = "???";
	}

	public int compareTo(Object iTo) {
		if (!(iTo instanceof User))
			return 0;
		return _name.compareTo(((User) iTo)._name);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof User))
			return false;
		if (obj == null)
			return false;
		if (IsAnonymous() || ((User) obj).IsAnonymous())
			return false;

		return _name.equals(((User) obj)._name);
	}

	public String GetName() {
		return _name;
	}

	public char GetStatus() {
		return _status;
	}

	public boolean IsAnonymous() {
		return _anonymous;
	}

	public void SetStatus(char iStatus) {
		_status = iStatus;
	}

	@Override
	public String toString() {
		return _name;
	}

	private boolean _anonymous = false;

	private final String _name;

	private char _status = User.STATUS_NOTAUTH;
}
