package ShapeTalk.Chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * MulticastDispatcher.java
 * 
 * MulticastNetworkDispatcher is the derived class of NetworkDispather that
 * sends data over the network using multicast sockets.
 * 
 * @author Q
 */
public class MulticastDispatcher extends NetworkDispatcher {
	private class RecvThread extends Thread {
		public RecvThread(final NetworkDispatcher iDispatcher,
				final MulticastSocket iSock) {
			_cSock = iSock;
			_Dispatcher = iDispatcher;
		}

		@Override
		public void run() {
			final byte inBuf[] = new byte[MulticastDispatcher.RecvBufSize];
			final DatagramPacket rPack = new DatagramPacket(inBuf,
					MulticastDispatcher.RecvBufSize);

			try {
				for (;;) {
					_cSock.receive(rPack);
					_Dispatcher
							.DataReceived(rPack.getData(), rPack.getLength());

				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}

		private final MulticastSocket _cSock;

		private final NetworkDispatcher _Dispatcher;
	}

	/**
	 * Default multicast group ip.
	 */
	public static final String DefaultMulticastGroupIP = "230.1.1.1";

	/**
	 * Default multicast group port.
	 */
	public static final int DefaultMulticastGroupPort = 1314;

	/**
	 * Receiver buffer size.
	 */
	private static final int RecvBufSize = 65536;

	/** Creates a new instance of MulticastDispatcher */
	public MulticastDispatcher() throws Exception {
		_curAddr = InetAddress
				.getByName(MulticastDispatcher.DefaultMulticastGroupIP);
		_curPort = MulticastDispatcher.DefaultMulticastGroupPort;
		_cSock = new MulticastSocket(_curPort);
		_cSock.joinGroup(_curAddr);

		_rThread = new RecvThread(this, _cSock);
		_rThread.start();
	}

	@Override
	protected void DispatchToAll(final byte iBuf[], final int iSize)
			throws Exception {
		final DatagramPacket dPack = new DatagramPacket(iBuf, iSize, _curAddr,
				_curPort);
		_cSock.send(dPack);
	}

	private final MulticastSocket _cSock;

	private final InetAddress _curAddr;

	private final int _curPort;

	private final RecvThread _rThread;

}
