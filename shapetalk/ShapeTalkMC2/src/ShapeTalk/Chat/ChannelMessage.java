package ShapeTalk.Chat;

import java.io.IOException;
import java.io.Serializable;

public class ChannelMessage extends Message implements Serializable {
	public ChannelMessage(String iMsg, User iFrom, Channel iToChan) {
		super(iMsg, iFrom);
		_channel = iToChan;
	}

	public Channel GetChannel() {
		return _channel;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		_channel = Channel.GetByName(in.readUTF());
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeUTF(_channel.GetName());
	}

	private Channel _channel;

}
