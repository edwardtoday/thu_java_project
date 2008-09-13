package ShapeTalk.Chat;

/**
 * Users.java
 * 
 * Users class.
 * 
 * @author Q
 */
public class User implements java.io.Serializable, Comparable {
	/**
	 * Anonymous user.
	 */
	public static User Anonymous = new User();
	public static final char STATUS_ASKINGNICK = '1';
	public static final char STATUS_AUTH = '3';
	public static final char STATUS_NICKFAILED = '2';
	public static final char STATUS_NOTAUTH = '0';

	/** Creates a new instance of User */
	public User(final String iName) {
		_name = iName;
	}

	/** Creates an anonymous user */
	private User() {
		_anonymous = true;
		_name = "???";
	}

	/**
	 * Compare users.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final Object iTo) {
		if (!(iTo instanceof User)) {
			return 0;
		}
		return _name.compareTo(((User) iTo)._name);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof User)) {
			return false;
		}
		if (obj == null) {
			return false;
		}
		if (IsAnonymous() || ((User) obj).IsAnonymous()) {
			return false;
		}

		return _name.equals(((User) obj)._name);
	}

	/**
	 * Get user name.
	 * 
	 */
	public String GetName() {
		return _name;
	}

	/**
	 * Get user status.
	 * 
	 */
	public char GetStatus() {
		return _status;
	}

	/**
	 * Check if user is anonymous.
	 * 
	 */
	public boolean IsAnonymous() {
		return _anonymous;
	}

	/**
	 * Set user status.
	 * 
	 * @param iStatus
	 */
	public void SetStatus(final char iStatus) {
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
