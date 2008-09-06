package ShapeTalk.Chat;

public class ChanAdvertiserThread extends Thread {
	public ChanAdvertiserThread(Channel iChan) {
		_channel = iChan;
	}

	@Override
	public void run() {
		final Manager manager = Manager.GetInstance();

		for (;;) {
			try {
				final ServiceMessage advMsg = new ServiceMessage(manager
						.GetMe(), ServiceMessage.CODE_CHAN_ADV, _channel
						.GetName());
				manager.GetDispatcher().DispatchToAll(advMsg);
				Thread.sleep(2000);
			} catch (final InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	private final Channel _channel;
}
