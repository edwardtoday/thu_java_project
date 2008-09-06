package ShapeTalk.Chat;

import java.io.Serializable;

public class ServiceMessage extends Message implements Serializable {
	public final static char CODE_ASK_FILE = 'f';
	public final static char CODE_ASK_SHARE = 's';
	public final static char CODE_CHAN_ADV = 'a';

	public final static char CODE_CHAN_OWNER = 'o';
	public final static char CODE_CHAN_TAKEN = 'h';
	public final static char CODE_HELOJOIN = 'i';
	public final static char CODE_JOIN = 'j';
	public final static char CODE_NICK_TAKEN = 't';
	public final static char CODE_PART = 'p';
	public final static char CODE_QUERY_CHAN_FREE = 'c';
	public final static char CODE_QUERY_NICK_FREE = 'n';

	public ServiceMessage(User iFrom, char iCode, String iArg) {
		super("", iFrom);
		_code = iCode;
		_arg = iArg;
	}

	public ServiceMessage(User iFrom, User iTo, char iCode, String iArg) {
		this(iFrom, iCode, iArg);
		_to = iTo;
	}

	@Override
	public boolean DontEncrypt() {
		if (_code == ServiceMessage.CODE_CHAN_ADV
				|| _code == ServiceMessage.CODE_QUERY_NICK_FREE
				|| _code == ServiceMessage.CODE_QUERY_CHAN_FREE
				|| _code == ServiceMessage.CODE_NICK_TAKEN
				|| _code == ServiceMessage.CODE_CHAN_TAKEN)
			return true;
		return false;
	}

	public String GetArg() {
		return _arg;
	}

	public char GetCode() {
		return _code;
	}

	public User GetToUser() {
		return _to;
	}

	public boolean IsBroadcast() {
		return _to == null;
	}

	private final char _code;

	String _arg;

	User _to;

}
