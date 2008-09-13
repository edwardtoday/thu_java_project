package ShapeTalk.Chat;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Channel.java
 * 
 * Code of the channel class.
 * 
 * @author Q
 */
public class Channel {
	/**
	 * Tree of known channels.
	 */
	private static TreeMap _knownchannels = new TreeMap();

	/**
	 * Create new channel.
	 * 
	 * @param iName
	 * @param iKey
	 */
	public static void CreateNew(final String iName, final String iKey) {
		final Channel newChan = new Channel(iName);
		if (iKey != null && iKey.length() > 0) {
			newChan.SetKey(iKey);
		}
		newChan.SetOwner(Manager.GetInstance().GetMe());
		Manager.GetInstance().SetAndAdvertiseChannel(newChan);
	}

	/**
	 * Get channel by name.
	 * 
	 * @param iName
	 */
	public static Channel GetByName(final String iName) {
		if (Channel._knownchannels.containsKey(iName)) {
			return (Channel) Channel._knownchannels.get(iName);
		} else {
			return null;
		}
	}

	/**
	 * Join existing channel.
	 * 
	 * @param iName
	 * @param iKey
	 * @throws Exception
	 */
	public static void JoinExisting(final String iName, final String iKey)
			throws Exception {
		Manager.GetInstance().GetDispatcher().SetKey(iKey);
		final ServiceMessage newMsg = new ServiceMessage(Manager.GetInstance()
				.GetMe(), ServiceMessage.CODE_JOIN, iName);
		Manager.GetInstance().GetDispatcher().DispatchToAll(newMsg);
	}

	/**
	 * Creates a new instance of Channel
	 * 
	 * @param iName
	 */
	public Channel(final String iName) {
		_name = iName;

		_ui = new frmChan();
		_ui.setTitle("Channel " + _name);
		_ui.setVisible(true);

		Channel._knownchannels.put(_name, this);
	}

	/**
	 * Add user to channel.
	 * 
	 * @param iUser
	 */
	public void AddUser(final User iUser) {

		if (_users.contains(iUser)) {
			return;
		}

		_users.add(iUser);
		_ui.UpdateNickList(GetUsers());

	}

	/**
	 * @param iTo
	 * 
	 */
	public boolean equals(final Channel iTo) {
		if (iTo == null) {
			return false;
		}
		return _name.equals(iTo._name);
	}

	/**
	 * @see java.lang.Object#finalize()
	 */
	@Override
	public void finalize() throws Throwable {
		Channel._knownchannels.remove(this);
		super.finalize();
	}

	/**
	 * Return channel name.
	 * 
	 */
	public String GetName() {
		return _name;
	}

	/**
	 * Get channel owner.
	 * 
	 */
	public User GetOwner() {
		return _owner;
	}

	/**
	 * Get channel users.
	 * 
	 */
	public User[] GetUsers() {
		final User[] outArr = new User[_users.size()];

		_users.toArray(outArr);
		return outArr;
	}

	/**
	 * Show user joining channel.
	 * 
	 * @param iUser
	 */
	public void Join(final User iUser) {
		Notice(iUser.GetName() + " joined " + GetName());
		AddUser(iUser);

	}

	/**
	 * Show received message.
	 * 
	 * @param iMsg
	 */
	public void MessageReceived(final ChannelMessage iMsg) {
		_ui.AddRecvLine("< " + iMsg.GetSender().GetName() + "> "
				+ iMsg.GetText());
	}

	/**
	 * Show notice.
	 * 
	 * @param iStr
	 */
	public void Notice(final String iStr) {
		_ui.AddRecvLine("::: " + iStr);
	}

	/**
	 * User quits.
	 * 
	 * @param iUser
	 */
	public void Part(final User iUser) {
		if (_users.contains(iUser)) {
			_users.remove(iUser);
		}
		Notice(iUser.GetName() + " left " + GetName());
		_ui.UpdateNickList(GetUsers());
	}

	/**
	 * Set channel key.
	 * 
	 * @param iKey
	 */
	public void SetKey(final String iKey) {
		Manager.GetInstance().GetDispatcher().SetKey(iKey);
	}

	/**
	 * Set channel owner.
	 * 
	 * @param iOwner
	 */
	public void SetOwner(final User iOwner) {
		_owner = iOwner;
	}

	/**
	 * Name of the channel.
	 */
	private final String _name;

	/**
	 * Owner of the channel.
	 */
	private User _owner;

	/**
	 * Frame of the channel viewer.
	 */
	private final frmChan _ui;

	/**
	 * List of users.
	 */
	private final LinkedList _users = new LinkedList();
}
