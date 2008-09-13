package ShapeTalk.Chat;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * frmStart.java
 * 
 * Start conference frame.
 * 
 * @author Q
 */
public class frmStart extends javax.swing.JFrame {

	private static frmStart _singObj;

	/**
	 * Singleton
	 * 
	 */
	public static frmStart GetInstance() {
		if (frmStart._singObj == null) {
			frmStart._singObj = new frmStart();
		}
		return frmStart._singObj;
	}

	/**
	 * New frames
	 */
	private frmStart() {
		initComponents();
		this.setSize(280, 320);
		update(getGraphics());
	}

	public boolean GetCreateNewChannel() {
		return tbNewChan.getText().length() > 0;
	}

	public String GetNewChannelKey() {

		return tbKey.getText();
	}

	public String GetNewChannelName() {
		return tbNewChan.getText();
	}

	public String GetNick() {
		return tbNickname.getText();
	}

	public String GetSelChannelKey() {
		return tbSelKey.getText();
	}

	/**
	 * Start new conference command committed.
	 * 
	 * @param evt
	 */
	private void btOKActionPerformed(final java.awt.event.ActionEvent evt) {
		if (GetNick().length() == 0) {
			JOptionPane.showMessageDialog(this, "Nick cannot be empty",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (ltChans.getSelectedValue() == null && !GetCreateNewChannel()) {
			JOptionPane.showMessageDialog(this,
					"You must select a channel to join or create a new one",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		final JDialog jWait = new JDialog(this,
				"Wait... Checking nick and channel availability");
		jWait.setResizable(false);
		// jWait.setAlwaysOnTop(true);
		jWait.setSize(400, 20);
		jWait.setVisible(true);
		jWait.requestFocus();

		boolean NickAvail = true, ChanAvail = true;

		NickAvail = Manager.GetInstance().TrySetNick(GetNick());
		if (GetCreateNewChannel()) {
			ChanAvail = Manager.GetInstance()
					.IsChannelFree(GetNewChannelName());
		}

		jWait.dispose();

		if (!NickAvail) {
			JOptionPane.showMessageDialog(this, "Nick already taken", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (GetCreateNewChannel()) {
			if (ChanAvail) {
				Channel.CreateNew(GetNewChannelName(), GetNewChannelKey());
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Channel already exists",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			final String selChan = (String) ltChans.getSelectedValue();
			try {
				Channel.JoinExisting(selChan, GetSelChannelKey());
				System.out.println("Join requested");
				synchronized (Manager.GetInstance().WaitForJoinAck) {
					Manager.GetInstance().WaitForJoinAck
							.wait(Manager.DefaultOperTimeout);
				}
				if (Manager.GetInstance().GetCurrentChannel() == null) {
					JOptionPane
							.showMessageDialog(
									this,
									"Timeout waiting welcome acknowledgement (probably due to a WRONG KEY)",
									"Error", JOptionPane.ERROR_MESSAGE);
					System.exit(-1);
				}
				dispose();
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	/**
	 * Initialize frame components.
	 */
	private void initComponents() {
		tbNickname = new javax.swing.JTextField();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		ltChans = new javax.swing.JList();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		tbNewChan = new javax.swing.JTextField();
		jLabel5 = new javax.swing.JLabel();
		tbKey = new javax.swing.JTextField();
		btOK = new javax.swing.JButton();
		jButton1 = new javax.swing.JButton();
		tbSelKey = new javax.swing.JTextField();
		jLabel7 = new javax.swing.JLabel();

		getContentPane().setLayout(null);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("ShapeTalk.Chat");
		setAlwaysOnTop(true);
		setBackground(new java.awt.Color(255, 255, 255));
		setName("frmStart");
		setResizable(false);
		getContentPane().add(tbNickname);
		tbNickname.setBounds(100, 10, 170, 20);

		jLabel1.setFont(new java.awt.Font("Arial", 0, 12));
		jLabel1.setText("Nickname:");
		getContentPane().add(jLabel1);
		jLabel1.setBounds(10, 10, 90, 20);

		jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
		jLabel2.setText("Join a channel...");
		getContentPane().add(jLabel2);
		jLabel2.setBounds(10, 50, 89, 14);

		jScrollPane1.setViewportView(ltChans);

		getContentPane().add(jScrollPane1);
		jScrollPane1.setBounds(20, 70, 250, 110);

		jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
		jLabel3.setText("... Or create a new channel");
		getContentPane().add(jLabel3);
		jLabel3.setBounds(10, 220, 148, 14);

		jLabel4.setFont(new java.awt.Font("Arial", 0, 12));
		jLabel4.setText("Name:");
		getContentPane().add(jLabel4);
		jLabel4.setBounds(10, 240, 60, 20);

		tbNewChan.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(final java.awt.event.KeyEvent evt) {
				tbNewChanKeyTyped(evt);
			}
		});

		getContentPane().add(tbNewChan);
		tbNewChan.setBounds(80, 240, 90, 20);

		jLabel5.setFont(new java.awt.Font("Arial", 0, 12));
		jLabel5.setText("Key:");
		getContentPane().add(jLabel5);
		jLabel5.setBounds(172, 240, 40, 20);

		tbKey.setEnabled(false);
		getContentPane().add(tbKey);
		tbKey.setBounds(210, 240, 60, 20);

		btOK.setText("OK");
		btOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				btOKActionPerformed(evt);
			}
		});

		getContentPane().add(btOK);
		btOK.setBounds(10, 270, 260, 23);

		jButton1.setText("Update List");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		getContentPane().add(jButton1);
		jButton1.setBounds(150, 50, 119, 20);

		getContentPane().add(tbSelKey);
		tbSelKey.setBounds(190, 190, 80, 20);

		jLabel7.setFont(new java.awt.Font("Arial", 0, 12));
		jLabel7.setText("Join using key:");
		getContentPane().add(jLabel7);
		jLabel7.setBounds(90, 190, 100, 20);

		pack();
	}

	/**
	 * Request to update channel list.
	 * 
	 * @param evt
	 */
	private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) {
		UpdateChanList();
	}

	/**
	 * Set new channel key.
	 * 
	 * @param evt
	 */
	private void tbNewChanKeyTyped(final java.awt.event.KeyEvent evt) {
		tbKey.setEnabled(tbNewChan.getText().length() > 0);
	}

	/**
	 * Update channel list.
	 */
	private void UpdateChanList() {
		ltChans.setModel(new javax.swing.AbstractListModel() {
			public Object getElementAt(final int i) {
				return strings[i];
			}

			public int getSize() {
				return strings.length;
			}

			String[] strings = Manager.GetInstance().GetAvailableChannels();
		});

	}

	private javax.swing.JButton btOK;
	private javax.swing.JButton jButton1;
	private javax.swing.JLabel jLabel1;

	private javax.swing.JLabel jLabel2;

	private javax.swing.JLabel jLabel3;

	private javax.swing.JLabel jLabel4;

	private javax.swing.JLabel jLabel5;

	private javax.swing.JLabel jLabel7;

	private javax.swing.JScrollPane jScrollPane1;

	private javax.swing.JList ltChans;

	private javax.swing.JTextField tbKey;

	private javax.swing.JTextField tbNewChan;

	private javax.swing.JTextField tbNickname;

	private javax.swing.JTextField tbSelKey;

}
