package ShapeTalk.Chat;

import java.io.IOException;
import java.io.Serializable;

/**
 * ChannelMessage.java
 * 
 * ChannelMessage is a text message sent by the user to a channel.
 * 
 * @author Q
 * 
 */
public class ChannelMessage extends Message implements Serializable {
	/**
	 * @param iMsg
	 * @param iFrom
	 * @param iToChan
	 */
	public ChannelMessage(final String iMsg, final User iFrom,
			final Channel iToChan) {
		super(iMsg, iFrom);
		_channel = iToChan;
	}

	/**
	 * Get channel.
	 */
	public Channel GetChannel() {
		return _channel;
	}

	/**
	 * Read object.
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(final java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		_channel = Channel.GetByName(in.readUTF());
	}

	/**
	 * Write object.
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(final java.io.ObjectOutputStream out)
			throws IOException {
		out.writeUTF(_channel.GetName());
	}

	private Channel _channel;

}
