package ShapeTalk.Chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeMap;

import javax.swing.JOptionPane;

public class Manager {
	public static final int DefaultOperTimeout = 3000;

	private static final int MAX_RECVFILES_NUM = 50;

	/* Singleton Structure */
	private static Manager singObj;

	public static Manager GetInstance() {
		try {
			if (Manager.singObj == null) {
				Manager.singObj = new Manager();
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
		return Manager.singObj;
	}

	/* End of Singleton Structure */

	private Manager() throws Exception {
		_Dispatcher = new MulticastDispatcher();
		_me = User.Anonymous; // Anonymous user
	}

	public String[] GetAvailableChannels() {
		final String[] outArr = new String[_ChannelList.size()];
		_ChannelList.toArray(outArr);
		return outArr;
	}

	public Channel GetCurrentChannel() {
		return _curChan;
	}

	public NetworkDispatcher GetDispatcher() {
		return _Dispatcher;
	}

	public User GetMe() {
		return _me;
	}

	public String[] GetUserShare(User iUser) {
		if (!_SharingMap.containsKey(iUser))
			return null;
		return (String[]) _SharingMap.get(iUser);
	}

	public boolean HasJoined() {
		return _curChan != null;
	}

	public boolean IsChannelFree(String iChanName) {
		_ChannelFree = true;
		final ServiceMessage newMsg = new ServiceMessage(GetMe(),
				ServiceMessage.CODE_QUERY_CHAN_FREE, iChanName);
		GetDispatcher().DispatchToAll(newMsg);
		try {
			Thread.sleep(Manager.DefaultOperTimeout);
		} catch (final InterruptedException ex) {
		}
		return _ChannelFree;
	}

	public void ParseMessage(Message iMsg) {
		if (iMsg instanceof ServiceMessage) {
			ParseServiceMessage((ServiceMessage) iMsg);
		} else if (iMsg instanceof ChannelMessage) {
			ParseChannelMessage((ChannelMessage) iMsg);
		} else if (iMsg instanceof PrivateMessage) {
			ParsePrivateMessage((PrivateMessage) iMsg);
		} else if (iMsg instanceof AttachmentMessage) {
			ParseAttachmentMessage((AttachmentMessage) iMsg);
		} else if (iMsg instanceof SharingListMessage) {
			ParseSharingListMessage((SharingListMessage) iMsg);
		}
	}

	public void Quit() {
		if (_curChan != null) {
			final ServiceMessage newMsg = new ServiceMessage(GetMe(),
					ServiceMessage.CODE_PART, _curChan.GetName());
			GetDispatcher().DispatchToAll(newMsg);
		}
		System.exit(0);
	}

	public void SendChanMessage(String iMsg) {
		if (_curChan == null)
			return;

		final ChannelMessage newMsg = new ChannelMessage(iMsg, GetMe(),
				_curChan);
		GetDispatcher().DispatchToAll(newMsg);

	}

	public void SendFileToAll() {
		try {

			final java.io.File chFile = FileDialog.OpenFileDialog();
			if (chFile == null)
				return;
			if (!chFile.exists())
				return;
			if (chFile.length() > GetDispatcher().GetMaxFileSize()) {
				JOptionPane.showMessageDialog(null,
						"File is too big (max size: "
								+ GetDispatcher().GetMaxFileSize() + " bytes)",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			final AttachmentMessage newMsg = new AttachmentMessage(GetMe(),
					chFile, false);
			GetDispatcher().DispatchToAll(newMsg);
		} catch (final Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	public void SendPrivateMsg(User iTo, String iMsg) {
		final PrivateMessage newMsg = new PrivateMessage(iMsg, GetMe(), iTo);
		GetDispatcher().DispatchToAll(newMsg);
	}

	public void SetAndAdvertiseChannel(Channel iChan) {
		_curChan = iChan;
		_curChan.AddUser(GetMe());
		_ReqChan = "";
		if (_advThread != null) {
			_advThread.stop();
		}
		_advThread = new ChanAdvertiserThread(_curChan);
		_advThread.start();
	}

	public void SetMyShare(boolean iSet) {
		if (!iSet) {
			_myshare.Unshare();
		} else {
			final java.io.File shareFld = FileDialog.DirFileDialog();
			if (shareFld == null || !shareFld.exists()
					|| !shareFld.isDirectory())
				return;
			_myshare.ShareDir(shareFld.getAbsolutePath());
			System.out.println(_myshare.GetSharedFiles().length
					+ " files shared");
			// broadcast my share
			final SharingListMessage newMsg = new SharingListMessage(GetMe(),
					_myshare.GetSharedFiles());
			GetDispatcher().DispatchToAll(newMsg);
		}
	}

	public void ShowRecvFiles() {
		final AttachmentMessage[] recvArr = new AttachmentMessage[_recvfiles
				.size()];

		try {
			if (_recvui != null) {
				_recvui.dispose();
			}
			_recvfiles.toArray(recvArr);
			_recvui = new frmRecvFiles(recvArr);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	public void StartPrivateConversation(User iTo) {
		if (iTo == null)
			return;
		if (!_privconvs.containsKey(iTo)) {
			_privconvs.put(iTo, new PrivateConversation(iTo));
		} else {
			((PrivateConversation) _privconvs.get(iTo)).Show();
		}
	}

	public boolean TrySetNick(String iNick) {
		_me = new User(iNick);
		_me.SetStatus(User.STATUS_ASKINGNICK);
		final ServiceMessage newMsg = new ServiceMessage(User.Anonymous,
				ServiceMessage.CODE_QUERY_NICK_FREE, iNick);
		GetDispatcher().DispatchToAll(newMsg);
		try {
			Thread.sleep(Manager.DefaultOperTimeout);
		} catch (final InterruptedException ex) {
		}
		if (_me.GetStatus() == User.STATUS_NICKFAILED)
			return false;

		_me.SetStatus(User.STATUS_AUTH);
		return true;
	}

	private void ParseAttachmentMessage(AttachmentMessage iMsg) {
		if (iMsg.IsRequested()) {
			frmUserShare.ForUser(iMsg.GetSender()).AddToCache(iMsg);
		} else {
			_recvfiles.add(iMsg);
			if (_recvfiles.size() > Manager.MAX_RECVFILES_NUM) {
				_recvfiles.removeFirst();
			}
			if (_curChan != null) {
				_curChan.Notice("New file received " + iMsg);
			}
		}
	}

	private void ParseChannelMessage(ChannelMessage iMsg) {
		if (iMsg.GetChannel() == null)
			return;
		if (!iMsg.GetChannel().equals(_curChan))
			return;

		iMsg.GetChannel().MessageReceived(iMsg);
	}

	private void ParsePrivateMessage(PrivateMessage iMsg) {
		if (!iMsg.GetTo().equals(GetMe()))
			return;

		StartPrivateConversation(iMsg.GetSender());
		((PrivateConversation) _privconvs.get(iMsg.GetSender()))
				.MessageArrival(iMsg.GetText());
	}

	private void ParseServiceMessage(ServiceMessage iMsg) {
		if (!iMsg.IsBroadcast() && !iMsg.GetToUser().equals(GetMe()))
			return;

		switch (iMsg.GetCode()) {
		case ServiceMessage.CODE_CHAN_ADV: {
			if (!_ChannelList.contains(iMsg.GetArg())) {
				_ChannelList.add(iMsg.GetArg());
				System.out.println("New channel discovered " + iMsg.GetArg());
			}
		}
			break;
		case ServiceMessage.CODE_JOIN: {
			if (_curChan == null) {// my join
				_ReqChan = iMsg.GetArg();
			} else {// somone else join
				if (!iMsg.GetArg().equals(_curChan.GetName()))
					return;
				_curChan.Join(iMsg.GetSender());

				// Send helo to the client to notify it that i'm in the channel
				ServiceMessage newMsg = new ServiceMessage(GetMe(),
						ServiceMessage.CODE_HELOJOIN, _curChan.GetName());
				GetDispatcher().DispatchToAll(newMsg);

				// If i'm the owner of the chan, notify the new arrived that i'm
				// the owner.!!Hackable
				if (GetMe().equals(_curChan.GetOwner())) {
					newMsg = new ServiceMessage(GetMe(),
							ServiceMessage.CODE_CHAN_OWNER, _curChan.GetName());
					GetDispatcher().DispatchToAll(newMsg);
				}

			}
		}
			break;

		case ServiceMessage.CODE_HELOJOIN: {
			if (_curChan == null && _ReqChan.length() > 0
					&& _ReqChan.equals(iMsg.GetArg())) {
				System.out.println("Join Accepted");
				SetAndAdvertiseChannel(new Channel(iMsg.GetArg()));
				_curChan.AddUser(GetMe());
				synchronized (WaitForJoinAck) {
					WaitForJoinAck.notify();
				}
			} else if (_curChan == null && _ReqChan.length() <= 0)
				return;

			if (!iMsg.GetArg().equals(_curChan.GetName()))
				return;

			_curChan.AddUser(iMsg.GetSender());
		}
			break;

		case ServiceMessage.CODE_PART: {
			if (_curChan == null)
				return;
			if (!iMsg.GetArg().equals(_curChan.GetName()))
				return;
			_curChan.Part(iMsg.GetSender());

		}
			break;

		case ServiceMessage.CODE_CHAN_OWNER: {
			if (_curChan == null)
				return;
			if (!iMsg.GetArg().equals(_curChan.GetName()))
				return;
			_curChan.SetOwner(iMsg.GetSender());
		}
			break;

		case ServiceMessage.CODE_QUERY_NICK_FREE: {
			if (GetMe().GetName().equals(iMsg.GetArg())
					&& GetMe().GetStatus() == User.STATUS_AUTH) {
				final ServiceMessage newMsg = new ServiceMessage(GetMe(),
						ServiceMessage.CODE_NICK_TAKEN, GetMe().GetName());
				GetDispatcher().DispatchToAll(newMsg);
			}
		}
			break;

		case ServiceMessage.CODE_QUERY_CHAN_FREE: {
			if (Channel.GetByName(iMsg.GetArg()) != null) {
				final ServiceMessage newMsg = new ServiceMessage(GetMe(),
						ServiceMessage.CODE_CHAN_TAKEN, iMsg.GetArg());
				GetDispatcher().DispatchToAll(newMsg);
			}
		}
			break;

		case ServiceMessage.CODE_CHAN_TAKEN: {
			if (_curChan.GetName().equals(iMsg.GetArg())) {
				_ChannelFree = false;
			}
		}
			break;

		case ServiceMessage.CODE_NICK_TAKEN: {
			if (GetMe().GetStatus() != User.STATUS_ASKINGNICK)
				return;
			GetMe().SetStatus(User.STATUS_NICKFAILED);
		}
			break;

		case ServiceMessage.CODE_ASK_SHARE: {
			final SharingListMessage newMsg = new SharingListMessage(GetMe(),
					_myshare.GetSharedFiles());
			GetDispatcher().DispatchToAll(newMsg);
		}
			break;

		case ServiceMessage.CODE_ASK_FILE: {
			final String[] MyShareList = _myshare.GetSharedFiles();
			System.out.println("requested shared file " + iMsg.GetArg());
			if (MyShareList.length <= 0)
				return;

			if (Arrays.binarySearch(MyShareList, iMsg.GetArg()) < 0)
				return;

			final java.io.File sFile = new java.io.File(_myshare
					.GetFullFilePath(iMsg.GetArg()));
			if (!sFile.exists()) {
				System.out.println("Cannot find file " + sFile.getPath());
				return;
			}
			AttachmentMessage newMsg;
			try {
				newMsg = new AttachmentMessage(GetMe(), sFile, true);
				newMsg.SetFileName(iMsg.GetArg());
				GetDispatcher().DispatchToAll(newMsg);
			} catch (final Exception ex) {
				ex.printStackTrace();
			}

		}
			break;

		}
	}

	private void ParseSharingListMessage(SharingListMessage iMsg) {
		_SharingMap.put(iMsg.GetSender(), iMsg.GetShareList());
		if (_curChan != null) {
			_curChan
					.Notice(iMsg.GetSender()
							+ " is sharing "
							+ iMsg.GetShareList().length
							+ " files... right click in the nick list to see its share");
		}
	}

	public Object WaitForJoinAck = new Object();

	private ChanAdvertiserThread _advThread;

	private boolean _ChannelFree = true;

	private final ArrayList _ChannelList = new ArrayList();

	private Channel _curChan;

	private final NetworkDispatcher _Dispatcher;

	private User _me;

	private final FileSharing _myshare = new FileSharing();

	private final TreeMap _privconvs = new TreeMap();

	private final LinkedList _recvfiles = new LinkedList();

	private frmRecvFiles _recvui;

	private String _ReqChan = "";

	private final TreeMap _SharingMap = new TreeMap();

}
