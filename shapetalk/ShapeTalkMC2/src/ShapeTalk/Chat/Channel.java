package ShapeTalk.Chat;

import java.util.LinkedList;
import java.util.TreeMap;

public class Channel {
	private static TreeMap _knownchannels = new TreeMap();

	public static void CreateNew(String iName, String iKey) {
		final Channel newChan = new Channel(iName);
		if (iKey != null && iKey.length() > 0) {
			newChan.SetKey(iKey);
		}
		newChan.SetOwner(Manager.GetInstance().GetMe());
		Manager.GetInstance().SetAndAdvertiseChannel(newChan);
	}

	public static Channel GetByName(String iName) {
		if (Channel._knownchannels.containsKey(iName))
			return (Channel) Channel._knownchannels.get(iName);
		else
			return null;
	}

	public static void JoinExisting(String iName, String iKey) throws Exception {
		Manager.GetInstance().GetDispatcher().SetKey(iKey);
		final ServiceMessage newMsg = new ServiceMessage(Manager.GetInstance()
				.GetMe(), ServiceMessage.CODE_JOIN, iName);
		Manager.GetInstance().GetDispatcher().DispatchToAll(newMsg);
	}

	/** Creates a new instance of Channel */
	public Channel(String iName) {
		_name = iName;

		_ui = new frmChan();
		_ui.setTitle("Channel " + _name);
		_ui.setVisible(true);

		Channel._knownchannels.put(_name, this);
	}

	public void AddUser(User iUser) {

		if (_users.contains(iUser))
			return;

		_users.add(iUser);
		_ui.UpdateNickList(GetUsers());

	}

	public boolean equals(Channel iTo) {
		if (iTo == null)
			return false;
		return _name.equals(iTo._name);
	}

	@Override
	public void finalize() throws Throwable {
		Channel._knownchannels.remove(this);
		super.finalize();
	}

	public String GetName() {
		return _name;
	}

	public User GetOwner() {
		return _owner;
	}

	public User[] GetUsers() {
		final User[] outArr = new User[_users.size()];

		_users.toArray(outArr);
		return outArr;
	}

	public void Join(User iUser) {
		Notice(iUser.GetName() + " joined " + GetName());
		AddUser(iUser);

	}

	public void MessageReceived(ChannelMessage iMsg) {
		_ui.AddRecvLine("< " + iMsg.GetSender().GetName() + "> "
				+ iMsg.GetText());
	}

	public void Notice(String iStr) {
		_ui.AddRecvLine("::: " + iStr);
	}

	public void Part(User iUser) {
		if (_users.contains(iUser)) {
			_users.remove(iUser);
		}
		Notice(iUser.GetName() + " left " + GetName());
		_ui.UpdateNickList(GetUsers());
	}

	public void SetKey(String iKey) {
		Manager.GetInstance().GetDispatcher().SetKey(iKey);
	}

	public void SetOwner(User iOwner) {
		_owner = iOwner;
	}

	private final String _name;

	private User _owner;

	private final frmChan _ui;

	private final LinkedList _users = new LinkedList();
}
