package ShapeTalk.Chat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Provider;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * NetworkDispatcher.java
 * 
 * NetworkDispatcher is a generic (abstract) class that delivers messages over
 * the network. It does not really care how messages will be really delivered
 * (derived classes will do it).
 * 
 * Supports password.
 * 
 * @author Q
 */
public abstract class NetworkDispatcher {

	private static final byte BLOCK_ENCRYPTED = (byte) 0xba;
	private static final byte BLOCK_UNENCRYPTED = (byte) 0xab;
	private static byte[] salt = { (byte) 0xc9, (byte) 0x53, (byte) 0x67,
			(byte) 0x9a, (byte) 0x5b, (byte) 0xc8, (byte) 0xae, (byte) 0x18 };

	protected NetworkDispatcher() {
		try {

			_encCipher = Cipher.getInstance("PBEWithMD5AndDES");
			_decCipher = Cipher.getInstance("PBEWithMD5AndDES");

		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Dispath to all.
	 * 
	 * @param iMsg
	 */
	public void DispatchToAll(final Message iMsg) {
		try {
			final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			final boolean EncData = _keySet && !iMsg.DontEncrypt();

			if (EncData) {
				bOut.write(NetworkDispatcher.BLOCK_ENCRYPTED);
			} else {
				bOut.write(NetworkDispatcher.BLOCK_UNENCRYPTED);
			}

			OutputStream underlayingStream = bOut;
			if (EncData) {
				underlayingStream = new CipherOutputStream(bOut, _encCipher);
			}

			final ObjectOutputStream ooStream = new ObjectOutputStream(
					underlayingStream);

			ooStream.writeObject(iMsg);
			ooStream.close();

			bOut.toByteArray();

			DispatchToAll(bOut.toByteArray());

		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Get max file size.
	 * 
	 */
	public int GetMaxFileSize() {
		if (_keySet) {
			return _decCipher.getOutputSize(65000);
		} else {
			return 65000;
		}
	}

	/**
	 * Set key.
	 * 
	 * @param iKey
	 */
	public void SetKey(final String iKey) {
		if (iKey == null || iKey.length() <= 0) {
			_keySet = false;
			return;
		}

		_keySet = true;
		try {
			final Provider sunJce = new com.sun.crypto.provider.SunJCE();
			Security.addProvider(sunJce);
			_paramSpec = new PBEParameterSpec(NetworkDispatcher.salt, 20);
			final PBEKeySpec keySpec = new PBEKeySpec(iKey.toCharArray());
			final SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("PBEWithMD5AndDES");
			_secretKey = keyFactory.generateSecret(keySpec);

			_encCipher.init(Cipher.ENCRYPT_MODE, _secretKey, _paramSpec);
			_decCipher.init(Cipher.DECRYPT_MODE, _secretKey, _paramSpec);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Data received.
	 * 
	 * @param iBuf
	 * @param iLen
	 */
	protected void DataReceived(byte[] iBuf, int iLen) {
		try {
			if (iBuf[0] == NetworkDispatcher.BLOCK_ENCRYPTED) {
				if (!_keySet) {
					return;
				}
				final byte[] outBuf = new byte[_decCipher
						.getOutputSize(iLen - 1) + 1];
				iLen = _decCipher.doFinal(iBuf, 1, iLen - 1, outBuf, 1) + 1;
				iBuf = outBuf;
			} else if (iBuf[0] != NetworkDispatcher.BLOCK_UNENCRYPTED) {
				return;
			}

			final ByteArrayInputStream bIn = new ByteArrayInputStream(iBuf, 1,
					iLen - 1);
			final ObjectInputStream ooStream = new ObjectInputStream(bIn);
			final Object msgIn = ooStream.readObject();
			ooStream.close();
			final Message recMsg = (Message) msgIn;
			Manager.GetInstance().ParseMessage(recMsg);

		} catch (final BadPaddingException ex) {

			try {
				_decCipher.init(Cipher.DECRYPT_MODE, _secretKey, _paramSpec);
			} catch (final Exception exc) {
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
		}

	}

	protected void DispatchToAll(final byte[] iBuf) throws Exception {
		DispatchToAll(iBuf, iBuf.length);
	}

	protected abstract void DispatchToAll(byte[] iBuf, int iSize)
			throws Exception;

	private Cipher _decCipher;

	private Cipher _encCipher;

	private boolean _keySet = false;

	private PBEParameterSpec _paramSpec;

	private SecretKey _secretKey;

	SecretKeySpec _keyIV;
}
