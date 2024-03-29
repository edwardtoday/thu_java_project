package ShapeTalk.Chat;

/**
 * PrivateConversation.java
 * 
 * @author Q
 * 
 */
public class PrivateConversation {
	public PrivateConversation(final User iTo) {
		_to = iTo;
		_ui = new frmPrivateConv(this);
	}

	public User GetTo() {
		return _to;
	}

	public void MessageArrival(final String iMsg) {
		_ui.AddRecvLine("<" + _to.GetName() + "> " + iMsg);
		if (!_ui.isVisible()) {
			_ui.setVisible(true);
			_ui.requestFocus();
		}

	}

	public void SendMessage(final String iMsg) {
		Manager.GetInstance().SendPrivateMsg(_to, iMsg);
		_ui.AddRecvLine("<" + Manager.GetInstance().GetMe().GetName() + "> "
				+ iMsg);
	}

	public void Show() {
		_ui.setVisible(true);
	}

	private final User _to;

	private final frmPrivateConv _ui;
}
